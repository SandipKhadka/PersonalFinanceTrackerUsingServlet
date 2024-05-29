package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.UserDao;
import org.app.finance.model.Login;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Login userLoginDetails = new Login();
        userLoginDetails.setUserName(userName);
        userLoginDetails.setPassword(password);
        UserDao loginUserDao = new UserDao();
        try {
            if (loginUserDao.isUserNameAvailable(userName)) {
                request.setAttribute("userNameError", "The username does not exist");
                RequestDispatcher loginPageDispatcher = request.getRequestDispatcher("login.jsp");
                loginPageDispatcher.forward(request, response);
            }
            long hashedPassword = userLoginDetails.hashedPassword();
            if (!loginUserDao.isPasswordCorrect(userName, hashedPassword)) {
                request.setAttribute("passwordError", "The password is not incorrect");
                RequestDispatcher loginPageDispatcher = request.getRequestDispatcher("login.jsp");
                loginPageDispatcher.forward(request, response);
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", userName);
            loginUserDao.loginUser(userLoginDetails);
            response.sendRedirect("dashboard");
        } catch (Exception e) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
            e.printStackTrace();
        }
    }
}
