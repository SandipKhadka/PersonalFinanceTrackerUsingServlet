package org.app.finance.dao;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.Expenses;
import org.app.finance.model.ExpensesCategory;
import org.app.finance.model.GraphData;
import org.app.finance.model.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExpensesDao {
    PreparedStatement preparedStatement;
    Connection connection;
    ResultSet resultSet;
    String sql, remarks;
    int userId, categoryId, amount, year, month;
    String[] dateStrings;

    public void addExpanses(Expenses expenses, String userName) {
        amount = expenses.getAmount();
        categoryId = expenses.getCategoryId();
        remarks = expenses.getRemarks();
        userId = getUserId(userName);
        LocalTime localTime = LocalTime.now();
        Time time = Time.valueOf(localTime);
        sql = "INSERT INTO expenses(expenses_amount,expenses_category,user_id,remarks,date,time) VALUES (?,?,?,?,curdate(),?)";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setInt(3, userId);
            preparedStatement.setString(4, remarks);
            preparedStatement.setTime(5, time);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExpensesCategory(String categoryName, String userName) {
        userId = getUserId(userName);
        sql = "INSERT INTO expenses_category(category_name, user_id) VALUES(?,?)";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException");
        }
    }

    public List<ExpensesCategory> getExpensesCategory(String userName) {
        userId = getUserId(userName);
        sql = "SELECT category_id, category_name FROM expenses_category WHERE user_id=? OR user_id IS NULL";
        List<ExpensesCategory> expensesCategoryList = new ArrayList<ExpensesCategory>();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ExpensesCategory expensesCategory = new ExpensesCategory();
                expensesCategory.setCategoryId(resultSet.getInt(1));
                expensesCategory.setCategoryName(resultSet.getString(2));
                expensesCategoryList.add(expensesCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expensesCategoryList;
    }

    public List<Transaction> getExpensesTransaction(String userName, String filterDate) {
        String sql = "SELECT expenses_amount,category_name,remarks,date,time FROM expenses INNER JOIN expenses_category ON expenses.expenses_category=expenses_category.category_id INNER JOIN user_details ON expenses.user_id = user_details.user_id WHERE user_name =? AND YEAR(date) =? AND MONTH(date) =?";
        dateStrings = filterDate.split("-");
        year = Integer.parseInt(dateStrings[0]);
        month = Integer.parseInt(dateStrings[1]);
        List<Transaction> expensesTransactions = new ArrayList<Transaction>();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setAmount(resultSet.getInt(1));
                transaction.setCategory(resultSet.getString(2));
                transaction.setRemarks(resultSet.getString(3));
                transaction.setDate(resultSet.getDate(4));
                transaction.setTime(resultSet.getTime(5));
                expensesTransactions.add(transaction);
            }
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.getSQLState();
        }
        return expensesTransactions;
    }

    public int getExpensesAmount(String userName) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        userId = getUserId(userName);
        sql = "SELECT SUM(expenses.expenses_amount ) FROM expenses WHERE user_id=? AND YEAR(date) =? AND MONTH(date) =?";
        int expenses = 0;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                expenses = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public int getSumOfExpenses(String userName, int categoryId) {
        int expensesAmount = 0;
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        sql = "SELECT SUM(expenses.expenses_amount) FROM expenses WHERE user_id=? AND expenses_category=? AND year(date)=? AND MONTH(date)=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, month);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                expensesAmount = resultSet.getInt(1);
            }
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("expenses sum");
        }
        return expensesAmount;
    }

    public List<GraphData> getExpensesDataWithAmountAndCategory(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> data = new ArrayList<GraphData>();
        dateStrings = filterDate.split("-");
        year = Integer.parseInt(dateStrings[0]);
        month = Integer.parseInt(dateStrings[1]);
        String sql = "SELECT SUM(expenses.expenses_amount),expenses_category.category_name FROM expenses INNER JOIN expenses_category ON expenses.expenses_category = expenses_category.category_id WHERE expenses.user_id=? AND YEAR(date)=? AND MONTH(date)=? GROUP BY expenses_category.category_id";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GraphData graphTransaction = new GraphData();
                graphTransaction.setAmount(resultSet.getInt(1));
                graphTransaction.setName(resultSet.getString(2));
                data.add(graphTransaction);
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(data);
        }
        return data;
    }

    public List<GraphData> getExpensesByDay(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> expensesByDay = new ArrayList<GraphData>();
        dateStrings = filterDate.split("-");
        year = Integer.parseInt(dateStrings[0]);
        month = Integer.parseInt(dateStrings[1]);
        sql = "SELECT DAY(date),SUM(expenses.expenses_amount) FROM expenses  WHERE user_id=? AND YEAR(date)=? AND MONTH(date)=? GROUP BY expenses.expenses_category ,DAY(date) ";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GraphData graphTransaction = new GraphData();
                graphTransaction.setDay(resultSet.getInt(1));
                graphTransaction.setAmount(resultSet.getInt(2));
                expensesByDay.add(graphTransaction);
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("expenses sum");
        }
        return expensesByDay;
    }

    public List<GraphData> getTopFiveExpensesByCategory(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> graphData = new ArrayList<GraphData>();
        sql = "SELECT SUM(expenses.expenses_amount),expenses_category.category_name FROM expenses INNER JOIN expenses_category ON expenses.expenses_category = expenses_category.category_id WHERE expenses.user_id=? AND YEAR(DATE)=? AND MONTH(DATE)=? GROUP BY expenses.expenses_category ORDER BY SUM(expenses.expenses_amount) DESC LIMIT 5";
        dateStrings = filterDate.split("-");
        year = Integer.parseInt(dateStrings[0]);
        month = Integer.parseInt(dateStrings[1]);
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GraphData graphTransaction = new GraphData();
                graphTransaction.setAmount(resultSet.getInt(1));
                graphTransaction.setName(resultSet.getString(2));
                graphData.add(graphTransaction);
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("top 5");
        }
        return graphData;
    }

    public int getUserId(String userName) {
        try {
            sql = "SELECT user_id FROM user_details WHERE user_name=? ";
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
