package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.ExpensesDao;
import org.app.finance.model.ExpensesCategory;
import org.app.finance.model.SpendLimit;

import java.io.IOException;
import java.util.List;

@WebServlet("/addspendlimit")
public class AddSpendingLimitServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        ExpensesDao expensesDao = new ExpensesDao();
        List<ExpensesCategory> expensesCategoryList = expensesDao.getAllCategory();
        request.setAttribute("categoryNames", expensesCategoryList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("spending_limit_form.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException {
        int amount = Integer.parseInt(request.getParameter("amount"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        HttpSession session = request.getSession(false);
        String userName =(String) session.getAttribute("user");
        SpendLimit spendLimit = new SpendLimit();
        spendLimit.setAmount(amount);
        spendLimit.setCategoryId(categoryId);
        ExpensesDao expensesDao = new ExpensesDao();
        expensesDao.addSpendingLimit(userName,spendLimit);
        response.sendRedirect("dashboard");
    }
}