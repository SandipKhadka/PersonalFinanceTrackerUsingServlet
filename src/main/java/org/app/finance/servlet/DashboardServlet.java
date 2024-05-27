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
import org.app.finance.model.GraphData;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");

        IncomeDao incomeDao = new IncomeDao();
        int income = incomeDao.getIncomeAmount(userName);
        request.setAttribute("income", income);

        ExpensesDao expensesDao = new ExpensesDao();
        int expenses = expensesDao.getExpensesAmount(userName);
        request.setAttribute("expenses", expenses);

        int netIncome = income - expenses;
        request.setAttribute("netIncome", netIncome);

        List<GraphData> transactions = expensesDao.getExpensesDataForGraph(userName);
        request.setAttribute("transactions", transactions);
        transactions.forEach(System.out::println);

        List<GraphData> graphDataByDay = expensesDao.getExpensesByDay(userName);
        request.setAttribute("expensesByDay", graphDataByDay);

        List<GraphData> topFiveCategory = expensesDao.getTopFiveExpensesByCategory(userName);
        request.setAttribute("topFiveCategory", topFiveCategory);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user_dashboard.jsp");
        requestDispatcher.forward(request, response);
    }
}
