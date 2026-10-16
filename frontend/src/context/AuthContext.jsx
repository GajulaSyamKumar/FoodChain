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
    // Default demo persona: Farmer Lakshmi Devi or null
    return {
      role: 'customer',
      id: 1,
      name: 'Kiran Varma',
      email: 'kiran@foodchain.local',
      customerCode: 'CUST-1001',
    };
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
      id: farmerData.id || 1,
      name: farmerData.name || 'Lakshmi Devi',
      city: farmerData.city || 'Vijayawada',
      farmerCode: farmerData.farmerCode || 'FARM-101',
    };
    setUser(farmerUser);
    return farmerUser;
  };

  const loginAsCustomer = (customerData) => {
    const customerUser = {
      role: 'customer',
      id: customerData.id || 1,
      name: customerData.name || 'Kiran Varma',
      email: customerData.email || 'kiran@foodchain.local',
      customerCode: customerData.customerCode || 'CUST-1001',
    };
    setUser(customerUser);
    return customerUser;
  };

  const logout = () => {
    setUser(null);
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
