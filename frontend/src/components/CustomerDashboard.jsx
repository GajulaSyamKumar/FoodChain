import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  ShoppingBasket,
  Search,
  MapPin,
  ArrowRight,
  ArrowUpRight,
  Truck,
  PackageCheck,
  Sparkles,
  Filter,
  ChevronRight,
  Boxes,
  Plus,
  Minus,
  Check,
  X,
  AlertCircle,
  Clock3,
  CookingPot,
  RefreshCw,
  ShieldCheck,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext.jsx';
import { getProducts, getCustomerOrders, placeOrder, cancelOrder } from '../services/api.js';
import { cities } from '../data/mockData.js';

const timelineSteps = ['Order Placed', 'Prepared', 'Dispatched', 'In Transit', 'Out for Delivery', 'Delivered'];
const stepIcons = [Check, CookingPot, Boxes, Truck, MapPin, PackageCheck];

export default function CustomerDashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const isCustomer = user && user.role === 'customer';
  const customerName = isCustomer ? user.name : 'Guest Customer';
  const customerEmail = isCustomer ? (user.email || 'Not provided') : 'Not signed in';

  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');
  const [selectedCity, setSelectedCity] = useState('All');
  const [selectedCategory, setSelectedCategory] = useState('All');

  // Quick Order In-Page Modal State
  const [orderModalProduct, setOrderModalProduct] = useState(null);
  const [orderQuantity, setOrderQuantity] = useState(2);
  const [orderDestination, setOrderDestination] = useState('Vijayawada');
  const [ordering, setOrdering] = useState(false);
  const [orderNotice, setOrderNotice] = useState('');
  const [orderError, setOrderError] = useState('');

  // Selected order to view detailed delivery timeline
  const [trackedOrder, setTrackedOrder] = useState(null);

  // Fetch live products and ONLY this customer's orders from PostgreSQL
  const fetchData = async () => {
    try {
      setLoading(true);
      const prodList = await getProducts();
      setProducts(prodList || []);

      if (user && user.role === 'customer' && user.id) {
        const myCustomerOrders = await getCustomerOrders(user.id).catch(() => []);
        setOrders(myCustomerOrders || []);
        if (myCustomerOrders && myCustomerOrders.length > 0) {
          setTrackedOrder(myCustomerOrders[0]);
        } else {
          setTrackedOrder(null);
        }
      } else {
        setOrders([]);
        setTrackedOrder(null);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [user]);

  // Filter products by search, city, and category
  const filteredProducts = products.filter((p) => {
    const name = (p.product || p.productName || '').toLowerCase();
    const matchesQuery = !query.trim() || name.includes(query.trim().toLowerCase());
    const matchesCity = selectedCity === 'All' || (p.warehouse || '').includes(selectedCity);
    const matchesCategory = selectedCategory === 'All' || (p.category || '').toLowerCase() === selectedCategory.toLowerCase();
    return matchesQuery && matchesCity && matchesCategory;
  });

  // Orders are already scoped to this customer from backend /api/orders/customer/:id
  const myOrders = orders;

  const activeDeliveries = myOrders.filter((o) => o.status !== 'Delivered' && o.status !== 'Cancelled');
  const deliveredCount = myOrders.filter((o) => o.status === 'Delivered').length;

  // Open Quick Order Modal
  const openQuickOrder = (product) => {
    if (!isCustomer) {
      navigate('/login');
      return;
    }
    setOrderModalProduct(product);
    setOrderQuantity(Math.min(2, product.quantity || 1));
    setOrderDestination(cities[0]);
    setOrderError('');
    setOrderNotice('');
  };

  // Submit quick order with strictly bound customerId
  const handleConfirmOrder = async (e) => {
    e.preventDefault();
    if (!orderModalProduct) return;
    setOrderError('');
    setOrderNotice('');

    if (!user || user.role !== 'customer' || !user.id) {
      setOrderError('Please sign in or register as a customer to place an order.');
      return;
    }

    setOrdering(true);

    try {
      const created = await placeOrder({
        productId: orderModalProduct.id,
        quantity: orderQuantity,
        destination: orderDestination,
        customerId: user.id,
      });

      setOrderNotice(`Order placed successfully! Order ID: ${created.id}`);
      await fetchData();
      setTrackedOrder(created);
      setTimeout(() => {
        setOrderModalProduct(null);
      }, 1500);
    } catch (err) {
      setOrderError(err.message || 'Failed to place order.');
    } finally {
      setOrdering(false);
    }
  };

  // Handle Cancel Order
  const handleCancelOrder = async (orderId) => {
    if (!window.confirm(`Are you sure you want to cancel order ${orderId}?`)) return;
    try {
      await cancelOrder(orderId);
      await fetchData();
    } catch (err) {
      alert(err.message || 'Unable to cancel order.');
    }
  };

  // Timeline computation for active tracked order
  const currentActiveOrder = trackedOrder || (myOrders.length > 0 ? myOrders[0] : null);
  const activeStep = currentActiveOrder
    ? Math.max(
        0,
        timelineSteps.findIndex(
          (step) => step.toLowerCase().replace(/[\s_]+/g, '') === (currentActiveOrder.status || '').toLowerCase().replace(/[\s_]+/g, '')
        )
      )
    : 0;

  return (
    <div className="customer-dashboard">
      {/* Title & Customer Status */}
      <div className="page-title-row">
        <div>
          <div className="eyebrow" style={{ color: '#d77c5a' }}>
            <span style={{ background: '#d77c5a' }} /> CUSTOMER STOREFRONT · KRISHNA REGIONAL MARKETPLACE
          </div>
          <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <ShoppingBasket size={30} color="#d77c5a" />
            Customer Store — Fresh Food &amp; Fast Delivery
          </h1>
          <p>
            Welcome, <b>{customerName}</b> ({customerEmail})! Explore fresh harvest directly from local growers in Vijayawada, Gudivada, Machilipatnam, Nuzvid, and Pedana.
          </p>
        </div>
        <div className="title-action" style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <span className="role-badge-pill role-badge-customer" style={{ padding: '6px 12px', fontSize: '11px' }}>
            <ShieldCheck size={14} /> CUSTOMER PORTAL
          </span>
          <button
            type="button"
            className="button button-outline"
            style={{ color: '#684534', borderColor: '#e4c4b6', height: '34px' }}
            onClick={fetchData}
            title="Refresh Storefront"
          >
            <RefreshCw size={14} /> Refresh
          </button>
        </div>
      </div>

      {/* Customer Quick Stats */}
      {!isCustomer && (
        <div style={{ background: '#fff5f0', border: '1px solid #f2cfbf', borderRadius: '10px', padding: '14px 18px', marginBottom: '20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '15px', flexWrap: 'wrap' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <AlertCircle size={22} color="#d77c5a" />
            <div>
              <b style={{ color: '#883b19', display: 'block', fontSize: '13px' }}>Customer Account Required for Orders &amp; Tracking</b>
              <span style={{ color: '#735043', fontSize: '12px' }}>
                You are currently browsing the public catalog. To place orders and view your private deliveries, sign in or enroll your customer credentials in PostgreSQL (pgAdmin).
              </span>
            </div>
          </div>
          <button
            type="button"
            className="button button-dark"
            style={{ background: '#d77c5a', borderColor: '#d77c5a', whiteSpace: 'nowrap', height: '34px', fontSize: '12px' }}
            onClick={() => navigate('/login')}
          >
            Sign In / Register Customer
          </button>
        </div>
      )}

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#fdf1ec', color: '#d77c5a' }}>
              <ShoppingBasket size={18} />
            </span>
            <span className="stat-change" style={{ color: '#d77c5a' }}>Marketplace</span>
          </div>
          <div className="stat-value">{filteredProducts.length}</div>
          <div className="stat-label">Available harvest listings</div>
        </div>

        <div className="stat-card stat-green">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#e9f3e4', color: '#4d753b' }}>
              <Truck size={18} />
            </span>
            <span className="stat-change">On the Way</span>
          </div>
          <div className="stat-value">{activeDeliveries.length}</div>
          <div className="stat-label">My active deliveries</div>
        </div>

        <div className="stat-card stat-orange">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#fdf2e3', color: '#d1863d' }}>
              <PackageCheck size={18} />
            </span>
            <span className="stat-change" style={{ color: '#bc7530' }}>Delivered</span>
          </div>
          <div className="stat-value">{deliveredCount}</div>
          <div className="stat-label">Fulfilled orders</div>
        </div>

        <div className="stat-card stat-cream">
          <div className="stat-top">
            <span className="stat-icon" style={{ background: '#f7f1e4', color: '#9a7f3e' }}>
              <MapPin size={18} />
            </span>
            <span className="stat-change">5 Hubs</span>
          </div>
          <div className="stat-value">Krishna</div>
          <div className="stat-label">Connected delivery network</div>
        </div>
      </div>

      {/* FEATURE 1: LIVE DELIVERY TRACKER PREVIEW (CUSTOMER EXCLUSIVE) */}
      {currentActiveOrder && (
        <section className="panel" style={{ padding: '20px', marginBottom: '28px', border: '1px solid #ebd9cf', background: '#fff' }}>
          <div className="panel-heading" style={{ marginBottom: '16px' }}>
            <div>
              <span className="eyebrow" style={{ color: '#d77c5a' }}>
                <Truck size={13} style={{ display: 'inline', marginRight: '4px' }} />
                REAL-TIME SHIPMENT TRACKING
              </span>
              <h2>
                Tracking Order #{currentActiveOrder.id} — {currentActiveOrder.product || currentActiveOrder.productName}
              </h2>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span className={`order-status status-${(currentActiveOrder.status || '').toLowerCase().replaceAll(' ', '-')}`}>
                <i /> {currentActiveOrder.status}
              </span>
              <Link to={`/orders/${currentActiveOrder.id}`} className="button button-dark" style={{ height: '30px', padding: '0 12px', fontSize: '11px', background: '#35563a' }}>
                Full Route Map <ArrowRight size={13} />
              </Link>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', marginBottom: '18px', padding: '12px 14px', background: '#fdfbf9', borderRadius: '6px', border: '1px solid #f1eae5' }}>
            <div>
              <small className="muted" style={{ display: 'block', fontSize: '10px' }}>QUANTITY</small>
              <b style={{ color: '#333' }}>{currentActiveOrder.quantity} kg</b>
            </div>
            <div style={{ borderLeft: '1px solid #ebd9cf', paddingLeft: '14px' }}>
              <small className="muted" style={{ display: 'block', fontSize: '10px' }}>FARM ORIGIN</small>
              <b style={{ color: '#333' }}>{currentActiveOrder.farmer || 'Local Grower'} ({currentActiveOrder.warehouse})</b>
            </div>
            <div style={{ borderLeft: '1px solid #ebd9cf', paddingLeft: '14px' }}>
              <small className="muted" style={{ display: 'block', fontSize: '10px' }}>DESTINATION</small>
              <b style={{ color: '#d77c5a' }}><MapPin size={12} style={{ display: 'inline' }} /> {currentActiveOrder.destination || currentActiveOrder.destinationCity}</b>
            </div>
            <div style={{ borderLeft: '1px solid #ebd9cf', paddingLeft: '14px' }}>
              <small className="muted" style={{ display: 'block', fontSize: '10px' }}>TOTAL PAID</small>
              <b style={{ color: '#38652d' }}>₹{(currentActiveOrder.totalPrice || 0).toLocaleString()}</b>
            </div>
          </div>

          {/* 6-Step Visual Delivery Progress Bar */}
          <div className="timeline-horizontal" style={{ display: 'grid', gridTemplateColumns: 'repeat(6, 1fr)', gap: '6px', position: 'relative' }}>
            {timelineSteps.map((step, idx) => {
              const Icon = stepIcons[idx];
              const isPast = idx < activeStep;
              const isCurrent = idx === activeStep;

              return (
                <div
                  key={step}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    textAlign: 'center',
                    padding: '8px 4px',
                    borderRadius: '6px',
                    background: isCurrent ? '#fbf0eb' : isPast ? '#f1f8ed' : '#fafafa',
                    border: isCurrent ? '1.5px solid #d77c5a' : isPast ? '1px solid #c9dec1' : '1px solid #ededed',
                  }}
                >
                  <div
                    style={{
                      width: '26px',
                      height: '26px',
                      borderRadius: '50%',
                      display: 'grid',
                      placeItems: 'center',
                      background: isCurrent ? '#d77c5a' : isPast ? '#527b40' : '#e6e6e6',
                      color: isCurrent || isPast ? '#fff' : '#999',
                      marginBottom: '6px',
                    }}
                  >
                    <Icon size={14} />
                  </div>
                  <b style={{ fontSize: '10px', color: isCurrent ? '#d77c5a' : isPast ? '#365b27' : '#999' }}>
                    {step}
                  </b>
                  <small style={{ fontSize: '9px', color: '#888', marginTop: '2px' }}>
                    {isCurrent ? 'Current' : isPast ? 'Done' : 'Pending'}
                  </small>
                </div>
              );
            })}
          </div>
        </section>
      )}

      {/* FEATURE 2: SEARCH, CITY & CATEGORY FILTERS */}
      <div className="catalog-toolbar" style={{ flexWrap: 'wrap', gap: '10px', marginBottom: '22px' }}>
        <label className="search-field" style={{ minWidth: '280px', flex: 1 }}>
          <Search size={18} color="#d77c5a" />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search produce (e.g. Tomatoes, Carrots, Onions, Rice, Bananas)..."
          />
          {query && (
            <button
              type="button"
              onClick={() => setQuery('')}
              style={{ background: 'transparent', border: 0, cursor: 'pointer', color: '#999' }}
            >
              <X size={15} />
            </button>
          )}
        </label>

        {/* Category Filter */}
        <div style={{ display: 'flex', gap: '5px' }}>
          {['All', 'Vegetables', 'Fruit', 'Grains'].map((cat) => (
            <button
              key={cat}
              type="button"
              className={`city-pill ${selectedCategory === cat ? 'active' : ''}`}
              style={{
                background: selectedCategory === cat ? '#d77c5a' : '#fff',
                color: selectedCategory === cat ? '#fff' : '#555',
                borderColor: selectedCategory === cat ? '#d77c5a' : '#e3e5de',
              }}
              onClick={() => setSelectedCategory(cat)}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* City Filter */}
        <div className="city-pill-selector">
          {['All', 'Vijayawada', 'Gudivada', 'Machilipatnam', 'Nuzvid', 'Pedana'].map((c) => (
            <button
              key={c}
              type="button"
              className={`city-pill ${selectedCity === c ? 'active' : ''}`}
              onClick={() => setSelectedCity(c)}
            >
              {c}
            </button>
          ))}
        </div>
      </div>

      {/* FEATURE 3: FRESH HARVEST CATALOG GRID */}
      {loading ? (
        <div className="loading-state">
          <span className="spinner" /> Loading harvest from Krishna growers...
        </div>
      ) : filteredProducts.length ? (
        <div className="product-grid">
          {filteredProducts.map((p) => (
            <article key={p.id} className="product-card">
              <Link to={`/products/${p.id}`} className="product-image-wrap">
                <img src={p.image} alt={p.product || p.productName} />
                <span className="product-category" style={{ background: 'rgba(255,255,255,0.95)', color: '#d77c5a', fontWeight: '600' }}>
                  {p.category || 'Vegetables'}
                </span>
                <span className="image-arrow"><ArrowUpRight size={16} /></span>
              </Link>

              <div className="product-card-body">
                <div className="product-meta">
                  <span>
                    <span className="stock-dot" /> {p.quantity} kg in stock
                  </span>
                  <span className="product-zone">KRISHNA HUB</span>
                </div>

                <Link to={`/products/${p.id}`} className="product-name">
                  {p.product || p.productName}
                </Link>

                <div className="product-farmer">
                  <span className="farmer-mini" style={{ background: '#f8eee9', color: '#d77c5a' }}>
                    {(p.farmer || 'Farmer').split(' ').map((n) => n[0]).join('')}
                  </span>
                  <span>Grown by <b>{p.farmer}</b></span>
                  <span className="farmer-separator">·</span>
                  <span>{(p.warehouse || '').replace(' Warehouse', '')}</span>
                </div>

                <div className="product-card-bottom">
                  <span className="product-price">
                    ₹{p.price || 40.0}<small>/ kg</small>
                  </span>

                  <div style={{ display: 'flex', gap: '6px' }}>
                    <Link
                      to={`/products/${p.id}`}
                      className="small-order-button"
                      style={{ background: '#f5f6f2', color: '#333', padding: '5px 9px', borderRadius: '4px' }}
                    >
                      Details
                    </Link>
                    <button
                      type="button"
                      className="small-order-button"
                      style={{ background: '#d77c5a', color: '#fff', padding: '5px 10px', borderRadius: '4px' }}
                      disabled={p.quantity < 1}
                      onClick={() => openQuickOrder(p)}
                    >
                      Quick Order <ArrowRight size={13} />
                    </button>
                  </div>
                </div>
              </div>
            </article>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <Search size={26} color="#d77c5a" />
          <h3>No harvest matches your search "{query}"</h3>
          <p>Try searching for Organic Tomatoes, Carrots, Fresh Potatoes, Rice, or Bananas.</p>
        </div>
      )}

      {/* FEATURE 4: MY ORDERS REGISTER TABLE (CUSTOMER ORDER HISTORY) */}
      {myOrders.length > 0 && (
        <section className="panel" style={{ marginTop: '38px', padding: '20px' }}>
          <div className="panel-heading">
            <div>
              <span className="eyebrow" style={{ color: '#d77c5a' }}>CUSTOMER PURCHASE REGISTER</span>
              <h2>My Placed Orders ({myOrders.length})</h2>
            </div>
            <Link to="/orders" className="text-link">
              View all orders <ArrowRight size={14} />
            </Link>
          </div>

          <div className="table-scroll">
            <table className="data-table">
              <thead>
                <tr>
                  <th>ORDER ID</th>
                  <th>PRODUCE</th>
                  <th>QUANTITY</th>
                  <th>GROWER</th>
                  <th>DESTINATION</th>
                  <th>TOTAL (₹)</th>
                  <th>STATUS</th>
                  <th>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {myOrders.map((ord) => {
                  const isPlaced = (ord.status || '').toUpperCase() === 'ORDER PLACED' || ord.status === 'ORDER_PLACED';
                  return (
                    <tr key={ord.id}>
                      <td>
                        <Link to={`/orders/${ord.id}`} className="order-id" style={{ color: '#d77c5a', fontWeight: '600' }}>
                          {ord.id}
                        </Link>
                      </td>
                      <td>
                        <b>{ord.product || ord.productName}</b>
                      </td>
                      <td>
                        <span className="mono">{ord.quantity} kg</span>
                      </td>
                      <td>{ord.farmer || 'Farmer'}</td>
                      <td>
                        <span className="city-cell">
                          <MapPin size={13} color="#d77c5a" /> {ord.destination || ord.destinationCity}
                        </span>
                      </td>
                      <td>
                        <b style={{ color: '#31552a' }}>₹{(ord.totalPrice || 0).toLocaleString()}</b>
                      </td>
                      <td>
                        <span className={`order-status status-${(ord.status || '').toLowerCase().replaceAll(' ', '-')}`}>
                          <i /> {ord.status}
                        </span>
                      </td>
                      <td style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                        <button
                          type="button"
                          className="button button-outline"
                          style={{ height: '26px', padding: '0 8px', fontSize: '10px', color: '#555', borderColor: '#ccc' }}
                          onClick={() => setTrackedOrder(ord)}
                          title="Track delivery progress"
                        >
                          <Truck size={12} /> Track
                        </button>
                        {isPlaced && (
                          <button
                            type="button"
                            className="button button-outline"
                            style={{ height: '26px', padding: '0 8px', fontSize: '10px', color: '#b54536', borderColor: '#f0c7c2' }}
                            onClick={() => handleCancelOrder(ord.id)}
                            title="Cancel Order"
                          >
                            Cancel
                          </button>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </section>
      )}

      {/* QUICK ORDER MODAL */}
      {orderModalProduct && (
        <div className="modal-backdrop" onClick={() => setOrderModalProduct(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()} style={{ width: 'min(100%, 460px)' }}>
            <button className="modal-close" onClick={() => setOrderModalProduct(null)}>
              <X size={17} />
            </button>
            <span className="eyebrow" style={{ color: '#d77c5a' }}>DIRECT HARVEST ORDER</span>
            <h2>Order {orderModalProduct.product || orderModalProduct.productName}</h2>
            <p>
              Direct procurement from grower <b>{orderModalProduct.farmer}</b> ({orderModalProduct.warehouse}).
            </p>

            {orderNotice && (
              <div className="form-success" style={{ marginBottom: '14px' }}>
                <Check size={16} /> {orderNotice}
              </div>
            )}
            {orderError && (
              <div className="form-error" style={{ marginBottom: '14px' }}>
                <AlertCircle size={16} /> {orderError}
              </div>
            )}

            <form onSubmit={handleConfirmOrder}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '16px' }}>
                <img
                  src={orderModalProduct.image}
                  alt=""
                  style={{ width: '56px', height: '56px', borderRadius: '6px', objectFit: 'cover' }}
                />
                <div>
                  <b style={{ display: 'block', fontSize: '14px' }}>{orderModalProduct.product || orderModalProduct.productName}</b>
                  <small style={{ color: '#777' }}>
                    Available stock: {orderModalProduct.quantity} kg · Rate: ₹{orderModalProduct.price || 40}/kg
                  </small>
                </div>
              </div>

              {/* Quantity selector */}
              <label>
                Quantity (kg)
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <button
                    type="button"
                    style={{ width: '36px', height: '36px', border: '1px solid #ccc', borderRadius: '4px', background: '#f5f5f5', cursor: 'pointer' }}
                    onClick={() => setOrderQuantity(Math.max(1, orderQuantity - 1))}
                  >
                    −
                  </button>
                  <input
                    type="number"
                    min="1"
                    max={orderModalProduct.quantity}
                    value={orderQuantity}
                    onChange={(e) => setOrderQuantity(Math.max(1, Math.min(orderModalProduct.quantity, Number(e.target.value) || 1)))}
                    style={{ textAlign: 'center', fontWeight: 'bold' }}
                  />
                  <button
                    type="button"
                    style={{ width: '36px', height: '36px', border: '1px solid #ccc', borderRadius: '4px', background: '#f5f5f5', cursor: 'pointer' }}
                    onClick={() => setOrderQuantity(Math.min(orderModalProduct.quantity, orderQuantity + 1))}
                  >
                    +
                  </button>
                </div>
              </label>

              {/* Destination City */}
              <label style={{ marginTop: '12px' }}>
                Delivery Destination City (Krishna District)
                <select
                  value={orderDestination}
                  onChange={(e) => setOrderDestination(e.target.value)}
                >
                  {cities.map((city) => (
                    <option key={city} value={city}>
                      {city}
                    </option>
                  ))}
                </select>
              </label>

              {/* Price Calculation Box */}
              <div
                style={{
                  margin: '16px 0',
                  padding: '12px',
                  background: '#fcf8f6',
                  borderRadius: '6px',
                  border: '1px solid #ebd9cf',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
              >
                <div>
                  <small className="muted">Total Order Amount:</small>
                  <div style={{ fontSize: '18px', fontWeight: '700', color: '#d77c5a' }}>
                    ₹{((orderModalProduct.price || 40) * orderQuantity).toLocaleString()}
                  </div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <small className="muted">Estimated Handoff:</small>
                  <div style={{ fontSize: '11px', color: '#555' }}>Within 24 Hours</div>
                </div>
              </div>

              <button
                type="submit"
                className="button button-dark"
                style={{ width: '100%', height: '40px', background: '#d77c5a' }}
                disabled={ordering}
              >
                <Check size={16} /> {ordering ? 'Placing Order...' : 'Confirm & Place Order'}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
