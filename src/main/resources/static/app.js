const money = value => new Intl.NumberFormat('en-IN', {
  style: 'currency', currency: 'INR'
}).format(value);

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(data.message || 'Request failed');
  return data;
}

async function loadProducts() {
  const products = await request('/api/products');
  document.getElementById('product-grid').innerHTML = products.map(product => `
    <article class="product-card">
      <img src="${product.image}" alt="${product.name}" loading="lazy">
      <div class="product-info">
        <h3>${product.name}</h3>
        <p>${product.description}</p>
        <div class="product-bottom">
          <span class="price">${money(product.price)}</span>
          <button class="add-button" onclick="addToCart(${product.id})">Add to cart +</button>
        </div>
      </div>
    </article>
  `).join('');
}

async function loadCart() {
  const cart = await request('/api/cart');
  document.getElementById('cart-count').textContent = cart.itemCount;
  document.getElementById('cart-total').textContent = money(cart.total);
  document.getElementById('checkout-button').disabled = cart.itemCount === 0;
  const target = document.getElementById('cart-items');
  if (cart.items.length === 0) {
    target.innerHTML = '<p class="muted">Your cart is empty. Add something you like!</p>';
    return;
  }
  target.innerHTML = cart.items.map(item => `
    <div class="cart-row">
      <img src="${item.image}" alt="${item.name}">
      <div class="details"><strong>${item.name}</strong><span>${money(item.price)} each · Line total ${money(item.price * item.quantity)}</span></div>
      <div class="quantity-controls">
        <button aria-label="Decrease quantity" onclick="removeOne(${item.productId})">−</button>
        <strong>${item.quantity}</strong>
        <button aria-label="Increase quantity" onclick="addToCart(${item.productId})">+</button>
      </div>
    </div>
  `).join('');
}

function showNotice(message) {
  document.getElementById('notice').textContent = message;
}

async function addToCart(id) {
  try {
    await request(`/api/cart/${id}`, { method: 'POST' });
    await loadCart();
    showNotice('Added to your cart.');
  } catch (error) { showNotice(error.message); }
}

async function removeOne(id) {
  try {
    await request(`/api/cart/${id}`, { method: 'DELETE' });
    await loadCart();
    showNotice('Cart updated.');
  } catch (error) { showNotice(error.message); }
}

async function checkout() {
  try {
    const result = await request('/api/checkout', { method: 'POST' });
    await loadCart();
    showNotice(`${result.message} Order: ${result.orderNumber} · Total: ${money(result.total)}`);
  } catch (error) { showNotice(error.message); }
}

Promise.all([loadProducts(), loadCart()]).catch(error => showNotice(error.message));
