package com.company;

public class User {
    public String name;
    public String email;
    public String userName;
    public int expenseAmount;

    public void incrementExpenseAmount(int amount){
        expenseAmount += amount;
    }

}
