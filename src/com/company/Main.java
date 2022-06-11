package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Creating users
        System.out.println("Welcome Budget Planner");
        System.out.println("How many people will the budget split?");
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> userList = prepareUserList(scanner);
        System.out.println("Added user count: " + userList.size());

        // Setting the budget
        System.out.println("set a limit for the budget so we can warn you when you reach the limit: ");
        int limit = scanner.nextInt();

        ArrayList<Expense> expenseList = new ArrayList<>();

        // Creating options - 0: Make Expense", 1: "List Specific Persons Expense", 2: "List All Expenses", 3: "Make Split", 4: "Close budget"
        String[] optionList= prepareOptionList();
        while (true){
            System.out.println("What would you like to do:");
            for (int i=0;i<optionList.length; i ++){
                System.out.println(optionList[i] + ":  " + i);
            }
            int request = scanner.nextInt();
            switch (request){
                case 0:
                    Expense expense = new Expense();
                    System.out.println("Expense Name: ");
                    expense.expenseName = scanner.next();
                    System.out.println("Expense Amount ");
                    expense.amount = scanner.nextInt();
                    System.out.println("Which user make this expense ? Just type user id: ");
                    for (User user : userList){
                        System.out.println("id: " + userList.indexOf(user) + " Name: " + user.name);
                    }
                    int userId = scanner.nextInt();
                    // is validation necessary ?
                    User selectedUser =userList.get(userId);
                    expense.userName = selectedUser.userName;
                    expenseList.add(expense);
                    selectedUser.incrementExpenseAmount(expense.amount);
                    if(isLimitBreached(limit, expenseList)){
                        System.out.println("You have reached the limit!!");
                    }

                    break;
                case 1:
                    System.out.println("Please provide userName that you would like to search ");
                    String userName = scanner.next();
                    User user = null;
                    for (User u : userList){
                        if (u.userName.equals(userName)) {
                            user = u;
                            break;
                        }
                    }
                    if (user == null){
                        System.out.println("User couldn't find please try again.");
                        break;
                    }
                    System.out.println(user.userName + " has made " + user.expenseAmount + " expense. Expense detail can be found below.");
                    for (int i = 0; i<expenseList.size() ; i++){
                        if (expenseList.get(i).userName.equals(userName)){
                            System.out.println(i + " - expense amount: " + expenseList.get(i).amount + " , expensed by : " + expenseList.get(i).userName);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i<expenseList.size() ; i++){
                        System.out.println(i + " - expense amount: " + expenseList.get(i).amount + " , expensed by : " + expenseList.get(i).userName);
                    }
                    break;
                case 3:
                    double totalAmount = 0;
                    ArrayList<Split> splitList = calculateSplitByUser(expenseList);
                    for (Split split : splitList){
                        totalAmount += split.amount;
                    }
                    makeSplit(totalAmount, splitList);
                    break;
                case 4:
                    for(User eachUser: userList) {
                        System.out.println("id: " + userList.indexOf(eachUser) + " Name: " + eachUser.name);
                    }
                    break;
                case 5:
                    System.exit(1);
            }
        }
    }
    public static String[] prepareOptionList(){
        return new String[]{"Make Expense", "List Specific Persons Expense", "List All Expenses", "Make Split", "List All Users", "Close budget"};
    }

    public static boolean isLimitBreached(int limit, ArrayList<Expense> expenseList){
        int expenseAmount = 0;
        for (Expense expense : expenseList){
            expenseAmount += expense.amount;
            if (expenseAmount >= limit){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<User> prepareUserList(Scanner scanner){
        int userCount = scanner.nextInt();

        ArrayList<User> userList = new ArrayList<>();

        for (int i =0 ; i< userCount; i++){
            User user = new User();
            System.out.println("Name: ");
            user.name = scanner.next();

            System.out.println("Email: ");
            user.email = scanner.next();

            System.out.println("UserName: ");
            user.userName = scanner.next();
            user.expenseAmount = 0;
            userList.add(user);
        }
        System.out.println("Would you like set a limit for the budget?");
        return userList;
    }

    public static ArrayList<Split> calculateSplitByUser(ArrayList<Expense> expenseList){
        ArrayList<Split> splitList = new ArrayList<>();

        for (Expense expense : expenseList){
            Split split = existInSplitList(expense.userName, splitList);
            if (split != null){
                split.amount += expense.amount;
            }else {
                Split willBeAdded = new Split();
                willBeAdded.userName = expense.userName;
                willBeAdded.amount = expense.amount;
                splitList.add(willBeAdded);
            }
        }
        return splitList;
    }
    public static Split existInSplitList(String userName, ArrayList<Split> splitList){
        for (Split split : splitList){
            if (split.userName.equals(userName)){
                return split;
            }
        }
        return null;
    }

    // wrapper class
    public static void makeSplit(Double totalAmount, ArrayList<Split> splitList){
        double amount = (totalAmount / splitList.size());
        for (Split split : splitList){
            if (split.amount > amount){
                System.out.println(split.userName + " needs to take back " + (split.amount - amount));
            }else {
                System.out.println(split.userName + " needs give " + (-1 * (split.amount - amount)));
            }
        }
    }
}
