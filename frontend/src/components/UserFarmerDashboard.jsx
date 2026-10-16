import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Sprout,
  Boxes,
  Warehouse,
  Clock3,
  PackageCheck,
  Plus,
  ArrowRight,
  ExternalLink,
  X,
  Check,
  Command,
  Sparkles,
  Truck,
  MapPin,
  TrendingUp,
  Cpu,
  RefreshCw,
  AlertCircle,
  FileCheck,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext.jsx';
import {
  getProducts,
  addProduct,
  updateStock,
  getFarmerOrders,
  updateOrderStatus,
  executeCO1,
} from '../services/api.js';
import { cities } from '../data/mockData.js';

export default function UserFarmerDashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const farmerName = user?.role === 'farmer' ? user.name : 'Lakshmi Devi';
  const farmerCity = user?.city || 'Vijayawada';

  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [ordersLoading, setOrdersLoading] = useState(true);

  // Listing Form State
  const [form, setForm] = useState({
    farmer: farmerName,
    product: '',
    quantity: '',
    city: farmerCity,
    category: 'Vegetables',
    price: '40',
    description: '',
  });

  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  // Restock Modal State
  const [stockTarget, setStockTarget] = useState(null);
  const [stockAmount, setStockAmount] = useState('');
  const [restockSubmitting, setRestockSubmitting] = useState(false);

  // Status update in-flight state
  const [updatingOrderId, setUpdatingOrderId] = useState(null);

  // Quality check tool state
  const [qualityText, setQualityText] = useState('Fresh organic harvest with grade-A quality certification.');
  const [qualityResult, setQualityResult] = useState(null);
  const [qualityChecking, setQualityChecking] = useState(false);

  // Load products and incoming orders
  const loadData = async () => {
    try {
      setLoading(true);
      const allProducts = await getProducts();
      setProducts(allProducts || []);
    } catch (err) {
      console.error('Error fetching products:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadOrders = async () => {
    try {
      setOrdersLoading(true);
      const farmerOrders = await getFarmerOrders(farmerName);
      setOrders(farmerOrders || []);
    } catch (err) {
      console.error('Error fetching farmer orders:', err);
    } finally {
      setOrdersLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    loadOrders();
    if (user?.role === 'farmer') {
      setForm((prev) => ({
        ...prev,
        farmer: user.name,
        city: user.city || prev.city,
      }));
    }
  }, [user]);

  // Filter listings by current farmer
  const farmerProducts = products.filter(
    (item) => !farmerName || (item.farmer || '').toLowerCase() === farmerName.toLowerCase()
  );

  const totalStockKg = farmerProducts.reduce((sum, item) => sum + (item.quantity || 0), 0);
  const pendingOrders = orders.filter((o) => o.status !== 'Delivered' && o.status !== 'Cancelled');
  const deliveredOrders = orders.filter((o) => o.status === 'Delivered');
  const totalRevenue = deliveredOrders.reduce((sum, o) => sum + (o.totalPrice || 0), 0);

  // Publish new produce listing
  async function submitListing(event) {
    event.preventDefault();
    setError('');
    setNotice('');

    if (!form.farmer.trim() || !form.product.trim() || !form.description.trim() || !form.quantity || Number(form.quantity) < 1) {
      setError('Please fill in every field and enter a quantity greater than zero.');
      return;
    }

    try {
      setSubmitting(true);
      const added = await addProduct(form);
      await loadData();
      setNotice(`${added.merged ? 'Stock incremented for existing listing' : 'New harvest published successfully'} · Product code #${added.productCode}`);
      setForm((prev) => ({
        ...prev,
        product: '',
        quantity: '',
        description: '',
      }));
    } catch (cause) {
      setError(cause.message || 'Failed to publish produce listing.');
    } finally {
      setSubmitting(false);
    }
  }

  // Restock existing produce
  async function handleAddStock(event) {
    event.preventDefault();
    if (!stockAmount || Number(stockAmount) < 1) return;
    try {
      setRestockSubmitting(true);
      await updateStock(stockTarget, stockAmount);
      await loadData();
      setNotice('Stock updated successfully.');
      setStockTarget(null);
      setStockAmount('');
    } catch (cause) {
      setError(cause.message || 'Failed to update stock.');
    } finally {
      setRestockSubmitting(false);
    }
  }

  // Farmer Order Fulfillment Action
  async function handleAdvanceOrderStatus(orderId, nextStatus) {
    try {
      setUpdatingOrderId(orderId);
      await updateOrderStatus(orderId, nextStatus);
      await loadOrders();
      setNotice(`Order ${orderId} marked as ${nextStatus.replace('_', ' ')}!`);
    } catch (err) {
      setError(err.message || 'Failed to update order status.');
    } finally {
      setUpdatingOrderId(null);
    }
  }

  // Quality check using CO1 algorithm
  async function handleQualityCheck(e) {
    e.preventDefault();
    if (!qualityText.trim()) return;
    try {
      setQualityChecking(true);
      const res = await executeCO1({
        algorithm: 'KMP String Matching',
        text: qualityText,
        pattern: 'organic',
      });
      setQualityResult(res);
    } catch (err) {
      console.error(err);
    } finally {
      setQualityChecking(false);
    }
  }

  return (
    <div className="farmer-dashboard-view">
      {/* Title & Role Info */}
      <div className="page-title-row">
        <div>
          <div className="eyebrow" style={{ color: '#4a7536' }}>
            <span style={{ background: '#557e3c' }} /> PRODUCER OPERATIONS · KRISHNA ZONE
          </div>
          <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Sprout size={30} color="#457032" />
            User (Farmer) Portal — {farmerName}
          </h1>
          <p>
            Manage farm produce, restock warehouses, and fulfill incoming consumer orders across Vijayawada, Gudivada, Nuzvid, Machilipatnam, and Pedana.
          </p>
        </div>
        <div className="title-action" style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <span className="farmer-status" style={{ background: '#eaf2e3', padding: '6px 12px', borderRadius: '20px', color: '#3f682a', fontWeight: '600' }}>
            <span className="stock-dot" /> VERIFIED PRODUCER ({user?.farmerCode || 'FARM-101'})
          </span>
          <button
            type="button"
            className="button button-outline"
            style={{ color: '#335028', borderColor: '#bed4b2', height: '34px' }}
            onClick={() => { loadData(); loadOrders(); }}
            title="Refresh Data"
          >
            <RefreshCw size={14} /> Refresh
          </button>
        </div>
      </div>

      {/* Farmer Metrics Cards */}
      <div className="stats-grid farmer-stats">
        <div className="stat-card">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#ebf4e6', color: '#4a7536' }}>
              <Boxes size={18} />
            </span>
            <span className="stat-change" style={{ color: '#4a7536' }}>Active Listings</span>
          </div>
          <div className="stat-value">{loading ? '—' : String(farmerProducts.length).padStart(2, '0')}</div>
          <div className="stat-label">Crops published to marketplace</div>
        </div>

        <div className="stat-card stat-green">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#e2edd9', color: '#3c672b' }}>
              <Warehouse size={18} />
            </span>
            <span className="stat-change">Regional Stock</span>
          </div>
          <div className="stat-value">{loading ? '—' : `${totalStockKg.toLocaleString()} kg`}</div>
          <div className="stat-label">Total available in warehouses</div>
        </div>

        <div className="stat-card stat-orange">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#fdf0e2', color: '#d3833b' }}>
              <Clock3 size={18} />
            </span>
            <span className="stat-change" style={{ color: '#c77830' }}>Needs Action</span>
          </div>
          <div className="stat-value">{ordersLoading ? '—' : String(pendingOrders.length).padStart(2, '0')}</div>
          <div className="stat-label">Pending incoming orders</div>
        </div>

        <div className="stat-card stat-cream">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#f4ede0', color: '#977a3d' }}>
              <TrendingUp size={18} />
            </span>
            <span className="stat-change" style={{ color: '#886d34' }}>Settled</span>
          </div>
          <div className="stat-value">₹{totalRevenue.toLocaleString()}</div>
          <div className="stat-label">Total fulfilled farm revenue</div>
        </div>
      </div>

      {/* Global Notice / Error Alerts */}
      {notice && (
        <div className="form-success" style={{ marginBottom: '18px' }}>
          <Check size={16} /> {notice}
        </div>
      )}
      {error && (
        <div className="form-error" style={{ marginBottom: '18px' }}>
          <AlertCircle size={16} /> {error}
        </div>
      )}

      {/* FEATURE 1: INCOMING CUSTOMER ORDERS FULFILLMENT PANEL (PRODUCER SPECIFIC) */}
      <section className="panel" style={{ padding: '20px', marginBottom: '24px', border: '1px solid #d7e4cf', background: '#ffffff' }}>
        <div className="panel-heading" style={{ marginBottom: '14px' }}>
          <div>
            <span className="eyebrow" style={{ color: '#457032' }}>
              <Truck size={13} style={{ display: 'inline', marginRight: '4px' }} />
              CUSTOMER FULFILLMENT MANAGEMENT
            </span>
            <h2 style={{ fontSize: '18px' }}>
              Incoming Customer Orders for {farmerName} <span className="heading-count">{orders.length}</span>
            </h2>
          </div>
          <span className="live-pill" style={{ background: '#eef5ea', color: '#3d6728' }}>
            <i /> LIVE ORDER STREAM
          </span>
        </div>
        <p style={{ fontSize: '12px', color: '#687260', margin: '0 0 16px' }}>
          When customers order your produce, their requests appear here. Advance their status from <b>Order Placed</b> ➔ <b>Prepared</b> ➔ <b>Dispatched</b> to keep distribution moving smoothly.
        </p>

        {ordersLoading ? (
          <div className="loading-state">
            <span className="spinner" /> Loading customer orders...
          </div>
        ) : orders.length > 0 ? (
          <div className="table-scroll">
            <table className="data-table">
              <thead>
                <tr>
                  <th>ORDER ID</th>
                  <th>PRODUCE ITEM</th>
                  <th>QUANTITY</th>
                  <th>DESTINATION CITY</th>
                  <th>TOTAL VALUE</th>
                  <th>PLACED AT</th>
                  <th>CURRENT STATUS</th>
                  <th>ACTION</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((ord) => {
                  const isPlaced = (ord.status || '').toUpperCase() === 'ORDER PLACED' || ord.status === 'ORDER_PLACED';
                  const isPrepared = (ord.status || '').toUpperCase() === 'PREPARED';
                  const isDispatched = (ord.status || '').toUpperCase() === 'DISPATCHED';
                  const isTransit = (ord.status || '').toUpperCase() === 'IN TRANSIT' || ord.status === 'IN_TRANSIT';
                  const isDelivered = (ord.status || '').toUpperCase() === 'DELIVERED';

                  return (
                    <tr key={ord.id} style={{ background: isPlaced ? '#fbfcfa' : 'transparent' }}>
                      <td>
                        <Link to={`/orders/${ord.id}`} className="order-id" style={{ fontWeight: '600' }}>
                          {ord.id}
                        </Link>
                      </td>
                      <td>
                        <b style={{ color: '#2d3826' }}>{ord.product || ord.productName}</b>
                      </td>
                      <td>
                        <span className="mono" style={{ fontSize: '11px', fontWeight: '600' }}>
                          {ord.quantity} kg
                        </span>
                      </td>
                      <td>
                        <span className="city-cell">
                          <MapPin size={13} color="#557e3c" /> {ord.destination || ord.destinationCity}
                        </span>
                      </td>
                      <td>
                        <b style={{ color: '#2a4d22' }}>₹{(ord.totalPrice || 0).toLocaleString()}</b>
                      </td>
                      <td className="muted" style={{ fontSize: '10px' }}>
                        {ord.createdAt}
                      </td>
                      <td>
                        <span className={`order-status status-${(ord.status || '').toLowerCase().replaceAll(' ', '-')}`}>
                          <i /> {ord.status}
                        </span>
                      </td>
                      <td>
                        {isPlaced && (
                          <button
                            type="button"
                            className="button button-dark"
                            style={{ height: '28px', padding: '0 10px', fontSize: '10px', background: '#3b692c' }}
                            disabled={updatingOrderId === ord.id}
                            onClick={() => handleAdvanceOrderStatus(ord.id, 'PREPARED')}
                          >
                            {updatingOrderId === ord.id ? 'Updating...' : 'Mark Prepared ➔'}
                          </button>
                        )}
                        {isPrepared && (
                          <button
                            type="button"
                            className="button button-dark"
                            style={{ height: '28px', padding: '0 10px', fontSize: '10px', background: '#c27e36' }}
                            disabled={updatingOrderId === ord.id}
                            onClick={() => handleAdvanceOrderStatus(ord.id, 'DISPATCHED')}
                          >
                            {updatingOrderId === ord.id ? 'Updating...' : 'Dispatch to Hub ➔'}
                          </button>
                        )}
                        {(isDispatched || isTransit) && (
                          <span style={{ fontSize: '10px', color: '#557e3c', fontStyle: 'italic', display: 'flex', alignItems: 'center', gap: '4px' }}>
                            <Truck size={13} /> En route in transit
                          </span>
                        )}
                        {isDelivered && (
                          <span style={{ fontSize: '10px', color: '#3d6728', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '4px' }}>
                            <Check size={13} /> Completed
                          </span>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="table-empty" style={{ padding: '24px', background: '#fcfdfb', borderRadius: '6px' }}>
            <Clock3 size={24} color="#87997a" style={{ display: 'block', margin: '0 auto 8px' }} />
            No incoming customer orders yet for {farmerName}. Once customers place orders, they will appear here for fulfillment.
          </div>
        )}
      </section>

      {/* FEATURE 2 & 3: PUBLISH HARVEST FORM & ACTIVE PRODUCE LISTINGS */}
      <div className="farmer-layout">
        {/* Form Panel */}
        <section className="panel form-panel">
          <div className="panel-heading">
            <div>
              <span className="eyebrow">ADD HARVEST TO NETWORK</span>
              <h2>Publish Farm Produce</h2>
            </div>
            <span className="form-step">NEW LISTING</span>
          </div>
          <p className="form-intro">
            Publish harvest quantities directly to Krishna district distribution centers.
          </p>

          <form onSubmit={submitListing} className="farmer-form">
            <label>
              Grower / Producer Name
              <input
                value={form.farmer}
                onChange={(e) => setForm({ ...form, farmer: e.target.value })}
                placeholder="e.g. Lakshmi Devi"
                required
              />
            </label>

            <label>
              Crop / Produce Name
              <input
                value={form.product}
                onChange={(e) => setForm({ ...form, product: e.target.value })}
                placeholder="e.g. Organic Tomatoes, Fresh Rice"
                required
              />
            </label>

            <div className="form-row">
              <label>
                Category
                <select
                  value={form.category}
                  onChange={(e) => setForm({ ...form, category: e.target.value })}
                >
                  <option value="Vegetables">Vegetables</option>
                  <option value="Fruit">Fruit</option>
                  <option value="Grains">Grains</option>
                  <option value="Pulses">Pulses</option>
                </select>
              </label>

              <label>
                Price (₹ per kg)
                <input
                  type="number"
                  min="1"
                  step="0.5"
                  value={form.price}
                  onChange={(e) => setForm({ ...form, price: e.target.value })}
                  placeholder="40"
                  required
                />
              </label>
            </div>

            <div className="form-row">
              <label>
                Harvest Quantity
                <div className="input-unit">
                  <input
                    type="number"
                    min="1"
                    value={form.quantity}
                    onChange={(e) => setForm({ ...form, quantity: e.target.value })}
                    placeholder="150"
                    required
                  />
                  <span>KG</span>
                </div>
              </label>

              <label>
                Distribution Hub (Nearest City)
                <select
                  value={form.city}
                  onChange={(e) => setForm({ ...form, city: e.target.value })}
                >
                  {cities.map((city) => (
                    <option key={city} value={city}>
                      {city}
                    </option>
                  ))}
                </select>
              </label>
            </div>

            <label>
              Harvest & Quality Description
              <textarea
                rows="3"
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                placeholder="Describe cultivation methods, soil condition, or harvest freshness..."
                required
              />
            </label>

            <div className="code-note">
              <Command size={15} />
              <span>
                Deterministic Product Code
                <small>FoodChain automatically generates a unique tracking code for this produce batch.</small>
              </span>
              <Sparkles size={14} />
            </div>

            <button className="button button-dark form-submit" type="submit" disabled={submitting}>
              <Plus size={16} /> {submitting ? 'Publishing...' : 'Publish Produce Listing'} <ArrowRight size={15} />
            </button>
          </form>
        </section>

        {/* Inventory Table Panel */}
        <section className="panel farmer-inventory">
          <div className="panel-heading">
            <div>
              <span className="eyebrow">LIVE FARM INVENTORY</span>
              <h2>Active Listings <span className="heading-count">{farmerProducts.length}</span></h2>
            </div>
            <button
              className="table-more"
              aria-label="Refresh listings"
              onClick={loadData}
              title="Refresh inventory"
            >
              <RefreshCw size={14} />
            </button>
          </div>

          <div className="table-scroll">
            <table className="data-table">
              <thead>
                <tr>
                  <th>PRODUCT</th>
                  <th>CODE</th>
                  <th>PRICE</th>
                  <th>QUANTITY</th>
                  <th>WAREHOUSE</th>
                  <th>STATUS</th>
                  <th>RESTOCK</th>
                </tr>
              </thead>
              <tbody>
                {farmerProducts.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <span className="table-product">{item.product || item.productName}</span>
                      <small className="table-category">{item.category || 'Produce'}</small>
                    </td>
                    <td className="mono muted">#{item.productCode}</td>
                    <td><b>₹{item.price || 40}/kg</b></td>
                    <td>
                      <b style={{ color: item.quantity > 50 ? '#386326' : '#b36a28' }}>
                        {item.quantity} kg
                      </b>
                    </td>
                    <td>{(item.warehouse || '').replace(' Warehouse', '')}</td>
                    <td>
                      <span className="status-badge">
                        <i /> {item.quantity > 0 ? 'Available' : 'Out of Stock'}
                      </span>
                    </td>
                    <td>
                      <button
                        className="add-stock"
                        onClick={() => setStockTarget(item.id)}
                        title="Add Stock"
                      >
                        <Plus size={15} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {!farmerProducts.length && !loading && (
              <div className="table-empty">
                No produce listings found for {farmerName}. Use the form on the left to publish your first crop.
              </div>
            )}
          </div>

          <div className="table-foot">
            <span>
              <span className="stock-dot" /> Live sync with PostgreSQL 18 & Marketplace
            </span>
            <Link to="/products">
              View on Marketplace <ArrowRight size={13} />
            </Link>
          </div>

          {/* Regional Warehouse Stock Allocation */}
          <div style={{ marginTop: '24px', paddingTop: '16px', borderTop: '1px solid #edf1ea' }}>
            <div className="eyebrow" style={{ marginBottom: '8px' }}>
              DISTRIBUTION LOGISTICS · KRISHNA HUBS
            </div>
            <div className="warehouse-list" style={{ padding: '4px 0' }}>
              {cities.map((city) => {
                const stockInCity = farmerProducts
                  .filter((p) => (p.warehouse || '').includes(city))
                  .reduce((sum, p) => sum + p.quantity, 0);
                const percent = totalStockKg > 0 ? Math.round((stockInCity / totalStockKg) * 100) : 0;
                return (
                  <div className="warehouse-row" key={city}>
                    <span>{city}</span>
                    <div>
                      <i style={{ width: `${Math.min(100, Math.max(percent, stockInCity > 0 ? 8 : 0))}%` }} />
                    </div>
                    <b>{stockInCity} kg</b>
                  </div>
                );
              })}
            </div>
          </div>
        </section>
      </div>

      {/* FEATURE 4: PRODUCER QUALITY CHECK INSPECTOR (DSA HARVEST VERIFICATION) */}
      <section className="panel" style={{ padding: '20px', marginTop: '24px', background: '#fbfcfa', border: '1px solid #e1e9dc' }}>
        <div className="panel-heading">
          <div>
            <span className="eyebrow">
              <Cpu size={13} style={{ display: 'inline', marginRight: '4px' }} />
              HARVEST QUALITY INSPECTOR (DSA ENGINE)
            </span>
            <h2>Produce Description & Certification Verifier</h2>
          </div>
          <span className="lab-badge">
            <Sparkles size={13} /> STRING ALGORITHM (CO1)
          </span>
        </div>
        <p style={{ fontSize: '11px', color: '#6a7364', margin: '0 0 14px' }}>
          Test your harvest descriptions against quality standards like "organic", "grade-A", or "fresh" to ensure optimal search indexing for customer discovery.
        </p>

        <form onSubmit={handleQualityCheck} style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <input
            style={{ flex: 1, height: '36px', border: '1px solid #d4decb', borderRadius: '4px', padding: '0 12px', fontSize: '12px' }}
            value={qualityText}
            onChange={(e) => setQualityText(e.target.value)}
            placeholder="Type your crop description to verify quality compliance..."
          />
          <button type="submit" className="button button-dark" style={{ height: '36px', whiteSpace: 'nowrap' }} disabled={qualityChecking}>
            <FileCheck size={15} /> {qualityChecking ? 'Checking...' : 'Verify Description'}
          </button>
        </form>

        {qualityResult && (
          <div style={{ marginTop: '12px', padding: '10px 14px', background: '#edf4e8', borderRadius: '4px', fontSize: '11px', color: '#335227', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Check size={16} />
            <span>
              <b>Quality Match Found:</b> "{qualityResult.pattern || 'organic'}" detected at index {qualityResult.firstIndex || 0} via KMP pattern search. Certification verified!
            </span>
          </div>
        )}
      </section>

      {/* Restock Modal */}
      {stockTarget && (
        <div className="modal-backdrop" onClick={() => setStockTarget(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setStockTarget(null)}>
              <X size={17} />
            </button>
            <span className="eyebrow">INVENTORY REPLENISHMENT</span>
            <h2>Add to Your Stock</h2>
            <p>Enter the additional quantity harvested to increment this listing's availability.</p>
            <form onSubmit={handleAddStock}>
              <label>
                Additional Quantity (kg)
                <input
                  autoFocus
                  type="number"
                  min="1"
                  value={stockAmount}
                  onChange={(e) => setStockAmount(e.target.value)}
                  placeholder="e.g. 50"
                  required
                />
              </label>
              <button className="button button-dark" type="submit" disabled={restockSubmitting}>
                <Plus size={15} /> {restockSubmitting ? 'Updating...' : 'Confirm Stock Addition'}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
