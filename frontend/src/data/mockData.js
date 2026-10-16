export const cities = ['Vijayawada', 'Machilipatnam', 'Gudivada', 'Nuzvid', 'Pedana'];

export const initialProducts = [
  { id: '356344', productCode: 356344, farmer: 'Lakshmi Devi', product: 'Organic Tomatoes', quantity: 160, zone: 'Krishna', warehouse: 'Vijayawada Warehouse', description: 'Vine-ripened, locally grown tomatoes harvested this morning.', price: 42, category: 'Vegetables', image: 'https://images.unsplash.com/photo-1546094096-0df4bcaaa337?auto=format&fit=crop&w=900&q=85' },
  { id: '356345', productCode: 356345, farmer: 'Ravi Kumar', product: 'Fresh Potatoes', quantity: 420, zone: 'Krishna', warehouse: 'Gudivada Warehouse', description: 'Firm, clean potatoes from small family farms.', price: 28, category: 'Vegetables', image: 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=900&q=85' },
  { id: '356346', productCode: 356346, farmer: 'Meena Rao', product: 'Green Chilies', quantity: 85, zone: 'Krishna', warehouse: 'Machilipatnam Warehouse', description: 'Bright, fresh green chilies with a lively heat.', price: 64, category: 'Vegetables', image: 'https://images.unsplash.com/photo-1588252303782-cb80119abd6d?auto=format&fit=crop&w=900&q=85' },
  { id: '356347', productCode: 356347, farmer: 'Suresh Babu', product: 'Organic Carrots', quantity: 230, zone: 'Krishna', warehouse: 'Vijayawada Warehouse', description: 'Sweet, crisp carrots grown without synthetic pesticides.', price: 38, category: 'Vegetables', image: 'https://images.unsplash.com/photo-1445282768818-728615cc910a?auto=format&fit=crop&w=900&q=85' },
  { id: '356348', productCode: 356348, farmer: 'Anitha Prasad', product: 'Fresh Onions', quantity: 610, zone: 'Krishna', warehouse: 'Pedana Warehouse', description: 'A kitchen staple, sorted and ready for distribution.', price: 32, category: 'Vegetables', image: 'https://images.unsplash.com/photo-1508747703725-719777637510?auto=format&fit=crop&w=900&q=85' },
  { id: '356349', productCode: 356349, farmer: 'Venkatesh Rao', product: 'Sona Masoori Rice', quantity: 960, zone: 'Krishna', warehouse: 'Nuzvid Warehouse', description: 'Light, aromatic rice sourced directly from Krishna farms.', price: 58, category: 'Grains', image: 'https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=900&q=85' },
  { id: '356350', productCode: 356350, farmer: 'Priya Nair', product: 'Bananas', quantity: 145, zone: 'Krishna', warehouse: 'Gudivada Warehouse', description: 'Naturally ripened bananas packed for a short journey.', price: 46, category: 'Fruit', image: 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?auto=format&fit=crop&w=900&q=85' },
  { id: '356351', productCode: 356351, farmer: 'Mohan Reddy', product: 'Banganapalli Mangoes', quantity: 75, zone: 'Krishna', warehouse: 'Nuzvid Warehouse', description: 'Seasonal mangoes from the orchards around Nuzvid.', price: 120, category: 'Fruit', image: 'https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=900&q=85' },
];

export const seedOrders = [
  { id: 'ORD1001', product: 'Organic Tomatoes', productCode: 356344, quantity: 10, farmer: 'Lakshmi Devi', warehouse: 'Vijayawada Warehouse', destination: 'Gudivada', status: 'In Transit', createdAt: 'Today, 09:42' },
  { id: 'ORD1000', product: 'Sona Masoori Rice', productCode: 356349, quantity: 25, farmer: 'Venkatesh Rao', warehouse: 'Nuzvid Warehouse', destination: 'Vijayawada', status: 'Prepared', createdAt: 'Today, 08:15' },
  { id: 'ORD0998', product: 'Fresh Potatoes', productCode: 356345, quantity: 12, farmer: 'Ravi Kumar', warehouse: 'Gudivada Warehouse', destination: 'Pedana', status: 'Delivered', createdAt: 'Yesterday' },
];

export const algorithmGroups = [
  { id: 'co1', label: 'CO1', title: 'String algorithms', count: '04 methods', description: 'Find patterns across products, descriptions and incoming requests.', algorithms: ['KMP', 'Z-Function', 'Rabin–Karp', 'Aho–Corasick'], icon: 'scan' },
  { id: 'co2', label: 'CO2', title: 'Suffix structures', count: '05 structures', description: 'Index product text for fast prefix, substring and similarity lookup.', algorithms: ['Suffix Array', 'SA-IS', 'LCP / Kasai', 'Suffix Tree', 'Suffix Automaton'], icon: 'layers' },
  { id: 'co3', label: 'CO3', title: 'Dynamic programming', count: '05 problems', description: 'Compare names, plan stock allocation and minimize delivery cost.', algorithms: ['Levenshtein', 'Damerau–Levenshtein', 'Bitmask DP', 'Matrix Chain', 'Optimal BST'], icon: 'grid' },
  { id: 'co4', label: 'CO4', title: 'Network flow', count: '05 methods', description: 'Model capacity through warehouses and optimize distribution paths.', algorithms: ['Ford–Fulkerson', 'Edmonds–Karp', 'Dinic', 'Bipartite Matching', 'Max-Flow Min-Cut'], icon: 'share' },
];
