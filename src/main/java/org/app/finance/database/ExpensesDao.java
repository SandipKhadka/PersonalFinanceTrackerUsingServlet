package org.app.finance.database;

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
    int userId, categoryId, amount, startYear, startMonth, endYear, endMonth;
    String[] startDateString, endDateString;

    public void addExpanses(Expenses expenses, String userName) {
        amount = expenses.getAmount();
        categoryId = expenses.getCategoryId();
        remarks = expenses.getRemarks();
        userId = getUserId(userName);
        LocalTime localTime = LocalTime.now();
        Time time = Time.valueOf(localTime);
        sql = "INSERT INTO expenses(expenses_amount,expenses_category,user_id,remarks,date,time) " +
                "VALUES (?,?,?,?,curdate(),?)";
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
        sql = "INSERT INTO expenses_category(category_name, user_id) " +
                "VALUES(?,?)";
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
        sql = "SELECT " +
                "category_id," +
                " category_name " +
                "FROM expenses_category " +
                "WHERE user_id=? " +
                "OR user_id IS NULL";
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

    public void deleteExpensesRecord(String userName, int expensesId) {
        userId = getUserId(userName);
        sql = "DELETE FROM expenses " +
                "WHERE user_id=? AND expenses_id=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, expensesId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("delete record");
        }
    }

    public void updateRecord(Expenses expenses, String userName, int expensesId) {
        userId = getUserId(userName);
        int amount = expenses.getAmount();
        String remarks = expenses.getRemarks();
        int categoryId = expenses.getCategoryId();
        System.out.println("category id from dao layer" + categoryId);
        sql = "UPDATE expenses" +
                " SET expenses_amount=? , " +
                "expenses_category=? ," +
                "remarks=?" +
                "WHERE user_id=? AND expenses_id=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setString(3, remarks);
            preparedStatement.setInt(4, userId);
            preparedStatement.setInt(5, expensesId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("update record");
        }
    }

    public int getOriginalAmount(String userName, int categoryId, int expensesId) {
        userId = getUserId(userName);
        sql = "SELECT " +
                "expenses_amount " +
                "FROM expenses" +
                " WHERE user_id=? AND expenses_category=?";
        int expensesAmountBeforeUpdate = 0;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                expensesAmountBeforeUpdate = resultSet.getInt("expenses_amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("before update");
        }
        return expensesAmountBeforeUpdate;
    }

    public List<Transaction> getExpensesTransaction(String userName, String startFilterDsate, String endFilterDate) {
        userId = getUserId(userName);
        String sql = "SELECT " +
                "expenses.expenses_id, " +
                "expenses.expenses_amount," +
                "expenses_category.category_name," +
                "remarks," +
                "date," +
                "time ," +
                "expenses.expenses_category " +
                "FROM expenses" +
                " INNER JOIN expenses_category " +
                "ON expenses.expenses_category=expenses_category.category_id " +
                "WHERE( expenses.user_id =? " +
                "AND YEAR(date) >=? AND MONTH(date) >=?  AND YEAR(date) <=? AND MONTH(date) <=?)" +
                "OR (expenses.user_id=? AND  YEAR(date)=? AND MONTH(date)=?)";
        startDateString = startFilterDsate.split("-");
        startYear = Integer.parseInt(startDateString[0]);
        startMonth = Integer.parseInt(startDateString[1]);
        if (endFilterDate != null && !endFilterDate.isEmpty()) {
            endDateString = endFilterDate.split("-");
            endYear = Integer.parseInt(endDateString[0]);
            endMonth = Integer.parseInt(endDateString[1]);

        }
        List<Transaction> expensesTransactions = new ArrayList<Transaction>();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startYear);
            preparedStatement.setInt(3, startMonth);
            preparedStatement.setInt(4, endYear);
            preparedStatement.setInt(5, endMonth);
            preparedStatement.setInt(6, userId);
            preparedStatement.setInt(7, startYear);
            preparedStatement.setInt(8, startMonth);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt(1));
                transaction.setAmount(resultSet.getInt(2));
                transaction.setCategory(resultSet.getString(3));
                transaction.setRemarks(resultSet.getString(4));
                transaction.setDate(resultSet.getDate(5));
                transaction.setTime(resultSet.getTime(6));
                transaction.setCategoryId(resultSet.getInt(7));
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
        sql = "SELECT " +
                "SUM(expenses.expenses_amount ) " +
                "FROM expenses " +
                "WHERE user_id=? AND YEAR(date) =? AND MONTH(date) =?";
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

    // this method is for checking spending limit
    public int getSumOfExpenses(String userName, int categoryId) {
        int expensesAmount = 0;
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        sql = "SELECT " +
                "SUM(expenses.expenses_amount) " +
                "FROM expenses " +
                "WHERE user_id=? AND expenses_category=? AND year(date)=? AND MONTH(date)=?";
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

    public List<GraphData> getExpensesDataWithAmountAndCategory(String userName, String startFilterDate, String endFilterDate) {
        userId = getUserId(userName);
        List<GraphData> data = new ArrayList<GraphData>();
        startDateString = startFilterDate.split("-");
        startYear = Integer.parseInt(startDateString[0]);
        startMonth = Integer.parseInt(startDateString[1]);

        if (endFilterDate != null && !endFilterDate.isEmpty()) {
            endDateString = endFilterDate.split("-");
            endYear = Integer.parseInt(endDateString[0]);
            endMonth = Integer.parseInt(endDateString[1]);
        }
        String sql = "SELECT " +
                "SUM(expenses.expenses_amount)," +
                "expenses_category.category_name " +
                "FROM expenses " +
                "INNER JOIN expenses_category " +
                "ON expenses.expenses_category = expenses_category.category_id " +
                "WHERE (expenses.user_id=? AND YEAR(date)>=? AND MONTH(date)>=? AND YEAR(date)<=? AND MONTH(date)<+?)" +
                "OR (expenses.user_id=? AND YEAR(date)=? AND MONTH(date)=?)" +
                " GROUP BY expenses_category.category_id";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startYear);
            preparedStatement.setInt(3, startMonth);
            preparedStatement.setInt(4, endYear);
            preparedStatement.setInt(5, endMonth);
            preparedStatement.setInt(6, userId);
            preparedStatement.setInt(7, startYear);
            preparedStatement.setInt(8, startMonth);
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

    public List<GraphData> getExpensesByDay(String userName, String startFilterDate, String endFilterDate) {
        userId = getUserId(userName);
        List<GraphData> expensesByDay = new ArrayList<GraphData>();
        startDateString = startFilterDate.split("-");
        startYear = Integer.parseInt(startDateString[0]);
        startMonth = Integer.parseInt(startDateString[1]);

        if (endFilterDate != null && !endFilterDate.isEmpty()) {
            endDateString = endFilterDate.split("-");
            endYear = Integer.parseInt(endDateString[0]);
            endMonth = Integer.parseInt(endDateString[1]);
        }
        sql = "SELECT " +
                "DAY(date)," +
                "SUM(expenses.expenses_amount) " +
                "FROM expenses " +
                " WHERE (user_id=? AND YEAR(date)>=? AND MONTH(date)>=? AND YEAR(date)<=? AND MONTH(date)<=?)" +
                "OR (user_id=? AND YEAR(date)=? AND MONTH(date)=?)" +
                " GROUP BY DAY(date) ";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startYear);
            preparedStatement.setInt(3, startMonth);
            preparedStatement.setInt(4, endYear);
            preparedStatement.setInt(5, endMonth);
            preparedStatement.setInt(6, userId);
            preparedStatement.setInt(7, startYear);
            preparedStatement.setInt(8, startMonth);
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

    public List<GraphData> getTopFiveExpensesByCategory(String userName, String startFilterDate, String endFilterDate) {
        userId = getUserId(userName);
        List<GraphData> graphData = new ArrayList<GraphData>();
        sql = "SELECT " +
                "SUM(expenses.expenses_amount)," +
                "expenses_category.category_name " +
                "FROM expenses " +
                "INNER JOIN expenses_category " +
                "ON expenses.expenses_category = expenses_category.category_id " +
                "WHERE (expenses.user_id=? AND YEAR(DATE)>=? AND MONTH(DATE)>=? AND YEAR(date)<=? AND MONTH(date)<=?)" +
                "OR (expenses.user_id=? AND YEAR(date)=? AND MONTH(date)=?)" +
                "GROUP BY expenses.expenses_category " +
                "ORDER BY SUM(expenses.expenses_amount) DESC LIMIT 5";
        startDateString = startFilterDate.split("-");
        startYear = Integer.parseInt(startDateString[0]);
        startMonth = Integer.parseInt(startDateString[1]);

        if (endFilterDate != null && !endFilterDate.isEmpty()) {
            endDateString = endFilterDate.split("-");
            endYear = Integer.parseInt(endDateString[0]);
            endMonth = Integer.parseInt(endDateString[1]);
        }
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startYear);
            preparedStatement.setInt(3, startMonth);
            preparedStatement.setInt(4, endYear);
            preparedStatement.setInt(5, endMonth);
            preparedStatement.setInt(6, userId);
            preparedStatement.setInt(7, startYear);
            preparedStatement.setInt(8, startMonth);
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
            sql = "SELECT user_id " +
                    "FROM user_details " +
                    "WHERE user_name=? ";
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
