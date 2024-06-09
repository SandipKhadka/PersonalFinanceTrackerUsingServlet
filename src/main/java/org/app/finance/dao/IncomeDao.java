package org.app.finance.dao;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.GraphData;
import org.app.finance.model.Income;
import org.app.finance.model.IncomeCategory;
import org.app.finance.model.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class IncomeDao {
    int amount, categoryId, userId;
    String remarks, sql;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int month, year;
    String[] daStrings;

    public void addIncome(Income income, String userName) {
        amount = income.getAmount();
        categoryId = income.getCategoryId();
        remarks = income.getRemarks();
        userId = getUserId(userName);
        LocalTime localTime = LocalTime.now();
        Time sqlTime = Time.valueOf(localTime);
        try {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO income(income_amount, income_category, user_id, remarks, date, time) VALUES(?,?,?,?,CURDATE(),?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setInt(3, userId);
            preparedStatement.setString(4, remarks);
            preparedStatement.setTime(5, sqlTime);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIncomeCategory(String categoryName, String userName) {
        userId = getUserId(userName);
        sql = "INSERT INTO income_category(category_name, user_id) VALUES(?,?)";
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

    public List<IncomeCategory> getIncomeCategory(String userName) {
        userId = getUserId(userName);
        sql = "SELECT category_id,category_name FROM income_category WHERE user_id=? OR user_id IS NULL";
        List<IncomeCategory> incomeCategoryList = new ArrayList<IncomeCategory>();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IncomeCategory incomeCategory = new IncomeCategory();
                incomeCategory.setCategoryId(resultSet.getInt(1));
                incomeCategory.setCategoryName(resultSet.getString(2));
                incomeCategoryList.add(incomeCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomeCategoryList;
    }

    public List<Transaction> getIncomeTransaction(String userName, String filterDate) {
        String sql = "SELECT income_amount,category_name,remarks,date,time FROM income INNER JOIN income_category ON income.income_category=income_category.category_id INNER JOIN user_details ON income.user_id = user_details.user_id WHERE user_name =? AND YEAR(date) =? AND MONTH(date) =?";
        List<Transaction> incomeTransactions = new ArrayList<Transaction>();
        daStrings = filterDate.split("-");
        year = Integer.parseInt(daStrings[0]);
        month = Integer.parseInt(daStrings[1]);
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
                incomeTransactions.add(transaction);
            }
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.getSQLState();
        }
        return incomeTransactions;
    }

    public int getIncomeAmount(String userName) {
        String sql = "SELECT SUM(income.income_amount ) FROM income INNER JOIN user_details ON income.user_id = user_details.user_id WHERE user_details.user_name =?";
        int income = 0;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                income = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

    public List<GraphData> getIncomesDataWithAmountAndCategory(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> data = new ArrayList<GraphData>();
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        String sql = "SELECT SUM(income.income_amount),income_category.category_name FROM income INNER JOIN income_category ON income.income_category = income_category.category_id WHERE income.user_id=? AND YEAR(date)=? AND MONTH(date)=? GROUP BY income_category.category_id";
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

    public List<GraphData> getIncomeByDay(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> expensesByDay = new ArrayList<GraphData>();
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        sql = "SELECT DAY(date),SUM(income.income_amount) FROM income  WHERE user_id=? AND YEAR(date)=? AND MONTH(date)=? GROUP BY income.income_category ,DAY(date) ";
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

    public List<GraphData> getTopFiveIncomeByCategory(String userName, String filterDate) {
        userId = getUserId(userName);
        List<GraphData> graphData = new ArrayList<GraphData>();
        sql = "SELECT SUM(income.income_amount),income_category.category_name FROM income INNER JOIN income_category ON income.income_category = income_category.category_id WHERE income.user_id=? AND YEAR(DATE)=? AND MONTH(DATE)=? GROUP BY income.income_category ORDER BY SUM(income.income_amount) DESC LIMIT 5";
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
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
        sql = "SELECT user_id FROM user_details WHERE user_name =?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
