package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.ExpensesDao;
import org.app.finance.dao.IncomeDao;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IncomeDao incomeDao = new IncomeDao();
        ExpensesDao expensesDao = new ExpensesDao();
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        int income = incomeDao.getIncomeAmount(userName);
        request.setAttribute("income", income);
        int expenses = expensesDao.getExpensesAmount(userName);
        request.setAttribute("expenses", expenses);
        int netIncome = income - expenses;
        request.setAttribute("netIncome", netIncome);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user_dashboard.jsp");
        requestDispatcher.forward(request, response);
    }
}
