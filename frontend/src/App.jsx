import { useEffect, useState } from 'react';
import { Link, NavLink, Route, Routes, useNavigate, useParams, useSearchParams, Navigate } from 'react-router-dom';
import { Activity, ArrowDownRight, ArrowLeft, ArrowRight, ArrowUpRight, Bell, Boxes, Check, ChevronDown, ChevronRight, CircleHelp, Clock3, Command, CookingPot, Cpu, Database, ExternalLink, Filter, Flower2, GitBranch, Grid2X2, Leaf, LogIn, LogOut, MapPin, Menu, Package, PackageCheck, Plus, Search, Share2, ShieldCheck, ShoppingBasket, Sparkles, Sprout, Truck, User, UserCheck, Warehouse, X } from 'lucide-react';
import { Bar, BarChart, CartesianGrid, Cell, Line, LineChart, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { algorithmGroups, cities } from './data/mockData.js';
import { addProduct, getAnalytics, getOrderById, getOrders, getProducts, placeOrder, updateStock, getDashboardSummary, getTracking, searchProducts, executeCO1, executeCO2, executeCO3, executeCO4, getCustomerOrders, getFarmerOrders } from './services/api.js';
import { useAuth } from './context/AuthContext.jsx';
import LoginPage from './components/LoginPage.jsx';
import CustomerDashboard from './components/CustomerDashboard.jsx';
import UserFarmerDashboard from './components/UserFarmerDashboard.jsx';

function getNavItems(user) {
  if (user?.role === 'customer') {
    return [
      { to: '/customer', label: 'Customer store', icon: ShoppingBasket },
      { to: '/products', label: 'All produce', icon: Boxes },
      { to: '/orders', label: 'My orders', icon: PackageCheck },
    ];
  }
  if (user?.role === 'farmer') {
    return [
      { to: '/user', label: 'Farmer hub', icon: Sprout },
      { to: '/dashboard', label: 'Overview', icon: Grid2X2 },
      { to: '/products', label: 'Marketplace', icon: Boxes },
      { to: '/orders', label: 'Farmer orders', icon: PackageCheck },
    ];
  }
  return [
    { to: '/login', label: 'Sign in / Register', icon: LogIn },
    { to: '/products', label: 'Marketplace', icon: Boxes },
  ];
}

function useProducts() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => { getProducts().then(setProducts).finally(() => setLoading(false)); }, []);
  return { products, loading, setProducts };
}

