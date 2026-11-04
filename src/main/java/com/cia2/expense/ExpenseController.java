package com.cia2.expense;

import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) { this.service = service; }

    @GetMapping("/expenses")
    public java.util.List<Expense> getExpenses(@RequestParam(required = false) String category) {
        return service.findAll(category);
    }

    @GetMapping("/expenses/{id}")
    public Expense getExpense(@PathVariable Long id) { return service.findById(id); }

    @PostMapping("/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createExpense(@Valid @RequestBody ExpenseRequest request) {
        return service.create(request);
    }

    @PutMapping("/expenses/{id}")
    public Expense updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) { service.delete(id); }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return Map.of("totalSpent", service.total(), "expenseCount", service.count(),
            "categoryTotals", service.categoryTotals());
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "application", "CIA 2 Expense Tracker");
    }
}
