package org.app.finance.database;

import org.app.finance.config.DatabaseConnection;
import org.app.finance.model.Login;
import org.app.finance.model.Register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String firstName, lastName, userName, sql;
    long hashedPassword;

    public boolean registerUser(Register user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        userName = user.getUserName();
        hashedPassword = user.hashedPassword();
        int insertStatus = 0;
        sql = "INSERT INTO user_details(first_name,last_name,user_name,password)" +
                " VALUES(?,?,?,?)";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, userName);
            preparedStatement.setLong(4, hashedPassword);
            insertStatus = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insertStatus == 1;
    }

    public boolean loginUser(Login user) {
        userName = user.getUserName();
        hashedPassword = user.hashedPassword();
        return isPasswordCorrect(userName, hashedPassword);

    }

    public boolean isUserNameAvailable(String userName) {
        int userNameStatus = 0;
        sql = "SELECT COUNT(user_id)" +
                " FROM user_details" +
                " WHERE user_name =?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userNameStatus = resultSet.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userNameStatus != 1;
    }

    public boolean isPasswordCorrect(String userName, long password) {
        int passwordStatus = 0;
        sql = "SELECT COUNT(user_id)" +
                " FROM user_details" +
                " WHERE user_name =? AND password=?";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setLong(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                passwordStatus = resultSet.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordStatus == 1;
    }
}
