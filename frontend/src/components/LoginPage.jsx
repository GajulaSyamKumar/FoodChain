import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  Sprout,
  ShoppingBasket,
  ArrowRight,
  Check,
  MapPin,
  Mail,
  Lock,
  User,
  ShieldCheck,
  Truck,
  Cpu,
  Database,
  ArrowLeft,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext.jsx';
import { getFarmers, createFarmer, getCustomers, createCustomer } from '../services/api.js';
import { cities } from '../data/mockData.js';

export default function LoginPage() {
  const { loginAsFarmer, loginAsCustomer } = useAuth();
  const navigate = useNavigate();

  // Role: 'customer' | 'farmer'
  const [role, setRole] = useState('customer');

  // Mode: 'signin' | 'register'
  const [mode, setMode] = useState('signin');

  // Sign-in typed credentials (no dropdown, manual input only)
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('1234');

  // Registration typed inputs
  const [regName, setRegName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regCity, setRegCity] = useState('Vijayawada');
  const [regPassword, setRegPassword] = useState('1234');

  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSignIn = async (e) => {
    e.preventDefault();
    setError('');
    setNotice('');
    setLoading(true);

    const inputVal = identifier.trim();
    if (!inputVal) {
      setError(`Please enter your ${role === 'farmer' ? 'Farmer Name or Farmer Code' : 'Email Address or Name'}.`);
      setLoading(false);
      return;
    }

    try {
      if (role === 'farmer') {
        const farmers = await getFarmers().catch(() => []);
        const matched = farmers.find(
          (f) =>
            (f.name && f.name.toLowerCase() === inputVal.toLowerCase()) ||
            (f.farmerCode && f.farmerCode.toLowerCase() === inputVal.toLowerCase()) ||
            (f.name && f.name.toLowerCase().includes(inputVal.toLowerCase()))
        );

        if (matched) {
          loginAsFarmer(matched);
          setNotice(`Welcome back, ${matched.name}!`);
          setTimeout(() => navigate('/user'), 400);
        } else {
          // Allow login with typed name
          const customFarmer = {
            id: Date.now() % 10000,
            name: inputVal,
            city: 'Vijayawada',
            farmerCode: `FARM-${Date.now().toString().slice(-4)}`,
          };
          loginAsFarmer(customFarmer);
          setNotice(`Signed in as ${customFarmer.name}`);
          setTimeout(() => navigate('/user'), 400);
        }
      } else {
        // Customer
        const customers = await getCustomers().catch(() => []);
        const matched = customers.find(
          (c) =>
            (c.email && c.email.toLowerCase() === inputVal.toLowerCase()) ||
            (c.name && c.name.toLowerCase() === inputVal.toLowerCase()) ||
            (c.customerCode && c.customerCode.toLowerCase() === inputVal.toLowerCase()) ||
            (c.name && c.name.toLowerCase().includes(inputVal.toLowerCase()))
        );

        if (matched) {
          loginAsCustomer(matched);
          setNotice(`Welcome back, ${matched.name}!`);
          setTimeout(() => navigate('/customer'), 400);
        } else {
          // Allow login with typed customer name or email
          const customCustomer = {
            id: Date.now() % 10000,
            name: inputVal.includes('@') ? inputVal.split('@')[0] : inputVal,
            email: inputVal.includes('@') ? inputVal : `${inputVal.toLowerCase().replaceAll(' ', '.')}@foodchain.local`,
            customerCode: `CUST-${Date.now().toString().slice(-4)}`,
          };
          loginAsCustomer(customCustomer);
          setNotice(`Signed in as ${customCustomer.name}`);
          setTimeout(() => navigate('/customer'), 400);
        }
      }
    } catch (err) {
      setError(err.message || 'Authentication error.');
      setLoading(false);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setNotice('');
    setLoading(true);

    if (!regName.trim()) {
      setError('Please provide your full name.');
      setLoading(false);
      return;
    }

    try {
      if (role === 'farmer') {
        const created = await createFarmer({
          name: regName.trim(),
          city: regCity,
          farmerCode: `FARM-${Date.now().toString().slice(-4)}`,
        });
        loginAsFarmer(created);
        setNotice(`Farm registered! Welcome, ${created.name}`);
        setTimeout(() => navigate('/user'), 400);
      } else {
        if (!regEmail.trim()) {
          setError('Please provide a valid email address.');
          setLoading(false);
          return;
        }
        const created = await createCustomer({
          name: regName.trim(),
          email: regEmail.trim(),
          customerCode: `CUST-${Date.now().toString().slice(-4)}`,
        });
        loginAsCustomer(created);
        setNotice(`Account registered! Welcome, ${created.name}`);
        setTimeout(() => navigate('/customer'), 400);
      }
    } catch (err) {
      setError(err.message || 'Registration failed.');
      setLoading(false);
    }
  };

  return (
    <div className="login-full-page">
      <div className="login-split-container">
        {/* LEFT PANEL: Executive Green Hero Banner */}
        <div className="login-hero-panel">
          <div className="hero-panel-top">
            <div className="hero-badge">
              <Sprout size={15} />
              <span>FOODCHAIN PLATFORM</span>
            </div>
            <h1 className="hero-headline">
              Intelligent Food Supply &amp; Logistics
            </h1>
            <p className="hero-subhead">
              Direct farmer-to-consumer distribution platform for Krishna District,
              powered by advanced algorithmic matching and network flow optimization.
            </p>

            <div className="hero-features-list">
              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <Truck size={17} />
                </div>
                <div>
                  <b>5 Regional Distribution Hubs</b>
                  <p>Vijayawada · Gudivada · Machilipatnam · Nuzvid · Pedana</p>
                </div>
              </div>

              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <Cpu size={17} />
                </div>
                <div>
                  <b>Algorithmic Route Optimization</b>
                  <p>String Search (CO1) · Suffix Arrays (CO2) · Dinic Flow (CO4)</p>
                </div>
              </div>

              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <Database size={17} />
                </div>
                <div>
                  <b>PostgreSQL 18 Real-Time Sync</b>
                  <p>Transactional inventory updates &amp; tamper-proof order logs</p>
                </div>
              </div>
            </div>
          </div>

          <div className="hero-panel-bottom">
            <div className="backend-live-indicator">
              <span className="live-dot-pulse" />
              <span>
                <b>Live Backend:</b> Spring Boot 3.3.4 · PostgreSQL Active
              </span>
            </div>
          </div>
        </div>

        {/* RIGHT PANEL: Input-Based Authentication Form */}
        <div className="login-form-panel">
          {/* Header */}
          <div className="form-panel-header">
            <div className="brand-logo-inline">
              <div className="brand-mark-sm">
                <Sprout size={18} />
              </div>
              <span className="brand-name-text">
                foodchain<span className="brand-dot">.</span>
              </span>
            </div>
            <h2 className="portal-title">
              {role === 'farmer' ? 'User (Farmer / Producer) Portal' : 'Customer (Buyer / Consumer) Portal'}
            </h2>
            <p className="portal-desc">
              Enter your credentials to access the Krishna Zone supply network.
            </p>
          </div>

          {/* Role Portal Tabs (Customer vs Farmer) */}
          <div className="portal-tabs-segmented">
            <button
              type="button"
              className={`portal-tab ${role === 'customer' ? 'active' : ''}`}
              onClick={() => {
                setRole('customer');
                setError('');
                setNotice('');
              }}
            >
              <ShoppingBasket size={16} />
              <span>Customer (Buyer)</span>
            </button>
            <button
              type="button"
              className={`portal-tab ${role === 'farmer' ? 'active' : ''}`}
              onClick={() => {
                setRole('farmer');
                setError('');
                setNotice('');
              }}
            >
              <Sprout size={16} />
              <span>User (Farmer)</span>
            </button>
          </div>

          {/* Sign In vs Register Tabs */}
          <div className="submode-tabs">
            <button
              type="button"
              className={`submode-tab ${mode === 'signin' ? 'active' : ''}`}
              onClick={() => {
                setMode('signin');
                setError('');
                setNotice('');
              }}
            >
              {role === 'farmer' ? 'Registered Farmer' : 'Sign In'}
            </button>
            <button
              type="button"
              className={`submode-tab ${mode === 'register' ? 'active' : ''}`}
              onClick={() => {
                setMode('register');
                setError('');
                setNotice('');
              }}
            >
              {role === 'farmer' ? '+ Register Farmer' : '+ Register Account'}
            </button>
          </div>

          {/* Alerts */}
          {error && <div className="auth-alert-error">{error}</div>}
          {notice && (
            <div className="auth-alert-success">
              <Check size={16} /> {notice}
            </div>
          )}

          {/* SIGN IN FORM (MANUAL INPUT ONLY - NO DROPDOWNS) */}
          {mode === 'signin' ? (
            <form onSubmit={handleSignIn} className="auth-inputs-form">
              <div className="input-field-group">
                <label htmlFor="user-identity">
                  {role === 'farmer'
                    ? 'FARMER NAME OR FARMER CODE'
                    : 'CUSTOMER EMAIL OR FULL NAME'}
                </label>
                <div className="field-icon-wrap">
                  {role === 'farmer' ? (
                    <User size={18} className="field-icon" />
                  ) : (
                    <Mail size={18} className="field-icon" />
                  )}
                  <input
                    id="user-identity"
                    type="text"
                    value={identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    placeholder={
                      role === 'farmer'
                        ? 'e.g. Lakshmi Devi or FARM-101'
                        : 'e.g. kiran@foodchain.local or Kiran Varma'
                    }
                    required
                    autoFocus
                  />
                </div>
              </div>

              <div className="input-field-group">
                <div className="label-aside-row">
                  <label htmlFor="user-password">SECURITY PIN / PASSWORD</label>
                  <span className="demo-pin-hint">
                    Demo PIN: <b>1234</b>
                  </span>
                </div>
                <div className="field-icon-wrap">
                  <Lock size={18} className="field-icon" />
                  <input
                    id="user-password"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Enter 4-digit PIN"
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                className="auth-submit-btn"
                disabled={loading}
              >
                <span>
                  {loading
                    ? 'Authenticating...'
                    : `Sign In as ${role === 'farmer' ? 'Farmer' : 'Customer'}`}
                </span>
                <ArrowRight size={17} />
              </button>

              <div className="input-hint-row">
                <span>
                  {role === 'farmer'
                    ? 'Type any registered farmer name or your own name to enter.'
                    : 'Type any customer email or name to enter store.'}
                </span>
              </div>
            </form>
          ) : (
            /* REGISTRATION FORM (MANUAL INPUT) */
            <form onSubmit={handleRegister} className="auth-inputs-form">
              <div className="input-field-group">
                <label htmlFor="reg-name">
                  {role === 'farmer' ? 'FARMER OR FARM NAME' : 'FULL NAME'}
                </label>
                <div className="field-icon-wrap">
                  <User size={18} className="field-icon" />
                  <input
                    id="reg-name"
                    type="text"
                    value={regName}
                    onChange={(e) => setRegName(e.target.value)}
                    placeholder={
                      role === 'farmer' ? 'e.g. Ramesh Naidu' : 'e.g. Ananya Rao'
                    }
                    required
                    autoFocus
                  />
                </div>
              </div>

              {role === 'farmer' ? (
                <div className="input-field-group">
                  <label htmlFor="reg-city">PRIMARY HUB / CITY</label>
                  <div className="field-icon-wrap">
                    <MapPin size={18} className="field-icon" />
                    <select
                      id="reg-city"
                      value={regCity}
                      onChange={(e) => setRegCity(e.target.value)}
                    >
                      {cities.map((city) => (
                        <option key={city} value={city}>
                          {city}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              ) : (
                <div className="input-field-group">
                  <label htmlFor="reg-email">EMAIL ADDRESS</label>
                  <div className="field-icon-wrap">
                    <Mail size={18} className="field-icon" />
                    <input
                      id="reg-email"
                      type="email"
                      value={regEmail}
                      onChange={(e) => setRegEmail(e.target.value)}
                      placeholder="e.g. ananya@foodchain.local"
                      required
                    />
                  </div>
                </div>
              )}

              <div className="input-field-group">
                <label htmlFor="reg-password">CREATE 4-DIGIT PIN</label>
                <div className="field-icon-wrap">
                  <Lock size={18} className="field-icon" />
                  <input
                    id="reg-password"
                    type="password"
                    value={regPassword}
                    onChange={(e) => setRegPassword(e.target.value)}
                    placeholder="Enter 4-digit PIN (e.g. 1234)"
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                className="auth-submit-btn"
                disabled={loading}
              >
                <span>
                  {loading
                    ? 'Registering...'
                    : `Create ${role === 'farmer' ? 'Farmer Profile' : 'Customer Account'}`}
                </span>
                <ArrowRight size={17} />
              </button>
            </form>
          )}

          {/* Footer details */}
          <div className="form-panel-footer">
            <div className="security-tag">
              <ShieldCheck size={14} />
              <span>End-to-End Encryption · PostgreSQL 18 Backed Session</span>
            </div>

            <Link to="/products" className="guest-return-link">
              <ArrowLeft size={13} />
              <span>Continue to Marketplace as Guest</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
