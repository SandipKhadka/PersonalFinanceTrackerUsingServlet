package org.app.finance.dao;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.Expenses;
import org.app.finance.model.ExpensesCategory;
import org.app.finance.model.SpendLimit;

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
    int userId, categoryId, amount;

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

    public List<ExpensesCategory> getAllCategory() {
        sql = "SELECT category_id, category_name FROM expenses_category";
        List<ExpensesCategory> expensesCategoryList = new ArrayList<ExpensesCategory>();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
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

    public int getExpensesAmount(String userName) {
        userId = getUserId(userName);
        sql = "SELECT SUM(expenses.expenses_amount ) FROM expenses WHERE user_id=?";
        int expenses = 0;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                expenses = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
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

    public void addSpendingLimit(String userName, SpendLimit spendLimit) {
        userId = getUserId(userName);
        categoryId = spendLimit.getCategoryId();
        amount = spendLimit.getAmount();
        sql = "INSERT INTO spending_limit(category_id, user_id, amount,date) values (?,?,?,CURDATE())";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, amount);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SpendLimit> getSpendingLimit(String userName) {
        userId = getUserId(userName);
        List<SpendLimit> spendLimitList = new ArrayList<SpendLimit>();
        LocalDate localDate = LocalDate.now();
        String[] splitDate = localDate.toString().split("-");
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        sql = "SELECT  spending_limit.amount,expenses_category.category_name FROM spending_limit INNER JOIN expenses_category ON spending_limit.category_id = expenses_category.category_id WHERE user_id=? AND YEAR(date)=? AND MONTH(date)=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SpendLimit spendLimit = new SpendLimit();
                spendLimit.setAmount(resultSet.getInt(1));
                spendLimit.setCategoryName(resultSet.getString(2));
                spendLimitList.add(spendLimit);
            }
            connection.close();
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spendLimitList;
    }

    public int getSumOfExpenses(String userName, int categoryId) {
        int expensesAmount = 0;
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        String[] splitDate = localDate.toString().split("-");
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
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

    public int getSumOfSpendLimit(String userName, int categoryId) {
        int spendLimit = 0;
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        String[] splitDate = localDate.toString().split("-");
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        sql = "SELECT SUM(spending_limit.amount) FROM spending_limit WHERE user_id=? AND category_id=? AND year(date)=? AND MONTH(date)=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, month);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                spendLimit = resultSet.getInt(1);
            }
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("spend limit");
        }
        return spendLimit;
    }
}

