package com.songjin.expensetracker.event;

import com.songjin.expensetracker.data.Expense;

public class ExpenseClickEvent {
    private Expense expense;

    public ExpenseClickEvent(Expense expense) {
        this.expense = expense;
    }

    public Expense getExpense() {
        return expense;
    }
}
