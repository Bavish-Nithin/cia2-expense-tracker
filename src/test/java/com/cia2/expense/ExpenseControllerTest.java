package com.cia2.expense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void listExpensesReturnsSeedData() throws Exception {
        mockMvc.perform(get("/api/expenses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void createExpenseReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"Coffee\",\"category\":\"Food\",\"amount\":120.50,\"date\":\"2026-11-04\",\"notes\":\"Test entry\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Coffee"))
            .andExpect(jsonPath("$.amount").value(120.50));
    }

    @Test
    void summaryContainsTotals() throws Exception {
        mockMvc.perform(get("/api/summary"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSpent").exists())
            .andExpect(jsonPath("$.categoryTotals").isArray());
    }

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }
}
