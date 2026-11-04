package com.cia2.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private Long id;
    private String title;
    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String notes;

    public Expense() {}

    public Expense(Long id, String title, String category, BigDecimal amount, LocalDate date, String notes) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
