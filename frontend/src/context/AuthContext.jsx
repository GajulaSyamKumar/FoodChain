import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

const STORAGE_KEY = 'foodchain_current_user';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      // ignore parse error
    }
    // No predeclared user — users must enroll or sign in with their real database credentials
    return null;
  });

  useEffect(() => {
    if (user) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }, [user]);

  const loginAsFarmer = (farmerData) => {
    const farmerUser = {
      role: 'farmer',
      id: farmerData.id,
      name: farmerData.name,
      city: farmerData.city || 'Vijayawada',
      farmerCode: farmerData.farmerCode,
      email: farmerData.email,
      phone: farmerData.phone,
    };
    setUser(farmerUser);
    return farmerUser;
  };

  const loginAsCustomer = (customerData) => {
    const customerUser = {
      role: 'customer',
      id: customerData.id,
      name: customerData.name,
      email: customerData.email,
      customerCode: customerData.customerCode,
      phone: customerData.phone,
      city: customerData.city || 'Vijayawada',
      address: customerData.address,
    };
    setUser(customerUser);
    return customerUser;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_KEY);
  };

  return (
    <AuthContext.Provider value={{ user, loginAsFarmer, loginAsCustomer, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
