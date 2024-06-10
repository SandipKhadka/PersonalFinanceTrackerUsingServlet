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
import org.app.finance.model.Expenses;
import org.app.finance.model.ExpensesCategory;
import org.app.finance.model.GraphData;
import org.app.finance.model.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@WebServlet("/expenses")
public class ExpensesTransactionServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        // get income categories
        ExpensesDao expensesDao = new ExpensesDao();
        List<ExpensesCategory> expensesCategoryList = expensesDao.getExpensesCategory(userName);
        request.setAttribute("categoryNames", expensesCategoryList);

        // get income transactions
        LocalDate localDate = LocalDate.now();
        String filterDate = request.getParameter("filterDate");
        if (filterDate == null || filterDate.isEmpty()) {
            filterDate = localDate.toString();
        }
        List<Transaction> transactions = expensesDao.getExpensesTransaction(userName, filterDate);
        request.setAttribute("transactions", transactions);

        List<GraphData> graphTransaction = expensesDao.getExpensesDataWithAmountAndCategory(userName, filterDate);
        request.setAttribute("pieChartData", graphTransaction);
        graphTransaction.forEach(System.out::println);

        List<GraphData> graphDataByDay = expensesDao.getExpensesByDay(userName, filterDate);
        request.setAttribute("expensesByDay", graphDataByDay);

        List<GraphData> topFiveCategory = expensesDao.getTopFiveExpensesByCategory(userName, filterDate);
        request.setAttribute("topFiveCategory", topFiveCategory);


        RequestDispatcher view = request.getRequestDispatcher("expenses_transaction.jsp");
        view.forward(request, response);
    }

    // this method will insert income in database and total expenses limit
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");

        String command = request.getParameter("submit");

        ExpensesDao expensesDao = new ExpensesDao();

        // add expenses category
        if (Objects.equals(command, "addExpensesCategory")) {
            String categoryName = request.getParameter("categoryName");
            expensesDao.addExpensesCategory(categoryName, userName);
        }
        if (Objects.equals(command, "addExpensesTransaction")) {
            // add expenses transaction
            int addedExpenses = Integer.parseInt(request.getParameter("amount"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String remarks = request.getParameter("remarks");

            SpendingLimitDao spendingLimitDao = new SpendingLimitDao();
            int totalExpensesAmount = expensesDao.getSumOfExpenses(userName, categoryId);
            int totalSpendLimit = spendingLimitDao.getSumOfSpendLimit(userName, categoryId);
            int leftSpendLimit = totalSpendLimit - totalExpensesAmount;

            if (totalSpendLimit != 0) {
                if ((totalExpensesAmount + addedExpenses) >= totalSpendLimit) {
                    System.out.println(totalExpensesAmount);
                    System.out.println(totalSpendLimit);
                    System.out.println(leftSpendLimit);
                    request.setAttribute("spendLimitError",
                            "You are exceeding your spending limit left amount is" + " " + leftSpendLimit);
                    doGet(request, response);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("expenses_form.jsp");
                    requestDispatcher.forward(request, response);
                    return;
                }
            }

            Expenses expenses = new Expenses();
            expenses.setAmount(addedExpenses);
            expenses.setCategoryId(categoryId);
            expenses.setRemarks(remarks);
            expensesDao.addExpanses(expenses, userName);
        }
        response.sendRedirect("expenses");
    }
}
