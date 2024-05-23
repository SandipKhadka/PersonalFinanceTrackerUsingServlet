package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.ExpensesDao;
import org.app.finance.model.Expenses;
import org.app.finance.model.ExpensesCategory;

import java.io.IOException;
import java.util.List;

@WebServlet("/expenses")
public class ExpensesServlet extends HttpServlet {
    // this get method will retrieve income category from database
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ExpensesDao expensesDao = new ExpensesDao();
        List<ExpensesCategory> expensesCategoryList = expensesDao.getAllCategory();
        request.setAttribute("categoryNames", expensesCategoryList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("expenses_form.jsp");
        requestDispatcher.forward(request, response);
    }

    // this method will insert income in database
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        ExpensesDao expensesDao = new ExpensesDao();
        int addedExpenses = Integer.parseInt(request.getParameter("amount"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String remarks = request.getParameter("remarks");
        int totalExpensesAmount = expensesDao.getSumOfExpenses(userName, categoryId);
        int totalSpendLimit = expensesDao.getSumOfSpendLimit(userName, categoryId);
        int leftSpendLimit = totalSpendLimit - totalExpensesAmount;
        if ((totalExpensesAmount + addedExpenses) >= totalSpendLimit) {
            System.out.println(totalExpensesAmount);
            System.out.println(totalSpendLimit);
            System.out.println(leftSpendLimit);
            request.setAttribute("spendLimitError", "You are exceeding your spending limit left amount is" + " " + leftSpendLimit);
            doGet(request, response);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("expenses_form.jsp");
            requestDispatcher.forward(request, response);
            return;
        }
        Expenses expenses = new Expenses();
        expenses.setAmount(addedExpenses);
        expenses.setCategoryId(categoryId);
        expenses.setRemarks(remarks);
        expensesDao.addExpanses(expenses, userName);
        response.sendRedirect("dashboard");
    }
}
