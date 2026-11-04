package com.cia2.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();
    private final AtomicLong ids = new AtomicLong(3);

    public ExpenseService() {
        expenses.add(new Expense(1L, "Groceries", "Food", new BigDecimal("850.00"), LocalDate.now().minusDays(1), "Weekly groceries"));
        expenses.add(new Expense(2L, "Bus pass", "Transport", new BigDecimal("500.00"), LocalDate.now().minusDays(2), "Monthly commute"));
        expenses.add(new Expense(3L, "Mobile recharge", "Bills", new BigDecimal("299.00"), LocalDate.now(), "Phone plan"));
    }

    public synchronized List<Expense> findAll(String category) {
        return expenses.stream()
            .filter(e -> category == null || category.isBlank() || e.getCategory().equalsIgnoreCase(category))
            .sorted(Comparator.comparing(Expense::getDate).reversed().thenComparing(Expense::getId).reversed())
            .toList();
    }

    public synchronized Expense create(ExpenseRequest request) {
        Expense expense = new Expense(ids.incrementAndGet(), request.getTitle().trim(),
            request.getCategory().trim(), request.getAmount(), request.getDate(),
            request.getNotes() == null ? "" : request.getNotes().trim());
        expenses.add(expense);
        return expense;
    }

    public synchronized Expense update(Long id, ExpenseRequest request) {
        Expense old = findById(id);
        old.setTitle(request.getTitle().trim());
        old.setCategory(request.getCategory().trim());
        old.setAmount(request.getAmount());
        old.setDate(request.getDate());
        old.setNotes(request.getNotes() == null ? "" : request.getNotes().trim());
        return old;
    }

    public synchronized void delete(Long id) {
        expenses.remove(findById(id));
    }

    public synchronized Expense findById(Long id) {
        return expenses.stream().filter(e -> e.getId().equals(id)).findFirst()
            .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public synchronized BigDecimal total() {
        return expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public synchronized long count() { return expenses.size(); }

    public synchronized List<CategoryTotal> categoryTotals() {
        return expenses.stream().collect(java.util.stream.Collectors.groupingBy(
            Expense::getCategory, java.util.TreeMap::new,
            java.util.stream.Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)))
            .entrySet().stream().map(e -> new CategoryTotal(e.getKey(), e.getValue())).toList();
    }

    public record CategoryTotal(String category, BigDecimal total) {}
}
