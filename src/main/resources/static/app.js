const list = document.getElementById("expenseList");
const form = document.getElementById("expenseForm");
const formMessage = document.getElementById("formMessage");
const statusBadge = document.getElementById("apiStatus");
const categoryIcons = { Food: "🍽️", Transport: "🚌", Bills: "🧾", Shopping: "🛍️", Health: "💊", Other: "📌" };

document.getElementById("date").value = new Date().toISOString().slice(0, 10);

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });
  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try { const body = await response.json(); message = body.error || message; } catch (_) {}
    throw new Error(message);
  }
  return response.status === 204 ? null : response.json();
}

function money(value) {
  return new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR" }).format(value);
}

async function refresh() {
  try {
    const category = document.getElementById("filterCategory").value;
    const [expenses, summary] = await Promise.all([
      api("/api/expenses" + (category ? `?category=${encodeURIComponent(category)}` : "")),
      api("/api/summary")
    ]);
    statusBadge.textContent = "Backend connected";
    statusBadge.className = "status online";
    document.getElementById("totalSpent").textContent = money(summary.totalSpent);
    document.getElementById("expenseCount").textContent = summary.expenseCount;
    renderExpenses(expenses);
    renderCategories(summary.categoryTotals);
  } catch (error) {
    statusBadge.textContent = "Backend unavailable";
    statusBadge.className = "status offline";
    list.textContent = "Could not load expenses. Please check that the backend is running.";
  }
}

function renderExpenses(expenses) {
  list.replaceChildren();
  if (!expenses.length) {
    const empty = document.createElement("p");
    empty.className = "muted";
    empty.textContent = "No expenses found. Add one above.";
    list.append(empty);
    return;
  }
  expenses.forEach(expense => {
    const row = document.createElement("div");
    row.className = "expense";
    const icon = document.createElement("div");
    icon.className = "expenseIcon";
    icon.textContent = categoryIcons[expense.category] || categoryIcons.Other;
    const info = document.createElement("div");
    info.className = "expenseInfo";
    const title = document.createElement("strong");
    title.textContent = expense.title;
    const detail = document.createElement("small");
    detail.textContent = `${expense.category} · ${expense.date}${expense.notes ? " · " + expense.notes : ""}`;
    info.append(title, detail);
    const amount = document.createElement("span");
    amount.className = "expenseAmount";
    amount.textContent = money(expense.amount);
    const remove = document.createElement("button");
    remove.className = "delete";
    remove.textContent = "Delete";
    remove.addEventListener("click", async () => {
      if (!confirm(`Delete ${expense.title}?`)) return;
      try { await api(`/api/expenses/${expense.id}`, { method: "DELETE" }); await refresh(); }
      catch (error) { alert(error.message); }
    });
    row.append(icon, info, amount, remove);
    list.append(row);
  });
}

function renderCategories(items) {
  const container = document.getElementById("categorySummary");
  container.replaceChildren();
  if (!items.length) {
    const empty = document.createElement("p");
    empty.className = "muted";
    empty.textContent = "Category totals will appear here.";
    container.append(empty);
    return;
  }
  const max = Math.max(...items.map(item => Number(item.total)), 1);
  items.forEach(item => {
    const row = document.createElement("div");
    row.className = "categoryRow";
    const name = document.createElement("span");
    name.textContent = item.category;
    const track = document.createElement("div");
    track.className = "bar";
    const fill = document.createElement("div");
    fill.className = "barFill";
    fill.style.width = `${Number(item.total) / max * 100}%`;
    track.append(fill);
    const amount = document.createElement("strong");
    amount.textContent = money(item.total);
    row.append(name, track, amount);
    container.append(row);
  });
}

form.addEventListener("submit", async event => {
  event.preventDefault();
  const payload = {
    title: document.getElementById("title").value.trim(),
    category: document.getElementById("category").value,
    amount: Number(document.getElementById("amount").value),
    date: document.getElementById("date").value,
    notes: document.getElementById("notes").value.trim()
  };
  const button = document.getElementById("saveButton");
  button.disabled = true;
  formMessage.textContent = "";
  try {
    await api("/api/expenses", { method: "POST", body: JSON.stringify(payload) });
    form.reset();
    document.getElementById("date").value = new Date().toISOString().slice(0, 10);
    formMessage.textContent = "Expense added successfully.";
    await refresh();
  } catch (error) {
    formMessage.textContent = error.message;
  } finally { button.disabled = false; }
});

document.getElementById("filterCategory").addEventListener("change", refresh);
refresh();
