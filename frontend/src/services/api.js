const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

async function request(endpoint, options = {}) {
  const url = `${BASE_URL}${endpoint}`;
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };

  try {
    const res = await fetch(url, { ...options, headers });
    if (!res.ok) {
      let errMessage = `Server error (${res.status})`;
      try {
        const errorData = await res.json();
        if (errorData.message) errMessage = errorData.message;
      } catch (e) {
        // fallback to status text
        if (res.statusText) errMessage = res.statusText;
      }
      throw new Error(errMessage);
    }
    if (res.status === 204) return null;
    return await res.json();
  } catch (err) {
    // If backend server is not running, provide informative error
    if (err.name === 'TypeError' && err.message.includes('fetch')) {
      throw new Error('Unable to reach FoodChain backend at ' + BASE_URL + '. Ensure Spring Boot server is running on port 8080.');
    }
    throw err;
  }
}

// ==========================================
// PRODUCTS
// ==========================================
export async function getProducts(city) {
  const query = city ? `?city=${encodeURIComponent(city)}` : '';
  return await request(`/products${query}`);
}

export async function searchProducts(term) {
  if (!term || !term.trim()) return getProducts();
  return await request(`/products/search?name=${encodeURIComponent(term.trim())}`);
}

export async function getProductById(id) {
  return await request(`/products/${encodeURIComponent(id)}`);
}

export async function addProduct(input) {
  const payload = {
    farmer: input.farmer,
    productName: input.product,
    product: input.product,
    quantity: Number(input.quantity),
    city: input.city,
    description: input.description,
    price: input.price ? Number(input.price) : 40.0,
    warehouse: input.warehouse || `${input.city} Warehouse`,
    zone: 'Krishna',
    category: input.category || 'Vegetables',
  };
  return await request('/products', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function updateStock(productId, quantity) {
  return await request(`/products/${encodeURIComponent(productId)}/stock`, {
    method: 'PATCH',
    body: JSON.stringify({ quantity: Number(quantity) }),
  });
}

// ==========================================
// ORDERS
// ==========================================
export async function placeOrder({ productId, quantity, destination, customerId }) {
  const payload = {
    productId,
    quantity: Number(quantity),
    destination,
    destinationCity: destination,
    customerId: customerId || null,
  };
  return await request('/orders', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function getOrders(status) {
  const query = status ? `?status=${encodeURIComponent(status)}` : '';
  return await request(`/orders${query}`);
}

export async function getOrderById(orderId) {
  return await request(`/orders/${encodeURIComponent(orderId)}`);
}

export async function cancelOrder(orderId) {
  return await request(`/orders/${encodeURIComponent(orderId)}/cancel`, {
    method: 'POST',
  });
}

export async function getFarmerOrders(farmerName) {
  if (!farmerName) return [];
  return await request(`/orders/farmer/${encodeURIComponent(farmerName)}`);
}

export async function updateOrderStatus(orderId, status) {
  return await request(`/orders/${encodeURIComponent(orderId)}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status }),
  });
}

// ==========================================
// TRACKING
// ==========================================
export async function getTracking(orderId) {
  return await request(`/tracking/${encodeURIComponent(orderId)}`);
}

export async function getTrackingHistory(orderId) {
  return await request(`/tracking/${encodeURIComponent(orderId)}/history`);
}

// ==========================================
// DASHBOARD & ANALYTICS
// ==========================================
export async function getDashboardSummary() {
  return await request('/dashboard/summary');
}

export async function getAnalytics() {
  return await request('/analytics');
}

// ==========================================
// ALGORITHM LAB (CO1 - CO4)
// ==========================================
export async function executeCO1(payload) {
  return await request('/algorithms/co1/execute', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function executeCO2(payload) {
  return await request('/algorithms/co2/execute', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function executeCO3(payload) {
  return await request('/algorithms/co3/execute', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function executeCO4(payload) {
  return await request('/algorithms/co4/execute', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

// ==========================================
// FARMERS & CUSTOMERS (AUTH & PROFILES)
// ==========================================
export async function getFarmers() {
  return await request('/farmers');
}

export async function createFarmer(farmer) {
  return await request('/farmers', {
    method: 'POST',
    body: JSON.stringify(farmer),
  });
}

export async function getFarmerProducts(farmerId) {
  return await request(`/farmers/${encodeURIComponent(farmerId)}/products`);
}

export async function getCustomers() {
  return await request('/customers');
}

export async function createCustomer(customer) {
  return await request('/customers', {
    method: 'POST',
    body: JSON.stringify(customer),
  });
}

export async function getCustomerOrders(customerId) {
  if (!customerId) return [];
  return await request(`/orders/customer/${encodeURIComponent(customerId)}`);
}

// User (Farmer / Producer domain) endpoints
export async function getUsers() {
  return await request('/users');
}

export async function createUser(userData) {
  return await request('/users', {
    method: 'POST',
    body: JSON.stringify(userData),
  });
}

// Real PostgreSQL-backed Authentication (pgAdmin storage)
export async function loginFarmerApi({ identifier, password }) {
  return await request('/auth/farmer/login', {
    method: 'POST',
    body: JSON.stringify({ identifier, password }),
  });
}

export async function registerFarmerApi(farmerData) {
  return await request('/auth/farmer/register', {
    method: 'POST',
    body: JSON.stringify(farmerData),
  });
}

export async function loginCustomerApi({ identifier, password }) {
  return await request('/auth/customer/login', {
    method: 'POST',
    body: JSON.stringify({ identifier, password }),
  });
}

export async function registerCustomerApi(customerData) {
  return await request('/auth/customer/register', {
    method: 'POST',
    body: JSON.stringify(customerData),
  });
}

