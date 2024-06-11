package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.database.UserDao;
import org.app.finance.model.Register;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Register userRegisterDetails = new Register();
        UserDao registerUserDao = new UserDao();
        userRegisterDetails.setFirstName(firstName);
        userRegisterDetails.setLastName(lastName);
        userRegisterDetails.setUserName(userName);
        userRegisterDetails.setPassword(password);
        try {
            if (!registerUserDao.isUserNameAvailable(userName)) {
                request.setAttribute("userNameError", "User name is already taken");
                RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
                rd.forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", userName);
            registerUserDao.registerUser(userRegisterDetails);
            response.sendRedirect("user_dashboard.jsp");
        } catch (Exception e) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
            e.printStackTrace();
        }
    }
}
