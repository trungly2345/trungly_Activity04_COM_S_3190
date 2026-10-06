const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// In-Memory product catalog for demo REST API
let products = [
  { id: 'p1', name: 'Pho Dac Biet (Special Beef Noodle Soup)', category: 'Mains', price: 14.99, description: 'Traditional Vietnamese beef noodle soup with rare steak, brisket, and meatballs.', image: '🍜' },
  { id: 'p2', name: 'Crispy Spring Rolls (Cha Gio)', category: 'Appetizers', price: 7.50, description: 'Crispy fried pork and shrimp rolls served with nuoc cham dipping sauce.', image: '🌯' },
  { id: 'p3', name: 'Banh Mi Thit Nuong', category: 'Mains', price: 9.25, description: 'Grilled pork baguette sandwich with pickled carrots, daikon, and fresh cilantro.', image: '🥪' },
  { id: 'p4', name: 'Iced Vietnamese Coffee (Ca Phe Sua Da)', category: 'Drinks', price: 4.75, description: 'Strong dark roast coffee brewed with sweetened condensed milk over ice.', image: '☕' },
  { id: 'p5', name: 'Pandan Che Dessert', category: 'Desserts', price: 5.50, description: 'Sweet coconut milk soup with pandan jelly and mung beans.', image: '🍧' }
];

let orders = [];
let inquiries = [];

// GET menu products
app.get('/api/products', (req, res) => {
  res.json({ success: true, count: products.length, data: products });
});

// POST new order
app.post('/api/orders', (req, res) => {
  const { customerName, items, totalAmount, notes } = req.body;
  if (!customerName || !items || items.length === 0) {
    return res.status(400).json({ success: false, message: 'Invalid order payload' });
  }
  const newOrder = {
    id: 'ORD-' + Math.floor(100000 + Math.random() * 900000),
    customerName,
    items,
    totalAmount,
    status: 'Pending',
    createdAt: new Date().toISOString()
  };
  orders.unshift(newOrder);
  res.status(201).json({ success: true, data: newOrder });
});

// GET all orders
app.get('/api/orders', (req, res) => {
  res.json({ success: true, data: orders });
});

// PUT update order status
app.put('/api/orders/:id/status', (req, res) => {
  const { status } = req.body;
  const order = orders.find(o => o.id === req.params.id);
  if (!order) return res.status(404).json({ success: false, message: 'Order not found' });
  order.status = status;
  res.json({ success: true, data: order });
});

// POST customer inquiry
app.post('/api/inquiries', (req, res) => {
  const { name, email, message } = req.body;
  const inquiry = { id: 'INQ-' + Date.now(), name, email, message, createdAt: new Date().toISOString() };
  inquiries.push(inquiry);
  res.status(201).json({ success: true, data: inquiry });
});

app.listen(PORT, () => {
  console.log(`Restaurant API server running on port ${PORT}`);
});

module.exports = app;
