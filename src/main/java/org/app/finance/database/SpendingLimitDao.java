package org.app.finance.database;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.SpendLimit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpendingLimitDao {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int userId, categoryId, amount;
    String sql;
    int year, month;

    public void addSpendingLimit(String userName, SpendLimit spendLimit) {
        userId = getUserId(userName);
        categoryId = spendLimit.getCategoryId();
        amount = spendLimit.getAmount();
        sql = "INSERT INTO spending_limit(category_id, user_id, amount,date) " +
                "VALUES (?,?,?,CURDATE())";
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
            System.out.println("adding");
        }
    }

    public List<SpendLimit> getSpendingLimit(String userName) {
        userId = getUserId(userName);
        List<SpendLimit> spendLimitList = new ArrayList<SpendLimit>();
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();
        sql = "SELECT " +
                "spending_limit." +
                "amount," +
                "expenses_category.category_name" +
                " FROM spending_limit" +
                " INNER JOIN expenses_category " +
                "ON spending_limit.category_id = expenses_category.category_id " +
                "WHERE spending_limit.user_id=? AND YEAR(date)=? AND MONTH(date)=?";
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
            System.out.println("getSpendingLimit");
        }
        return spendLimitList;
    }

    public int getSumOfSpendLimit(String userName, int categoryId) {
        int spendLimit = 0;
        userId = getUserId(userName);
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();
        sql = "SELECT " +
                "SUM(spending_limit.amount) " +
                "FROM spending_limit" +
                " WHERE user_id=? AND category_id=? AND year(date)=? AND MONTH(date)=?";
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

    public void updateSpendLimit(String userName, SpendLimit spendLimit) {
        userId = getUserId(userName);
        sql = "UPDATE spending_limit" +
                " SET amount=? " +
                "WHERE user_id=? AND category_id=? AND YEAR(date)=? AND MONTH(date)=? ";
        amount = spendLimit.getAmount();
        categoryId = spendLimit.getCategoryId();
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.setInt(4, year);
            preparedStatement.setInt(5, month);
            preparedStatement.execute();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(amount);
            System.out.println(userId);
            System.out.println(categoryId);
            System.out.println(year);
            System.out.println(month);
        }
    }

    public boolean isCategorySpendingLimitExisted(String userName, SpendLimit spendLimit) {
        userId = getUserId(userName);
        categoryId = spendLimit.getCategoryId();
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();
        sql = "SELECT id " +
                "FROM spending_limit" +
                " WHERE user_id=? AND category_id=? AND YEAR(date)=? AND MONTH(date)=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, month);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("isCategorySpendingLimitExisted");
        }
        return false;
    }

    public void addOrUpdateSpendingLimit(String userName, SpendLimit spendLimit) {
        if (isCategorySpendingLimitExisted(userName, spendLimit)) {
            updateSpendLimit(userName, spendLimit);
            return;
        }
        addSpendingLimit(userName, spendLimit);
    }

    public int getUserId(String userName) {
        sql = "SELECT " +
                "user_id " +
                "FROM user_details" +
                " WHERE user_name=? ";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
