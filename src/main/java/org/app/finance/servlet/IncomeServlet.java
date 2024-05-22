package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.IncomeDao;
import org.app.finance.model.Income;
import org.app.finance.model.IncomeCategory;

import java.io.IOException;
import java.util.List;

@WebServlet("/income")
public class IncomeServlet extends HttpServlet {
    // this get method will retrieve income category from database
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IncomeDao incomeDao = new IncomeDao();
        List<IncomeCategory> incomeCategoryList = incomeDao.getAllCategories();
        request.setAttribute("categoryNames", incomeCategoryList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("income_form.jsp");
        requestDispatcher.forward(request, response);
    }

    // this method will insert income in database
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        IncomeDao incomeDao = new IncomeDao();

        int amout = Integer.parseInt(request.getParameter("amount"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String remarks = request.getParameter("remarks");

        Income income = new Income();
        income.setAmount(amout);
        income.setCategoryId(categoryId);
        income.setRemarks(remarks);
        incomeDao.addIncome(income, userName);
        response.sendRedirect("dashboard");
    }
}
