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
  AlertCircle,
  KeyRound,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext.jsx';
import {
  loginFarmerApi,
  registerFarmerApi,
  loginCustomerApi,
  registerCustomerApi,
} from '../services/api.js';
import { cities } from '../data/mockData.js';

export default function LoginPage() {
  const { loginAsFarmer, loginAsCustomer } = useAuth();
  const navigate = useNavigate();

  // Role: 'customer' | 'farmer'
  const [role, setRole] = useState('customer');

  // Mode: 'signin' | 'register'
  const [mode, setMode] = useState('signin');

  // Sign-in typed credentials (no predeclared credentials)
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');

  // Registration typed inputs (enrolled and stored in pgAdmin)
  const [regName, setRegName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regCity, setRegCity] = useState('Vijayawada');
  const [regPhone, setRegPhone] = useState('');
  const [regPassword, setRegPassword] = useState('');

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
      setError(`Please enter your ${role === 'farmer' ? 'Farmer Name or Farmer Code' : 'Email Address or Customer Code'}.`);
      setLoading(false);
      return;
    }
    if (!password) {
      setError('Please enter your account password.');
      setLoading(false);
      return;
    }

    try {
      if (role === 'farmer') {
        // Authenticate against PostgreSQL farmers table
        const authedFarmer = await loginFarmerApi({
          identifier: inputVal,
          password: password.trim(),
        });

        loginAsFarmer(authedFarmer);
        setNotice(`Welcome back, Farmer ${authedFarmer.name}!`);
        setTimeout(() => navigate('/user'), 400);
      } else {
        // Authenticate against PostgreSQL customers table
        const authedCustomer = await loginCustomerApi({
          identifier: inputVal,
          password: password.trim(),
        });

        loginAsCustomer(authedCustomer);
        setNotice(`Welcome back, ${authedCustomer.name}!`);
        setTimeout(() => navigate('/customer'), 400);
      }
    } catch (err) {
      setError(err.message || 'Authentication failed. Please verify credentials.');
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
    if (!regPassword.trim()) {
      setError('Please choose a password for your account.');
      setLoading(false);
      return;
    }

    try {
      if (role === 'farmer') {
        // Enroll new farmer into pgAdmin database
        const created = await registerFarmerApi({
          name: regName.trim(),
          city: regCity,
          email: regEmail.trim(),
          phone: regPhone.trim(),
          password: regPassword.trim(),
        });

        loginAsFarmer(created);
        setNotice(`Producer registered and stored in pgAdmin! Welcome, ${created.name}`);
        setTimeout(() => navigate('/user'), 500);
      } else {
        if (!regEmail.trim()) {
          setError('Please provide a valid email address.');
          setLoading(false);
          return;
        }

        // Enroll new customer into pgAdmin database
        const created = await registerCustomerApi({
          name: regName.trim(),
          email: regEmail.trim(),
          city: regCity,
          phone: regPhone.trim(),
          password: regPassword.trim(),
        });

        loginAsCustomer(created);
        setNotice(`Customer registered and stored in pgAdmin! Welcome, ${created.name}`);
        setTimeout(() => navigate('/customer'), 500);
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
              powered by PostgreSQL 18 and algorithmic route optimization.
            </p>

            <div className="hero-features-list">
              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <Database size={17} />
                </div>
                <div>
                  <b>Real-Time pgAdmin &amp; PostgreSQL Sync</b>
                  <p>All credentials, farm inventories &amp; customer orders stored directly in database</p>
                </div>
              </div>

              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <ShieldCheck size={17} />
                </div>
                <div>
                  <b>Customer Data Privacy &amp; Isolation</b>
                  <p>Each customer's orders and delivery records are strictly private and isolated</p>
                </div>
              </div>

              <div className="hero-feature-item">
                <div className="hero-feature-icon">
                  <Truck size={17} />
                </div>
                <div>
                  <b>5 Regional Distribution Hubs</b>
                  <p>Vijayawada · Gudivada · Machilipatnam · Nuzvid · Pedana</p>
                </div>
              </div>
            </div>
          </div>

          <div className="hero-panel-bottom">
            <div className="backend-live-indicator">
              <span className="live-dot-pulse" />
              <span>
                <b>Live PostgreSQL Backend:</b> Spring Boot 3.3.4 · Port 8080 Active
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
              Enter your enrolled credentials stored in pgAdmin to access your portal.
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
              <span>Customer Portal</span>
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
              <span>User (Farmer) Portal</span>
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
              {role === 'farmer' ? 'Sign In as Farmer' : 'Sign In as Customer'}
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
              {role === 'farmer' ? '+ Enroll New Farmer' : '+ Enroll New Customer'}
            </button>
          </div>

          {/* Alerts */}
          {error && (
            <div className="auth-alert-error" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <AlertCircle size={16} />
              <span>{error}</span>
            </div>
          )}
          {notice && (
            <div className="auth-alert-success" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Check size={16} />
              <span>{notice}</span>
            </div>
          )}

          {/* SIGN IN FORM (MANUAL INPUT) */}
          {mode === 'signin' ? (
            <form onSubmit={handleSignIn} className="auth-inputs-form">
              <div className="input-field-group">
                <label htmlFor="user-identity">
                  {role === 'farmer'
                    ? 'FARMER NAME OR FARMER CODE'
                    : 'CUSTOMER EMAIL, NAME, OR CODE'}
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
                        : 'e.g. kiran@foodchain.local or CUST-1001'
                    }
                    required
                    autoFocus
                  />
                </div>
              </div>

              <div className="input-field-group">
                <label htmlFor="user-password">PASSWORD</label>
                <div className="field-icon-wrap">
                  <Lock size={18} className="field-icon" />
                  <input
                    id="user-password"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Enter account password"
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
                    ? 'Verifying in PostgreSQL...'
                    : `Sign In as ${role === 'farmer' ? 'Farmer' : 'Customer'}`}
                </span>
                <ArrowRight size={17} />
              </button>
            </form>
          ) : (
            /* REGISTRATION FORM (ENROLL DIRECTLY INTO PGADMIN) */
            <form onSubmit={handleRegister} className="auth-inputs-form">
              <div className="input-field-group">
                <label htmlFor="reg-name">
                  {role === 'farmer' ? 'FARMER OR PRODUCER NAME' : 'FULL NAME'}
                </label>
                <div className="field-icon-wrap">
                  <User size={18} className="field-icon" />
                  <input
                    id="reg-name"
                    type="text"
                    value={regName}
                    onChange={(e) => setRegName(e.target.value)}
                    placeholder={
                      role === 'farmer' ? 'e.g. Subba Rao' : 'e.g. Ananya Sharma'
                    }
                    required
                    autoFocus
                  />
                </div>
              </div>

              <div className="input-field-group">
                <label htmlFor="reg-email">
                  {role === 'farmer' ? 'EMAIL ADDRESS (OPTIONAL)' : 'EMAIL ADDRESS'}
                </label>
                <div className="field-icon-wrap">
                  <Mail size={18} className="field-icon" />
                  <input
                    id="reg-email"
                    type="email"
                    value={regEmail}
                    onChange={(e) => setRegEmail(e.target.value)}
                    placeholder={
                      role === 'farmer' ? 'e.g. farmer@foodchain.local' : 'e.g. customer@example.com'
                    }
                    required={role === 'customer'}
                  />
                </div>
              </div>

              <div className="input-field-group">
                <label htmlFor="reg-city">LOCATION / PRIMARY HUB</label>
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

              <div className="input-field-group">
                <label htmlFor="reg-password">SET ACCOUNT PASSWORD</label>
                <div className="field-icon-wrap">
                  <Lock size={18} className="field-icon" />
                  <input
                    id="reg-password"
                    type="password"
                    value={regPassword}
                    onChange={(e) => setRegPassword(e.target.value)}
                    placeholder="Create a secure password"
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
                    ? 'Saving to pgAdmin...'
                    : `Enroll in pgAdmin & Enter Portal`}
                </span>
                <Check size={17} />
              </button>
            </form>
          )}

          {/* Footer note */}
          <div className="form-panel-footer">
            <div className="security-tag">
              <ShieldCheck size={14} />
              <span>Enrolled records are stored in PostgreSQL 18 `foodchain_db`</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
