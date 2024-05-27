package org.app.finance.dao;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TransactionDao {
    String sql;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int year, month;
    String[] date;


    public List<Transaction> getIncomeTransaction(String userName, String filterDate) {
        String sql = "SELECT income_amount,category_name,remarks,date,time FROM income INNER JOIN income_category ON income.income_category=income_category.category_id INNER JOIN user_details ON income.user_id = user_details.user_id WHERE user_name =? AND YEAR(date) =? AND MONTH(date) =?";
        List<Transaction> incomeTransactions = new ArrayList<Transaction>();
        date = filterDate.split("-");
        year = Integer.parseInt(date[0]);
        month = Integer.parseInt(date[1]);
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


    public List<Transaction> getExpensesByCategoryNameAndAmount(String userName, String filterDate) {
        String sql = "SELECT expenses_amount,category_name,remarks,date,time FROM expenses INNER JOIN expenses_category ON expenses.expenses_category=expenses_category.category_id INNER JOIN user_details ON expenses.user_id = user_details.user_id WHERE user_name =? AND YEAR(date) =? AND MONTH(date) =?";
        date = filterDate.split("-");
        year = Integer.parseInt(date[0]);
        month = Integer.parseInt(date[1]);
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

}
