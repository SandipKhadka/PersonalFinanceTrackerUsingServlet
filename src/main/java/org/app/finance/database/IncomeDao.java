package org.app.finance.database;

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
    int startMonth, startYear, endMonth, endYear;
    String[] startDateString, endDateString;

    public void addIncome(Income income, String userName) {
        amount = income.getAmount();
        categoryId = income.getCategoryId();
        remarks = income.getRemarks();
        userId = getUserId(userName);
        LocalTime localTime = LocalTime.now();
        Time sqlTime = Time.valueOf(localTime);
        try {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO income(income_amount, income_category, user_id, remarks, date, time) " +
                    "VALUES(?,?,?,?,CURDATE(),?)";
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
        sql = "INSERT INTO income_category(category_name, user_id) " +
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

    public List<IncomeCategory> getIncomeCategory(String userName) {
        userId = getUserId(userName);
        sql = "SELECT " +
                "category_id,category_name " +
                "FROM income_category " +
                "WHERE user_id=? " +
                "OR user_id IS NULL";
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

    public void deleteIncomeRecord(String userName, int incomeId) {
        userId = getUserId(userName);
        sql = "DELETE FROM income " +
                "WHERE user_id=? AND income_id=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, incomeId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("delete record");
        }
    }

    public void updateIncomeRecord(Income income, String userName, int incomeId) {
        userId = getUserId(userName);
        int amount = income.getAmount();
        String remarks = income.getRemarks();
        int categoryId = income.getCategoryId();
        System.out.println("category id from dao layer" + categoryId);
        sql = "UPDATE income" +
                " SET income_amount=? , " +
                "income_category=? ," +
                "remarks=?" +
                "WHERE user_id=? AND income_id=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setString(3, remarks);
            preparedStatement.setInt(4, userId);
            preparedStatement.setInt(5, incomeId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("update record");
        }
    }

    public List<Transaction> getIncomeTransaction(String userName, String startFilterDate, String endFilterDAte) {
        userId = getUserId(userName);
        String sql = "SELECT " +
                "income_id, income_amount," +
                "category_name,remarks" +
                ",date," +
                "time , " +
                "income.income_category " +
                "FROM income " +
                "INNER JOIN income_category " +
                "ON income.income_category=income_category.category_id " +
                "WHERE (income.user_id =? AND YEAR(date) >=? AND MONTH(date) >=? AND YEAR(date) <=? AND MONTH(date) <=?)" +
                "OR (income.user_id=? AND YEAR(date)=? AND MONTH(date)=?)";
        List<Transaction> incomeTransactions = new ArrayList<Transaction>();
        startDateString = startFilterDate.split("-");
        startYear = Integer.parseInt(startDateString[0]);
        startMonth = Integer.parseInt(startDateString[1]);

        if (endFilterDAte != null && !endFilterDAte.isEmpty()) {
            endDateString = endFilterDAte.split("-");
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
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt(1));
                transaction.setAmount(resultSet.getInt(2));
                transaction.setCategory(resultSet.getString(3));
                transaction.setRemarks(resultSet.getString(4));
                transaction.setDate(resultSet.getDate(5));
                transaction.setTime(resultSet.getTime(6));
                transaction.setCategoryId(resultSet.getInt(7));
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
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        String sql = "SELECT " +
                "SUM(income_amount ) " +
                "FROM income " +
                " WHERE user_id=? AND YEAR(date) =? AND MONTH(date) =?";
        int income = 0;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, month);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                income = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

    public List<GraphData> getIncomesDataWithAmountAndCategory(String userName, String startFilterDate, String endFilterDate) {
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
                "SUM(income.income_amount)," +
                "income_category.category_name " +
                "FROM income" +
                " INNER JOIN income_category ON " +
                "income.income_category = income_category.category_id" +
                " WHERE (income.user_id=? AND YEAR(date)>=? AND MONTH(date)>=? AND YEAR(date)<=? AND MONTH(date)<=?)" +
                "OR (income.user_id=? AND YEAR(date)=? AND MONTH(date)=?) " +
                "GROUP BY income_category.category_id";
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

    public List<GraphData> getIncomeByDay(String userName, String startFilterDate, String endFilterDate) {
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
                "SUM(income.income_amount) " +
                "FROM income  " +
                "WHERE (user_id=? AND YEAR(date)>=? AND MONTH(date)<=? AND YEAR(date)<=? AND MONTH(date)<=?)" +
                "OR (user_id=? AND YEAR(date)=? AND MONTH(date)=?) " +
                "GROUP BY DAY(date) ";
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

    public List<GraphData> getTopFiveIncomeByCategory(String userName, String startFilterDate, String endFilterDate) {
        userId = getUserId(userName);
        List<GraphData> graphData = new ArrayList<GraphData>();
        sql = "SELECT " +
                "SUM(income.income_amount)," +
                "income_category.category_name " +
                "FROM income " +
                "INNER JOIN income_category " +
                "ON income.income_category = income_category.category_id" +
                " WHERE( income.user_id=? AND YEAR(DATE)>=? AND MONTH(DATE)>=? AND YEAR(date)<=? AND MONTH(date)<=?)" +
                "OR (income.user_id=? AND YEAR(date)=? AND MONTH(date)=?)" +
                " GROUP BY income.income_category" +
                " ORDER BY SUM(income.income_amount) DESC LIMIT 5";
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
        sql = "SELECT " +
                "user_id " +
                "FROM user_details" +
                " WHERE user_name =?";
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
