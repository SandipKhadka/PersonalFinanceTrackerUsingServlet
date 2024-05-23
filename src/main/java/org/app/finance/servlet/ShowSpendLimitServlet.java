package org.app.finance.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.ExpensesDao;
import org.app.finance.model.SpendLimit;

import java.io.IOException;
import java.util.List;

@WebServlet("/spendlimit")
public class ShowSpendLimitServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        ExpensesDao expensesDao = new ExpensesDao();
        List<SpendLimit> spendLimitList = expensesDao.getSpendingLimit(userName);
        request.setAttribute("spendLimitList", spendLimitList);
        request.getRequestDispatcher("show_spend_limit.jsp").forward(request, response);
    }
}
