package com.songjin.expensetracker;

import com.songjin.expensetracker.Expense;

public class ExpenseClickEvent {
    private Expense expense;

    public ExpenseClickEvent(Expense expense) {
        this.expense = expense;
    }

    public Expense getExpense() {
        return expense;
    }
}
