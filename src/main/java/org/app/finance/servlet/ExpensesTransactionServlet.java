package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.database.ExpensesDao;
import org.app.finance.database.SpendingLimitDao;
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
        String startFilterDate = request.getParameter("startFilterDate");
        String endFilterDate = request.getParameter("endFilterDate");
        if (startFilterDate == null || startFilterDate.isEmpty()) {
            startFilterDate = localDate.toString();
        }
        List<Transaction> transactions = expensesDao.getExpensesTransaction(userName, startFilterDate, endFilterDate);
        request.setAttribute("transactions", transactions);

        List<GraphData> graphTransaction = expensesDao.getExpensesDataWithAmountAndCategory(userName, startFilterDate, endFilterDate);
        request.setAttribute("pieChartData", graphTransaction);
        graphTransaction.forEach(System.out::println);

        List<GraphData> graphDataByDay = expensesDao.getExpensesByDay(userName, startFilterDate, endFilterDate);
        request.setAttribute("expensesByDay", graphDataByDay);

        List<GraphData> topFiveCategory = expensesDao.getTopFiveExpensesByCategory(userName, startFilterDate, endFilterDate);
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
        System.out.println(command);

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
        if (command.equals("delete")) {
            int expensesId = Integer.parseInt(request.getParameter("expensesId"));
            System.out.println(expensesId);
            expensesDao.deleteExpensesRecord(userName, expensesId);
        }
        if (command.equals("update")) {
            int expensesId = Integer.parseInt(request.getParameter("expensesId"));
            System.out.println("expenses id" + expensesId);
            int updatedExpenses = Integer.parseInt(request.getParameter("amount"));
            System.out.println("amount" + updatedExpenses);
            String remarks = request.getParameter("remarks");
            System.out.println("remarks" + remarks);
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            System.out.println("category id" + categoryId);

            SpendingLimitDao spendingLimitDao = new SpendingLimitDao();

            int originalAmount = expensesDao.getOriginalAmount(userName, categoryId, expensesId);

            int totalSpendLimit = spendingLimitDao.getSumOfSpendLimit(userName, categoryId);
            int totalExpensesBeforeUpdate = expensesDao.getSumOfExpenses(userName, categoryId);
            int totalExpensesAfterRemovingOriginalRecord = totalExpensesBeforeUpdate - originalAmount;
            int leftSpendLimitBeforeUpdate = totalSpendLimit - originalAmount;
//            int leftSpendLimit = totalSpendLimit + originalAmount - updatedExpenses;


            if (totalSpendLimit != 0) {
                if ((totalExpensesAfterRemovingOriginalRecord + updatedExpenses) >= totalSpendLimit) {
                    request.setAttribute("updateError",
                            "You are exceeding your spending limit left amount is" + " " + leftSpendLimitBeforeUpdate);
                    doGet(request, response);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("expenses_form.jsp");
                    requestDispatcher.forward(request, response);
                    return;
                }
            }

            Expenses expenses = new Expenses();
            expenses.setAmount(updatedExpenses);
            expenses.setCategoryId(categoryId);
            expenses.setRemarks(remarks);
            expensesDao.updateRecord(expenses, userName, expensesId);
        }
        response.sendRedirect("expenses");
    }
}
