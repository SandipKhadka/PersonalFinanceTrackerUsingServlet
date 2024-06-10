package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.ExpensesDao;
import org.app.finance.dao.SpendingLimitDao;
import org.app.finance.model.ExpensesCategory;
import org.app.finance.model.SpendLimit;

import java.io.IOException;
import java.util.List;

@WebServlet("/spendlimit")
public class SpendlimitServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        ExpensesDao expensesDao = new ExpensesDao();
        List<ExpensesCategory> expensesCategoryList = expensesDao.getExpensesCategory(userName);
        request.setAttribute("categoryNames", expensesCategoryList);
        System.out.println(expensesCategoryList);

        SpendingLimitDao spendingLimitDao = new SpendingLimitDao();
        List<SpendLimit> spendLimitList = spendingLimitDao.getSpendingLimit(userName);
        request.setAttribute("spendLimitList", spendLimitList);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("spend_limit.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int amount = Integer.parseInt(request.getParameter("amount"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        SpendLimit spendLimit = new SpendLimit();
        spendLimit.setAmount(amount);
        spendLimit.setCategoryId(categoryId);
        SpendingLimitDao spendingLimitDao = new SpendingLimitDao();
        spendingLimitDao.addOrUpdateSpendingLimit(userName, spendLimit);
        response.sendRedirect("spendlimit");
    }

}