function Shell({ children }) {
  const [menuOpen, setMenuOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const items = getNavItems(user);

  const getInitials = (name) => {
    if (!name) return 'FC';
    return name.split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase();
  };

  return <div className="app-shell">
    <aside className={`sidebar ${menuOpen ? 'sidebar-open' : ''}`}>
      <Link to="/" className="brand">
        <span className="brand-mark"><Sprout size={21} strokeWidth={2.2} /></span>
        <span>foodchain<span className="brand-period">.</span><small>SUPPLY, MADE SMARTER</small></span>
      </Link>
      <div className="workspace-label">
        {user?.role === 'farmer' ? 'FARMER WORKSPACE' : user?.role === 'customer' ? 'CUSTOMER WORKSPACE' : 'FOODCHAIN PORTAL'}
      </div>
      {user?.role === 'farmer' ? (
        <div className="workspace-switch" style={{ cursor: 'default' }}>
          <span className="workspace-avatar" style={{ background: '#557e3c' }}>F</span>
          <span>
            <b>Farmer Portal</b>
            <small>{user.name} · {user.farmerCode || 'Producer'}</small>
          </span>
        </div>
      ) : user?.role === 'customer' ? (
        <div className="workspace-switch" style={{ cursor: 'default' }}>
          <span className="workspace-avatar" style={{ background: '#d77c5a' }}>C</span>
          <span>
            <b>Customer Portal</b>
            <small>{user.name} · {user.customerCode || 'Consumer'}</small>
          </span>
        </div>
      ) : (
        <Link to="/login" className="workspace-switch" style={{ textDecoration: 'none', cursor: 'pointer' }}>
          <span className="workspace-avatar" style={{ background: '#4b5563' }}><LogIn size={15} /></span>
          <span>
            <b>Portal Access</b>
            <small>Sign In or Register →</small>
          </span>
        </Link>
      )}
      <div className="nav-caption">OPERATIONS</div>
      <nav className="side-nav">
        {items.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            onClick={() => setMenuOpen(false)}
            className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
          >
            <Icon size={17} strokeWidth={1.8} />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>
      <div className="nav-caption lab-caption">INTELLIGENCE</div>
      <nav className="side-nav">
        <NavLink
          to="/analytics"
          onClick={() => setMenuOpen(false)}
          className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
        >
          <Activity size={17} strokeWidth={1.8} />
          <span>Analytics</span>
        </NavLink>
        <NavLink
          to="/algorithms"
          onClick={() => setMenuOpen(false)}
          className={({ isActive }) => `nav-link ${isActive || location.pathname.startsWith('/algorithms') ? 'active' : ''}`}
        >
          <Cpu size={17} strokeWidth={1.8} />
          <span>Algorithm lab</span>
          <span className="live-dot" />
        </NavLink>
      </nav>
      <div className="sidebar-flow">
        <span className="flow-orbit"><GitBranch size={17} /></span>
        <p>Good food.<br />Smarter routes.</p>
        <small>DSA in every delivery.</small>
        <Link to="/algorithms" className="flow-link">Explore the lab <ArrowRight size={13} /></Link>
      </div>
      <div className="profile-row">
        <div className="profile-avatar" style={{ background: user?.role === 'farmer' ? '#557e3c' : '#d77c5a' }}>
          {getInitials(user?.name)}
        </div>
        <div style={{ flex: 1, minWidth: 0, overflow: 'hidden' }}>
          <b style={{ display: 'block', fontSize: '13px', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
            {user ? user.name : 'Guest User'}
          </b>
          <small style={{ display: 'block', textTransform: 'capitalize' }}>
            {user ? `${user.role} · ${user.city || 'Krishna'}` : 'Not logged in'}
          </small>
        </div>
        {user ? (
          <button
            type="button"
            onClick={logout}
            title="Log out"
            style={{ background: 'transparent', border: 'none', cursor: 'pointer', color: '#889080', padding: '4px' }}
          >
            <LogOut size={15} />
          </button>
        ) : (
          <Link to="/login" title="Log in" style={{ color: '#557e3c' }}>
            <LogIn size={15} />
          </Link>
        )}
      </div>
    </aside>
    <div className="main-column">
      <header className="topbar">
        <button className="icon-button mobile-menu" aria-label="Open navigation" onClick={() => setMenuOpen(!menuOpen)}>
          {menuOpen ? <X size={19} /> : <Menu size={19} />}
        </button>
        <div className="breadcrumbs">
          <span>Krishna network</span>
          <ChevronRight size={14} />
          <b>{currentSection()}</b>
        </div>
        <div className="top-actions">
          <span className="system-status"><i /> Systems operational</span>
          {user ? (
            <span className={`role-badge-pill ${user.role === 'farmer' ? 'role-badge-farmer' : 'role-badge-customer'}`}>
              {user.role === 'farmer' ? <Sprout size={12} /> : <ShoppingBasket size={12} />}
              {user.role}: {user.name}
            </span>
          ) : null}
          <Link to="/login" className="button button-outline" style={{ height: '32px', fontSize: '11px', padding: '0 10px' }}>
            {user ? 'Switch Login' : 'Sign In'}
          </Link>
          <button className="icon-button" aria-label="Search" onClick={() => { navigate('/products'); }}>
            <Search size={18} />
          </button>
          <button className="icon-button notice-button" aria-label="Notifications">
            <Bell size={18} /><i />
          </button>
          <div className="top-avatar" style={{ background: user?.role === 'farmer' ? '#557e3c' : '#d77c5a' }}>
            {getInitials(user?.name)}
          </div>
        </div>
      </header>
      <main className="page-content">{children}</main>
      <footer className="footer">
        <span>© 2026 FoodChain</span>
        <span><span className="footer-dot" /> Data Structures · Real-world movement</span>
        <a href="#top">Back to top ↑</a>
      </footer>
    </div>
  </div>;
}

function currentSection() {
  const path = window.location.pathname;
  if (path.includes('/login')) return 'Login';
  if (path.includes('/algorithms')) return 'Algorithm lab';
  if (path.includes('/user') || path.includes('/farmer')) return 'User (Farmer) hub';
  if (path.includes('/customer')) return 'Customer store';
  if (path.includes('/products')) return 'Marketplace';
  if (path.includes('/orders')) return 'Orders';
  if (path.includes('/analytics')) return 'Analytics';
  if (path.includes('/dashboard')) return 'Overview';
  return 'Home';
}

function PageTitle({ eyebrow, title, description, action }) {
  return <div className="page-title-row"><div><div className="eyebrow"><span />{eyebrow}</div><h1>{title}</h1>{description && <p>{description}</p>}</div>{action && <div className="title-action">{action}</div>}</div>;
}

function StatCard({ icon: Icon, label, value, change, accent = '' }) {
  return <div className={`stat-card ${accent}`}><div className="stat-top"><span className="stat-icon"><Icon size={17} /></span><span className="stat-change"><ArrowUpRight size={13} />{change}</span></div><div className="stat-value">{value}</div><div className="stat-label">{label}</div></div>;
}

function Home() {
  return <div className="home-page">
    <div className="home-topline"><Link to="/dashboard" className="eyebrow-link"><span /> FOODCHAIN NETWORK <ArrowRight size={14} /></Link><span className="home-date"><span className="status-led" /> KRISHNA DISTRICT · LIVE DEMO</span></div>
    <section className="hero"><div className="hero-copy"><div className="hero-kicker"><Sprout size={15} /> FROM THE FIELD TO YOUR TABLE</div><h1>Food moves.<br /><em>Intelligence</em> moves it better.</h1><p>A connected supply network for Krishna’s farmers, fresh produce and the people who need it. Powered by data structures built to do real work.</p><div className="hero-actions"><Link to="/customer" className="button button-dark"><ShoppingBasket size={16} /> Customer store <ArrowRight size={16} /></Link><Link to="/user" className="button button-outline"><Sprout size={16} /> User (Farmer) portal</Link></div><div className="hero-footnote"><span className="avatar-stack"><i>LK</i><i>RB</i><i>MR</i></span><span>Built around local growers <b>·</b> Designed for short routes</span></div></div>
      <div className="hero-visual"><div className="visual-top"><span>THE KRISHNA NETWORK</span><span><i /> ROUTE ACTIVE</span></div><div className="landscape"><div className="sun-disc" /><div className="hill hill-back" /><div className="hill hill-front" /><div className="field-lines"><i /><i /><i /><i /><i /></div><div className="route-line"><span className="route-node node-farmer"><Sprout size={15} /></span><span className="route-node node-store"><Warehouse size={15} /></span><span className="route-node node-market"><ShoppingBasket size={15} /></span><span className="route-dash dash-one" /><span className="route-dash dash-two" /></div><div className="route-caption cap-a">FARM <b>Vijayawada</b></div><div className="route-caption cap-b">DISTRIBUTION <b>Gudivada hub</b></div><div className="route-caption cap-c">MARKET <b>Machilipatnam</b></div><div className="route-card"><span className="route-card-icon"><Truck size={16} /></span><span><b>Fresh route, found</b><small>12.4 km · optimized path</small></span><span className="route-card-live"><i /> LIVE</span></div></div><div className="visual-bottom"><span><span className="mini-ring">01</span> FIELD</span><ArrowRight size={15} /><span><span className="mini-ring">02</span> WAREHOUSE</span><ArrowRight size={15} /><span><span className="mini-ring">03</span> CUSTOMER</span></div></div></section>
    <section className="home-metrics"><div><b>08</b><span>local food categories</span></div><div><b>05</b><span>Krishna Zone cities</span></div><div><b>04</b><span>algorithm course outcomes</span></div><div className="metrics-note"><span>SHORTER ROUTES,</span><br /><b>fresher food.</b></div></section>
    <section className="home-bottom"><div className="home-section-heading"><div><span className="eyebrow">THE PLATFORM</span><h2>One network. Every handoff.</h2></div><Link to="/dashboard" className="text-link">Open operations <ArrowRight size={15} /></Link></div><div className="feature-grid"><Link to="/products" className="feature-item"><span className="feature-icon sage"><Search size={18} /></span><span className="feature-index">01 / DISCOVERY</span><h3>Search that understands.</h3><p>Find the right produce across a live regional inventory.</p><ArrowUpRight className="feature-arrow" size={17} /></Link><Link to="/farmer" className="feature-item"><span className="feature-icon gold"><Boxes size={18} /></span><span className="feature-index">02 / INVENTORY</span><h3>Stock, without the guesswork.</h3><p>Farmers add produce. Repeat listings update in place.</p><ArrowUpRight className="feature-arrow" size={17} /></Link><Link to="/analytics" className="feature-item"><span className="feature-icon coral"><GitBranch size={18} /></span><span className="feature-index">03 / DISTRIBUTION</span><h3>Routes with a reason.</h3><p>See how flow, matching and optimization shape delivery.</p><ArrowUpRight className="feature-arrow" size={17} /></Link></div></section>
    <section className="algorithm-ribbon"><div><span className="eyebrow">THE ENGINE ROOM <span /></span><h2>Algorithms, out in the open.</h2><p>See how classic DSA turns a food network into a smarter one.</p></div><div className="ribbon-pipeline"><span>Search</span><b>→</b><span>Match</span><b>→</b><span>Optimize</span><b>→</b><span>Deliver</span></div><Link to="/algorithms" className="button button-light">Enter algorithm lab <ArrowRight size={15} /></Link></section>
  </div>;
}

function Dashboard() {
  const { products, loading } = useProducts();
  const [orders, setOrders] = useState([]);
  const [summary, setSummary] = useState(null);
  useEffect(() => {
    getOrders().then(setOrders).catch(() => {});
    getDashboardSummary().then(setSummary).catch(() => {});
  }, []);
  const listedCount = summary ? summary.totalProducts : products.length;
  const stock = summary ? summary.availableStock : products.reduce((sum, item) => sum + item.quantity, 0);
  const activeOrders = summary ? summary.activeOrders : orders.filter((item) => item.status !== 'Delivered').length;
  const deliveredOrders = summary ? summary.deliveredOrders : orders.filter((item) => item.status === 'Delivered').length;
  return <><PageTitle eyebrow="FOOD SUPPLY &amp; DISTRIBUTION PLATFORM" title="The network at a glance." description="A live look at the food moving through Krishna district." action={<Link to="/farmer" className="button button-dark"><Plus size={16} /> Add produce</Link>} />
    <div className="stats-grid"><StatCard icon={Boxes} label="Listed products" value={loading ? '—' : String(listedCount).padStart(2, '0')} change="Active" /><StatCard icon={Package} label="Available stock" value={loading ? '—' : `${stock.toLocaleString()} kg`} change="In stock" accent="stat-green" /><StatCard icon={Truck} label="Active orders" value={String(activeOrders).padStart(2, '0')} change="Live" accent="stat-orange" /><StatCard icon={PackageCheck} label="Delivered orders" value={String(deliveredOrders).padStart(2, '0')} change="Fulfilled" accent="stat-cream" /></div>
    <div className="dashboard-grid"><section className="panel inventory-panel"><div className="panel-heading"><div><span className="eyebrow">RECENT ACTIVITY</span><h2>Moving through the network</h2></div><Link to="/orders" className="text-link">All orders <ArrowRight size={14} /></Link></div><OrderTable orders={orders.slice(0, 4)} /></section><section className="panel pulse-panel"><div className="panel-heading"><div><span className="eyebrow">NETWORK PULSE</span><h2>Today, so far</h2></div><span className="live-pill"><i /> LIVE</span></div><div className="pulse-score">86<span>/100</span><span className="pulse-up"><ArrowUpRight size={14} /> 4.2%</span></div><p className="muted">Regional network health</p><div className="pulse-chart"><ResponsiveContainer width="100%" height="100%"><LineChart data={[{ v: 28 }, { v: 38 }, { v: 34 }, { v: 53 }, { v: 46 }, { v: 66 }, { v: 60 }, { v: 76 }, { v: 69 }, { v: 87 }]}><Line type="monotone" dataKey="v" stroke="#557e3c" strokeWidth={2.5} dot={false} /></LineChart></ResponsiveContainer></div><div className="pulse-foot"><span><i className="legend-green" />Network activity</span><span>LAST 24 HOURS</span></div></section></div>
    <section className="panel quick-panel"><div className="panel-heading"><div><span className="eyebrow">GO STRAIGHT TO IT</span><h2>Your everyday tools</h2></div></div><div className="quick-links"><Link to="/products"><ShoppingBasket size={19} /><span><b>Browse produce</b><small>Find available food nearby</small></span><ArrowRight size={15} /></Link><Link to="/farmer"><Sprout size={19} /><span><b>Manage inventory</b><small>Add harvest or update stock</small></span><ArrowRight size={15} /></Link><Link to="/algorithms"><Cpu size={19} /><span><b>Explore DSA</b><small>Open the algorithm lab</small></span><ArrowRight size={15} /></Link></div></section>
  </>;
}

function Products() {
  const { products, loading } = useProducts();
  const [query, setQuery] = useState('');
  const filtered = products.filter((item) => item.product.toLowerCase().includes(query.trim().toLowerCase()));
  return <><PageTitle eyebrow="PRODUCE, CLOSER TO HOME" title="The marketplace." description="Fresh inventory from farmers across the Krishna Zone." action={<span className="catalog-count">{filtered.length} LISTINGS <span /> UPDATED JUST NOW</span>} /><div className="catalog-toolbar"><label className="search-field"><Search size={18} /><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search by product name..." /><kbd>⌘ K</kbd></label><button className="filter-button"><Filter size={16} /> All produce <ChevronDown size={14} /></button><span className="toolbar-zone"><MapPin size={14} /> Krishna Zone</span></div>
    {loading ? <div className="loading-state"><span className="spinner" /> Loading regional inventory…</div> : filtered.length ? <div className="product-grid">{filtered.map((item) => <ProductCard key={item.id} product={item} />)}</div> : <div className="empty-state"><Search size={22} /><h3>No produce found</h3><p>Try another product name.</p></div>}
  </>;
}

function ProductCard({ product }) {
  return <article className="product-card"><Link to={`/products/${product.id}`} className="product-image-wrap"><img src={product.image} alt={product.product} /><span className="product-category">{product.category}</span><span className="image-arrow"><ArrowUpRight size={16} /></span></Link><div className="product-card-body"><div className="product-meta"><span><span className="stock-dot" /> {product.quantity} kg available</span><span className="product-zone">KRISHNA</span></div><Link to={`/products/${product.id}`} className="product-name">{product.product}</Link><div className="product-farmer"><span className="farmer-mini">{product.farmer.split(' ').map((part) => part[0]).join('')}</span><span>{product.farmer}</span><span className="farmer-separator">·</span><span>{product.warehouse.replace(' Warehouse', '')}</span></div><div className="product-card-bottom"><span className="product-price">{product.price ? `₹${product.price}` : 'Price on request'}<small>{product.price ? '/ kg' : ''}</small></span><Link to={`/products/${product.id}`} className="small-order-button">View details <ArrowRight size={14} /></Link></div></div></article>;
}

function ProductDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const { products, loading } = useProducts();
  const [quantity, setQuantity] = useState(1);
  const navigate = useNavigate();
  const product = products.find((item) => item.id === id);
  if (loading) return <div className="loading-state"><span className="spinner" /> Loading product…</div>;
  if (!product) return <div className="empty-state"><Package size={22} /><h3>Product not found</h3><Link className="text-link" to="/products">Return to marketplace <ArrowRight size={14} /></Link></div>;
  return <><Link to="/products" className="back-link"><ArrowLeft size={15} /> Marketplace <span>/</span> {product.category}</Link><div className="detail-layout"><div className="detail-image"><img src={product.image} alt={product.product} /><span className="detail-image-label"><span className="stock-dot" /> HARVEST AVAILABLE</span></div><div className="detail-copy"><div className="eyebrow"><span /> KRISHNA ZONE · {product.category.toUpperCase()}</div><h1>{product.product}</h1><p className="detail-description">{product.description}</p><div className="detail-farmer"><div className="farmer-avatar">{product.farmer.split(' ').map((part) => part[0]).join('')}</div><span>GROWN BY<b>{product.farmer}</b></span><span className="farmer-verified"><ShieldCheck size={14} /> VERIFIED</span></div><div className="detail-facts"><div><span>AVAILABLE</span><b>{product.quantity} <small>kg</small></b></div><div><span>AT WAREHOUSE</span><b>{product.warehouse}</b></div><div><span>PRODUCT CODE</span><b className="mono">#{product.productCode}</b></div></div><div className="order-inline"><div className="quantity-control"><button aria-label="Decrease quantity" onClick={() => setQuantity(Math.max(1, quantity - 1))}>−</button><span>{quantity}</span><button aria-label="Increase quantity" onClick={() => setQuantity(Math.min(product.quantity, quantity + 1))}>+</button></div><span className="quantity-label">kg selected</span><button className="button button-dark" disabled={product.quantity < 1} onClick={() => {
    if (!user || user.role !== 'customer') {
      navigate('/login');
    } else {
      navigate(`/orders/new?product=${product.id}&quantity=${quantity}`);
    }
  }}>Place an order <ArrowRight size={16} /></button></div><div className="availability-note"><span className="stock-dot" /> In stock · typically dispatched within 24 hours</div></div></div></>;
}

function Farmer() {
  const { user } = useAuth();
  const { products, loading, setProducts } = useProducts();
  const defaultFarmerName = user?.role === 'farmer' ? user.name : 'Lakshmi Devi';
  const defaultCity = user?.city || 'Vijayawada';
  const [form, setForm] = useState({ farmer: defaultFarmerName, product: '', quantity: '', city: defaultCity, description: '' });
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');
  const [stockTarget, setStockTarget] = useState(null);
  const [stockAmount, setStockAmount] = useState('');

  useEffect(() => {
    if (user?.role === 'farmer') {
      setForm((prev) => ({ ...prev, farmer: user.name, city: user.city || prev.city }));
    }
  }, [user]);

  const farmerProducts = products.filter((item) => !form.farmer || item.farmer.toLowerCase() === form.farmer.toLowerCase());
  const total = farmerProducts.reduce((sum, item) => sum + item.quantity, 0);

  async function submit(event) {
    event.preventDefault(); setError(''); setNotice('');
    if (!form.farmer.trim() || !form.product.trim() || !form.description.trim() || !form.quantity || Number(form.quantity) < 1) {
      setError('Complete every field and enter a quantity greater than zero.');
      return;
    }
    try {
      const added = await addProduct(form);
      setProducts(await getProducts());
      setNotice(`${added.merged ? 'Stock updated for existing listing' : 'Product added successfully'} · Product code ${added.productCode}`);
      setForm({ ...form, product: '', quantity: '', description: '' });
    } catch (cause) {
      setError(cause.message);
    }
  }

  async function addStock(event) {
    event.preventDefault();
    if (!stockAmount || Number(stockAmount) < 1) return;
    await updateStock(stockTarget, stockAmount);
    setProducts(await getProducts());
    setNotice('Stock updated successfully.');
    setStockTarget(null);
    setStockAmount('');
  }

  return <><PageTitle eyebrow="GROWER OPERATIONS · KRISHNA ZONE" title={`Welcome, Farmer ${user?.role === 'farmer' ? user.name : 'Grower'}`} description="Keep your listings fresh. Your next customer is already looking." action={<span className="farmer-status"><span className="stock-dot" /> GROWER PORTAL</span>} />
    <div className="stats-grid farmer-stats"><StatCard icon={Boxes} label="My listings" value={loading ? '—' : farmerProducts.length.toString().padStart(2, '0')} change="Current" /><StatCard icon={Warehouse} label="Total quantity" value={loading ? '—' : `${total.toLocaleString()} kg`} change="In stock" accent="stat-green" /><StatCard icon={Clock3} label="Pending orders" value="04" change="2 new" accent="stat-orange" /><StatCard icon={PackageCheck} label="Delivered this week" value="12" change="On track" accent="stat-cream" /></div>
    <div className="farmer-layout"><section className="panel form-panel"><div className="panel-heading"><div><span className="eyebrow">ADD TO THE NETWORK</span><h2>List your produce</h2></div><span className="form-step">01 <span>/</span> 02</span></div><p className="form-intro">A good listing gets good food moving. Add the details below.</p><form onSubmit={submit} className="farmer-form"><label>Farmer name<input value={form.farmer} onChange={(event) => setForm({ ...form, farmer: event.target.value })} placeholder="e.g. Lakshmi Devi" /></label><label>Product name<input value={form.product} onChange={(event) => setForm({ ...form, product: event.target.value })} placeholder="e.g. Organic Tomatoes" /></label><div className="form-row"><label>Available quantity<div className="input-unit"><input type="number" min="1" value={form.quantity} onChange={(event) => setForm({ ...form, quantity: event.target.value })} placeholder="160" /><span>KG</span></div></label><label>Nearest city<select value={form.city} onChange={(event) => setForm({ ...form, city: event.target.value })}>{cities.map((city) => <option key={city}>{city}</option>)}</select></label></div><label>About this harvest<textarea rows="3" value={form.description} onChange={(event) => setForm({ ...form, description: event.target.value })} placeholder="How was it grown? What makes this harvest special?" /></label><div className="code-note"><Command size={15} /><span>Product code generated automatically<small>FoodChain assigns a unique code when you publish.</small></span><Sparkles size={14} /></div>{error && <div className="form-error">{error}</div>}{notice && <div className="form-success"><Check size={15} />{notice}</div>}<button className="button button-dark form-submit" type="submit"><Plus size={16} /> Publish listing <ArrowRight size={15} /></button></form></section>
      <section className="panel farmer-inventory"><div className="panel-heading"><div><span className="eyebrow">YOUR PRODUCE</span><h2>Active listings <span className="heading-count">{farmerProducts.length}</span></h2></div><button className="table-more" aria-label="Inventory options"><ExternalLink size={16} /></button></div><div className="table-scroll"><table className="data-table"><thead><tr><th>PRODUCT</th><th>CODE</th><th>QUANTITY</th><th>WAREHOUSE</th><th>STATUS</th><th /></tr></thead><tbody>{farmerProducts.map((item) => <tr key={item.id}><td><span className="table-product">{item.product}</span><small className="table-category">{item.category}</small></td><td className="mono muted">{item.productCode}</td><td><b>{item.quantity} kg</b></td><td>{item.warehouse}</td><td><span className="status-badge"><i />Available</span></td><td><button className="add-stock" onClick={() => setStockTarget(item.id)} title="Add stock"><Plus size={15} /></button></td></tr>)}</tbody></table>{!farmerProducts.length && <div className="table-empty">No listings yet. Publish your first harvest.</div>}</div><div className="table-foot"><span><span className="stock-dot" /> All listings visible to the network</span><Link to="/products">Browse marketplace <ArrowRight size={13} /></Link></div></section></div>
    {stockTarget && <div className="modal-backdrop" onClick={() => setStockTarget(null)}><div className="modal" onClick={(event) => event.stopPropagation()}><button className="modal-close" onClick={() => setStockTarget(null)}><X size={17} /></button><span className="eyebrow">INVENTORY UPDATE</span><h2>Add to your stock</h2><p>Increase the available quantity for this listing.</p><form onSubmit={addStock}><label>Additional quantity (kg)<input autoFocus type="number" min="1" value={stockAmount} onChange={(event) => setStockAmount(event.target.value)} placeholder="e.g. 25" required /></label><button className="button button-dark" type="submit"><Plus size={15} /> Update stock</button></form></div></div>}
  </>;
}

function OrderTable({ orders }) {
  return <div className="table-scroll"><table className="data-table order-table"><thead><tr><th>ORDER</th><th>PRODUCT</th><th>DESTINATION</th><th>STATUS</th><th>PLACED</th></tr></thead><tbody>{orders.map((item) => <tr key={item.id}><td><Link to={`/orders/${item.id}`} className="order-id">{item.id}</Link></td><td><span className="table-product">{item.product}</span><small className="table-category">{item.quantity} kg · {item.farmer}</small></td><td><span className="city-cell"><MapPin size={13} />{item.destination}</span></td><td><span className={`order-status status-${item.status.toLowerCase().replaceAll(' ', '-')}`}><i />{item.status}</span></td><td className="muted">{item.createdAt}</td></tr>)}</tbody></table>{!orders.length && <div className="table-empty">No orders yet. They’ll appear here.</div>}</div>;
}

function Orders() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    if (user?.role === 'customer' && user.id) {
      getCustomerOrders(user.id)
        .then(setOrders)
        .catch(() => setOrders([]))
        .finally(() => setLoading(false));
    } else if (user?.role === 'farmer' && user.name) {
      getFarmerOrders(user.name)
        .then(setOrders)
        .catch(() => setOrders([]))
        .finally(() => setLoading(false));
    } else {
      setOrders([]);
      setLoading(false);
    }
  }, [user]);

  const eyebrow = user?.role === 'customer'
    ? 'MY DELIVERIES · CUSTOMER'
    : user?.role === 'farmer'
    ? 'PRODUCE ORDERS · FARMER'
    : 'ACCOUNT ORDERS';

  const title = user?.role === 'customer'
    ? 'My private orders.'
    : user?.role === 'farmer'
    ? 'Produce orders for your farm.'
    : 'Order registry.';

  const description = user?.role === 'customer'
    ? 'Track deliveries from local farms directly to your doorstep in Krishna.'
    : user?.role === 'farmer'
    ? 'Fulfill orders placed by buyers for your fresh crop listings.'
    : 'Please sign in or register with your credentials (stored in pgAdmin) to view orders.';

  return (
    <>
      <PageTitle eyebrow={eyebrow} title={title} description={description} action={<span className="catalog-count">{orders.length.toString().padStart(2, '0')} ORDERS</span>} />
      {!user && (
        <div style={{ background: '#f5f7f3', border: '1px solid #dce2d7', borderRadius: '10px', padding: '16px 20px', marginBottom: '20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '15px', flexWrap: 'wrap' }}>
          <div>
            <b style={{ color: '#27441d', display: 'block', fontSize: '14px' }}>Sign-in required to view orders</b>
            <span style={{ color: '#556050', fontSize: '13px' }}>Each customer and farmer has private, isolated order records stored in PostgreSQL.</span>
          </div>
          <Link to="/login" className="button button-dark" style={{ height: '34px', fontSize: '12px' }}>Sign In / Register</Link>
        </div>
      )}
      <section className="panel orders-list-panel">
        <div className="panel-heading">
          <div><span className="eyebrow">ORDER REGISTER</span><h2>{user?.role === 'customer' ? 'My Order History' : user?.role === 'farmer' ? 'Incoming Customer Orders' : 'Orders'}</h2></div>
          <span className="live-pill"><i /> {user ? 'ISOLATED TO YOU' : 'NO ACTIVE SESSION'}</span>
        </div>
        {loading ? <div className="loading-state"><span className="spinner" /> Loading your orders…</div> : <OrderTable orders={orders} />}
      </section>
      <section className="delivery-strip">
        <span className="delivery-icon"><Truck size={20} /></span>
        <div><b>Every journey has a trace.</b><span>Open an order to see its route and delivery timeline.</span></div>
        <ArrowRight size={17} />
      </section>
    </>
  );
}

function NewOrder() {
  const [params] = useSearchParams();
  const productId = params.get('product');
  const quantity = Number(params.get('quantity')) || 1;
  const navigate = useNavigate();
  const { user } = useAuth();
  const { products, loading } = useProducts();
  const [destination, setDestination] = useState('Vijayawada');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const product = products.find((item) => item.id === productId);

  async function submit(event) {
    event.preventDefault();
    setError('');

    if (!user || user.role !== 'customer' || !user.id) {
      setError('You must be signed in with a registered Customer account to place an order.');
      return;
    }

    setSubmitting(true);
    try {
      const order = await placeOrder({
        productId,
        quantity,
        destination,
        customerId: user.id,
      });
      navigate(`/orders/${order.id}`, { state: { confirmation: true } });
    } catch (cause) {
      setError(cause.message);
      setSubmitting(false);
    }
  }
  if (loading) return <div className="loading-state"><span className="spinner" /> Loading order…</div>;
  if (!product) return <div className="empty-state"><Package size={22} /><h3>Choose a product first</h3><Link className="text-link" to="/products">Browse produce <ArrowRight size={14} /></Link></div>;
  return <><Link to={`/products/${product.id}`} className="back-link"><ArrowLeft size={15} /> Back to product</Link><div className="checkout-layout"><section className="checkout-main"><PageTitle eyebrow="CHECKOUT · ONE SHORTER ROUTE" title="Let’s get it moving." description="Confirm where this harvest should go." /><form className="checkout-form" onSubmit={submit}><div className="checkout-section"><span className="checkout-number">01</span><div><h3>Delivery destination</h3><p>Choose a city in the Krishna Zone.</p></div></div><div className="city-select-grid">{cities.map((city) => <button type="button" key={city} className={`city-choice ${destination === city ? 'selected' : ''}`} onClick={() => setDestination(city)}><MapPin size={16} /><span>{city}</span>{destination === city && <Check size={15} />}</button>)}</div>{error && <div className="form-error">{error}</div>}<div className="checkout-bottom"><span><ShieldCheck size={16} /> Stock held only when you confirm</span><button className="button button-dark" type="submit" disabled={submitting}>{submitting ? 'Confirming…' : 'Confirm order'} <ArrowRight size={16} /></button></div></form></section><aside className="order-summary"><span className="eyebrow">YOUR ORDER</span><h2>From the field</h2><div className="summary-product"><img src={product.image} alt="" /><span><b>{product.product}</b><small>Grown by {product.farmer}</small></span></div><div className="summary-line"><span>Quantity</span><b>{quantity} kg</b></div><div className="summary-line"><span>Pickup</span><b>{product.warehouse}</b></div><div className="summary-line"><span>To</span><b>{destination}</b></div><div className="summary-total"><span>Estimate</span><b>{product.price ? `₹${(product.price * quantity).toLocaleString()}` : 'Price on request'}</b></div><small className="summary-footnote">Delivery and final pricing confirmed by your local supplier.</small></aside></div></>;
}

const timelineSteps = ['Order Placed', 'Prepared', 'Dispatched', 'In Transit', 'Out for Delivery', 'Delivered'];
const stepIcons = [Check, CookingPot, Boxes, Truck, MapPin, PackageCheck];
function OrderTracking() {
  const { id } = useParams();
  const [order, setOrder] = useState(null);
  const [error, setError] = useState('');
  useEffect(() => { getTracking(id).then(setOrder).catch(() => getOrderById(id).then(setOrder)).catch((cause) => setError(cause.message)); }, [id]);
  if (error) return <div className="empty-state"><Package size={22} /><h3>{error}</h3><Link to="/orders" className="text-link">Back to orders <ArrowRight size={14} /></Link></div>;
  if (!order) return <div className="loading-state"><span className="spinner" /> Loading tracking…</div>;
  const activeStep = Math.max(0, timelineSteps.indexOf(order.status));
  const timelineData = order.timeline && order.timeline.length ? order.timeline : timelineSteps.map((step, index) => ({
    step,
    completed: index <= activeStep,
    current: index === activeStep,
    time: index <= activeStep ? 'Completed' : 'Pending',
    description: index === 0 ? 'Your order is in the system.' : 'Latest checkpoint'
  }));
  return <><Link to="/orders" className="back-link"><ArrowLeft size={15} /> All orders</Link><div className="tracking-heading"><div><div className="eyebrow"><span /> ORDER TRACKING</div><h1>Your food is on its way.</h1><p>Every step, from the farm to {order.destination}.</p></div><span className="tracking-id">{order.id}</span></div>{window.history.state?.usr?.confirmation && <div className="confirmation-banner"><span><Check size={19} /></span><div><b>Order placed successfully</b><small>Your confirmation is ready. Order ID: {order.id}</small></div></div>}<div className="tracking-layout"><section className="panel timeline-panel"><div className="panel-heading"><div><span className="eyebrow">DELIVERY JOURNEY</span><h2>On the move</h2></div><span className="order-status status-in-transit"><i />{order.status}</span></div><div className="timeline">{timelineData.map((item, index) => { const Icon = stepIcons[index] || Check; return <div key={item.step} className={`timeline-step ${item.completed ? 'complete' : ''} ${item.current ? 'current' : ''}`}><div className="timeline-marker"><Icon size={15} /></div><div className="timeline-step-label"><b>{item.step}</b><small>{item.time} · {item.description || 'Verified checkpoint'}</small></div>{index < timelineData.length - 1 && <div className="timeline-connector" />}</div>; })}</div><div className="tracking-note"><span className="tracking-note-icon"><MapPin size={17} /></span><span><b>Delivery route</b><small>{order.warehouse} → {order.destination}</small></span><span className="route-distance">KRISHNA ZONE</span></div></section><aside className="panel tracking-summary"><span className="eyebrow">ORDER DETAILS</span><h2>{order.product}</h2><div className="tracking-quantity">{order.quantity}<small> kg</small><span>ORDERED QUANTITY</span></div><dl><div><dt>Order reference</dt><dd>{order.id}</dd></div><div><dt>Farmer</dt><dd>{order.farmer}</dd></div><div><dt>Warehouse</dt><dd>{order.warehouse}</dd></div><div><dt>Destination</dt><dd>{order.destination}</dd></div><div><dt>Placed</dt><dd>{order.createdAt}</dd></div></dl></aside></div></>;
}

function Analytics() {
  const [data, setData] = useState(null);
  useEffect(() => { getAnalytics().then(setData); }, []);
  const colors = ['#567a3d', '#d7a449', '#d77c5a'];
  if (!data) return <div className="loading-state"><span className="spinner" /> Loading regional data…</div>;
  return <><PageTitle eyebrow="REGIONAL INTELLIGENCE · KRISHNA" title="See the whole harvest." description="Inventory, demand and delivery performance across the network." action={<span className="catalog-count"><span /> LAST 7 DAYS</span>} /><div className="analytics-kpis"><div><span>ORDERS THIS WEEK</span><b>207</b><small><ArrowUpRight size={13} /> 12.4% vs last week</small></div><div><span>AVG. FULFILLMENT</span><b>1.8 <small>days</small></b><small className="kpi-neutral">Across all destinations</small></div><div><span>WAREHOUSE UTILIZATION</span><b>74<small>%</small></b><small><ArrowDownRight size={13} /> 3.1% capacity remaining</small></div><div><span>ON-TIME DELIVERY</span><b>96<small>%</small></b><small><ArrowUpRight size={13} /> 2.8% vs last week</small></div></div><div className="charts-grid"><section className="panel chart-panel"><div className="panel-heading"><div><span className="eyebrow">DEMAND</span><h2>Orders over the week</h2></div><span className="chart-unit">ORDERS / DAY</span></div><div className="chart-area"><ResponsiveContainer width="100%" height="100%"><LineChart data={data.trend} margin={{ top: 12, right: 12, left: -18, bottom: 0 }}><CartesianGrid vertical={false} stroke="#e8e9e1" strokeDasharray="3 5" /><XAxis dataKey="day" axisLine={false} tickLine={false} tick={{ fill: '#879083', fontSize: 11 }} dy={10} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#879083', fontSize: 11 }} /><Tooltip contentStyle={{ border: '1px solid #e7e9e0', borderRadius: 8, fontSize: 12 }} /><Line type="monotone" dataKey="orders" stroke="#567a3d" strokeWidth={2.5} dot={{ r: 3, fill: '#567a3d', strokeWidth: 0 }} activeDot={{ r: 5 }} /></LineChart></ResponsiveContainer></div></section><section className="panel chart-panel"><div className="panel-heading"><div><span className="eyebrow">CITY BREAKDOWN</span><h2>Orders by destination</h2></div><span className="chart-unit">KRISHNA ZONE</span></div><div className="chart-area"><ResponsiveContainer width="100%" height="100%"><BarChart data={data.cities} margin={{ top: 10, right: 3, left: -21, bottom: 0 }}><CartesianGrid vertical={false} stroke="#e8e9e1" strokeDasharray="3 5" /><XAxis dataKey="name" axisLine={false} tickLine={false} tick={{ fill: '#879083', fontSize: 9 }} interval={0} dy={9} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#879083', fontSize: 10 }} /><Tooltip contentStyle={{ border: '1px solid #e7e9e0', borderRadius: 8, fontSize: 12 }} /><Bar dataKey="orders" fill="#d5a44a" radius={[4, 4, 0, 0]} barSize={25} /></BarChart></ResponsiveContainer></div></section><section className="panel chart-panel"><div className="panel-heading"><div><span className="eyebrow">FULFILLMENT</span><h2>Delivery status</h2></div></div><div className="donut-wrap"><ResponsiveContainer width="52%" height={175}><PieChart><Pie data={data.deliveries} dataKey="value" innerRadius={51} outerRadius={72} paddingAngle={3} stroke="none">{data.deliveries.map((entry, index) => <Cell key={entry.name} fill={colors[index]} />)}</Pie><Tooltip /></PieChart></ResponsiveContainer><div className="donut-center"><b>100</b><small>ORDERS</small></div><div className="donut-legend">{data.deliveries.map((entry, index) => <div key={entry.name}><i style={{ backgroundColor: colors[index] }} /><span>{entry.name}</span><b>{entry.value}%</b></div>)}</div></div></section><section className="panel chart-panel warehouse-chart"><div className="panel-heading"><div><span className="eyebrow">CAPACITY</span><h2>Warehouse utilization</h2></div></div><div className="warehouse-list">{[{ name: 'Vijayawada', value: 82 }, { name: 'Gudivada', value: 67 }, { name: 'Nuzvid', value: 74 }, { name: 'Machilipatnam', value: 58 }].map((item) => <div className="warehouse-row" key={item.name}><span>{item.name}</span><div><i style={{ width: `${item.value}%` }} /></div><b>{item.value}%</b></div>)}</div></section></div><div className="analytics-foot"><span><span className="live-dot" /> DATA REFRESHES AUTOMATICALLY</span><span>DEMO DATA · READY FOR API CONNECTION</span></div></>;
}

function Algorithms() {
  return <><PageTitle eyebrow="DSA · COURSE OUTCOMES 01—04" title="The logic behind the logistics." description="Four algorithm families. One connected food supply chain. Explore how each idea moves the network forward." action={<span className="lab-badge"><Cpu size={15} /> DSA LAB</span>} /><section className="algorithm-pipeline"><div className="pipeline-label"><span className="eyebrow">FROM SEARCH TO DELIVERY</span><span className="pipeline-caption">A practical algorithm stack</span></div><div className="pipeline-steps"><span><Search size={15} /> Discover</span><i>→</i><span><Database size={15} /> Match</span><i>→</i><span><Grid2X2 size={15} /> Optimize</span><i>→</i><span><Share2 size={15} /> Distribute</span></div></section><div className="algorithm-grid">{algorithmGroups.map((group, index) => <AlgorithmCard key={group.id} group={group} index={index} />)}</div><section className="lab-note"><span className="lab-note-icon"><CircleHelp size={18} /></span><div><b>Course concepts, made tangible.</b><p>Interactive examples use food-chain scenarios. Each result is a browser-side demonstration and can later be connected to your Java implementation.</p></div><span className="lab-note-tag">JAVA BACKEND READY</span></section></>;
}

function AlgorithmCard({ group, index }) {
  const icons = [Search, Database, Grid2X2, Share2];
  const Icon = icons[index];
  return <Link to={`/algorithms/${group.id}`} className={`algorithm-card algo-${group.id}`}><div className="algorithm-card-top"><span className="co-label">{group.label}</span><span className="algo-card-icon"><Icon size={19} /></span></div><div className="algorithm-card-body"><span className="eyebrow">{group.count}</span><h2>{group.title}</h2><p>{group.description}</p><div className="algorithm-tags">{group.algorithms.map((algorithm) => <span key={algorithm}>{algorithm}</span>)}</div></div><div className="algorithm-card-bottom"><span>Explore algorithms</span><span className="algo-arrow"><ArrowUpRight size={17} /></span></div></Link>;
}

function AlgorithmDetail() {
  const { code } = useParams();
  const group = algorithmGroups.find((item) => item.id === code);
  const [selected, setSelected] = useState('');
  const [text, setText] = useState('organic tomatoes are fresh tomatoes');
  const [pattern, setPattern] = useState('tomatoes');
  const [result, setResult] = useState(null);
  const [running, setRunning] = useState(false);
  const [algoError, setAlgoError] = useState('');
  useEffect(() => { setSelected(group?.algorithms[0] || ''); setResult(null); setAlgoError(''); }, [code]);
  if (!group) return <div className="empty-state"><Cpu size={22} /><h3>Algorithm group not found</h3><Link to="/algorithms" className="text-link">Back to the lab <ArrowRight size={14} /></Link></div>;
  async function run(event) {
    event.preventDefault();
    setRunning(true);
    setAlgoError('');
    try {
      if (code === 'co1') {
        const res = await executeCO1({ algorithm: selected, text, pattern });
        setResult({
          found: res.found,
          index: res.firstIndex >= 0 ? res.firstIndex : 0,
          length: res.matchLength || pattern.length,
          executionTime: res.executionTime,
          explanation: res.explanation,
        });
      } else if (code === 'co2') {
        const res = await executeCO2({ algorithm: selected, text, pattern });
        setResult({
          suffixes: (res.suffixes || []).map((s) => ({ index: s.index, suffix: s.suffix })),
          total: res.totalSuffixes || (res.suffixes ? res.suffixes.length : 0),
          executionTime: res.executionTime,
          explanation: res.explanation,
        });
      } else if (code === 'co3') {
        const res = await executeCO3({ algorithm: selected, text, pattern });
        setResult({
          distance: res.distance != null ? res.distance : res.result,
          rows: res.dpMatrix && res.dpMatrix.length ? res.dpMatrix.slice(0, 9).map((row) => row.slice(0, 12)) : [[res.result]],
          executionTime: res.executionTime,
          explanation: res.explanation,
        });
      } else {
        const res = await executeCO4({ algorithm: selected });
        setResult({
          maxFlow: res.maxFlow,
          path: res.path || ['Supplier', 'Warehouse A', 'Distribution center', 'Market'],
          capacity: res.capacitySummary || '100 / 70 / 100 units',
          executionTime: res.executionTime,
          explanation: res.explanation,
        });
      }
    } catch (err) {
      setAlgoError(err.message);
    } finally {
      setRunning(false);
    }
  }
  return <><Link to="/algorithms" className="back-link"><ArrowLeft size={15} /> Algorithm lab <span>/</span> {group.label}</Link><PageTitle eyebrow={`${group.label} · ALGORITHM EXPLORER`} title={group.title} description={group.description} action={<span className="lab-badge"><Cpu size={15} /> JAVA REST API</span>} /><div className="algorithm-detail-layout"><section className="panel algorithm-workspace"><div className="algorithm-tabs" role="tablist">{group.algorithms.map((algorithm) => <button role="tab" aria-selected={selected === algorithm} className={selected === algorithm ? 'selected' : ''} key={algorithm} onClick={() => { setSelected(algorithm); setResult(null); setAlgoError(''); }}>{algorithm}</button>)}</div><div className="workspace-content"><div className="workspace-heading"><div><span className="eyebrow">SPRING BOOT + JAVA DSA BACKEND</span><h2>{selected}</h2></div><span className="demo-index">{group.label} / {String(group.algorithms.indexOf(selected) + 1).padStart(2, '0')}</span></div><p className="workspace-explainer">{code === 'co1' ? 'Search text for a pattern using backend string matching algorithms.' : code === 'co2' ? 'Generate sorted suffixes and Kasai LCP values computed by Spring Boot Java 21.' : code === 'co3' ? 'Execute dynamic programming edit distance and optimization models on the backend.' : 'Run network flow algorithms across the Krishna food distribution graph.'}</p><form className="algorithm-form" onSubmit={run}>{code !== 'co4' && <label>{code === 'co2' ? 'Input text' : code === 'co3' ? 'Product name' : 'Text'}<textarea value={text} onChange={(event) => setText(event.target.value)} rows={code === 'co2' ? 2 : 2} required /></label>}{(code === 'co1' || code === 'co3') && <label>{code === 'co1' ? 'Pattern to find' : 'Compare with'}<input value={pattern} onChange={(event) => setPattern(event.target.value)} required /></label>}{code === 'co4' && <div className="flow-method-picker">{group.algorithms.slice(0, 3).map((method) => <button key={method} type="button" onClick={() => setSelected(method)} className={selected === method ? 'selected' : ''}>{method}</button>)}</div>}{algoError && <div className="form-error">{algoError}</div>}<button type="submit" className="button button-dark run-button" disabled={running}><Activity size={16} /> {running ? 'Executing in Java…' : `Run ${selected}`} <ArrowRight size={15} /></button></form>
      {result && <div className="result-panel"><div className="result-heading"><span className="eyebrow">SERVER OUTPUT · {selected.toUpperCase()} ({result.executionTime || 'Live'})</span><span className="result-check"><Check size={14} /> COMPLETE</span></div>{code === 'co1' && <><h3 className={result.found ? 'result-positive' : 'result-negative'}>{result.found ? 'Pattern found' : 'No match in this text'}</h3>{result.found && <p className="result-meta">First occurrence at character <b>{result.index}</b> · {result.length} characters matched</p>}<div className="match-visual"><span className="match-caption">CHARACTER ALIGNMENT</span><div className="match-text">{(result.found ? text.slice(0, result.index) : text).split('').map((char, index) => <i key={index} className={result.found && index >= result.index && index < result.index + result.length ? 'matched' : ''}>{char === ' ' ? '·' : char}</i>)}</div>{result.found && <div className="match-under">{Array.from({ length: result.length }, (_, index) => <i key={index} style={{ left: `calc(${((result.index + index) / text.length) * 100}% + 2px)` }}>↓</i>)}</div>}</div><p className="execution-note"><b>Backend Execution Details</b><br />{result.explanation || `${selected} executed via Java DSA adapter on Spring Boot.`}</p></>}{code === 'co2' && <><h3>{result.total} suffixes generated</h3><div className="suffix-list">{result.suffixes.map(({ index, suffix }) => <div key={index}><span className="suffix-index">{String(index).padStart(2, '0')}</span><code>{suffix.slice(0, 42)}{suffix.length > 42 ? '…' : ''}</code><span className="suffix-rank">RANK {String(result.suffixes.findIndex((item) => item.index === index) + 1).padStart(2, '0')}</span></div>)}</div><p className="execution-note"><b>Backend Execution Details</b><br />{result.explanation}</p></>}{code === 'co3' && <><h3>Edit distance / Cost: <span className="distance-number">{result.distance}</span></h3><div className="dp-matrix">{result.rows.map((row, index) => row.map((value, column) => <span key={`${index}-${column}`} className={index === result.rows.length - 1 && column === row.length - 1 ? 'dp-highlight' : ''}>{value}</span>))}</div><p className="execution-note"><b>DP Matrix Explanation</b><br />{result.explanation}</p></>}{code === 'co4' && <><h3>Maximum flow: <span className="distance-number">{result.maxFlow} units</span></h3><FlowDiagram /><p className="result-meta">Bottleneck path: {result.path.join(' → ')}</p><p className="execution-note"><b>Capacity & Flow Information</b><br />{result.explanation}</p></>}</div>}
      </div></section><aside className="algorithm-aside"><div className="panel aside-panel"><span className="eyebrow">IN THIS GROUP</span><h2>{group.label} methods</h2>{group.algorithms.map((algorithm, index) => <button className={`method-row ${selected === algorithm ? 'active' : ''}`} onClick={() => { setSelected(algorithm); setResult(null); setAlgoError(''); }} key={algorithm}><span className="method-index">{String(index + 1).padStart(2, '0')}</span><span>{algorithm}</span>{selected === algorithm && <ChevronRight size={15} />}</button>)}</div><div className="concept-note"><span className="concept-icon"><Leaf size={16} /></span><span className="eyebrow">SUPPLY CHAIN CONNECTION</span><p>{code === 'co1' ? 'String matching helps customers find produce by name, even across long descriptions and multiple farmer listings.' : code === 'co2' ? 'Suffix structures index product text so repeated substring searches can be answered efficiently at scale.' : code === 'co3' ? 'Dynamic programming helps compare similar requests and optimize decisions where each choice affects the next.' : 'Flow algorithms model how many goods can move through a network when routes and warehouse capacity are limited.'}</p></div></aside></div></>;
}

function FlowDiagram() {
  return <div className="flow-diagram"><div className="flow-node supplier">SUPPLIER</div><div className="flow-edge edge-one"><span>100</span></div><div className="flow-node warehouse-a">WAREHOUSE A</div><div className="flow-edge edge-two"><span>70</span></div><div className="flow-node distribution">DISTRIBUTION</div><div className="flow-edge edge-three"><span>100</span></div><div className="flow-node market">MARKET</div><div className="flow-branch">Warehouse B <span>→ 50</span></div></div>;
}

function HomeGate() {
  const { user } = useAuth();
  if (user?.role === 'farmer') return <Navigate to="/user" replace />;
  if (user?.role === 'customer') return <Navigate to="/customer" replace />;
  return <Shell><Home /></Shell>;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<HomeGate />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/customer" element={<Shell><CustomerDashboard /></Shell>} />
      <Route path="/user" element={<Shell><UserFarmerDashboard /></Shell>} />
      <Route path="/farmer" element={<Shell><UserFarmerDashboard /></Shell>} />
      <Route path="/dashboard" element={<Shell><Dashboard /></Shell>} />
      <Route path="/products" element={<Shell><Products /></Shell>} />
      <Route path="/products/:id" element={<Shell><ProductDetails /></Shell>} />
      <Route path="/orders" element={<Shell><Orders /></Shell>} />
      <Route path="/orders/new" element={<Shell><NewOrder /></Shell>} />
      <Route path="/orders/:id" element={<Shell><OrderTracking /></Shell>} />
      <Route path="/analytics" element={<Shell><Analytics /></Shell>} />
      <Route path="/algorithms" element={<Shell><Algorithms /></Shell>} />
      <Route path="/algorithms/:code" element={<Shell><AlgorithmDetail /></Shell>} />
      <Route path="*" element={<Shell><div className="empty-state"><h3>Page not found</h3><Link to="/" className="text-link">Return home <ArrowRight size={14} /></Link></div></Shell>} />
    </Routes>
  );
}
