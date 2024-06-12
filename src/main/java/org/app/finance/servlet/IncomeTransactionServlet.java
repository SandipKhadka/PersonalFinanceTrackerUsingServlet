package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.database.IncomeDao;
import org.app.finance.model.GraphData;
import org.app.finance.model.Income;
import org.app.finance.model.IncomeCategory;
import org.app.finance.model.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/income")
public class IncomeTransactionServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        // getting income categories
        IncomeDao incomeDao = new IncomeDao();
        List<IncomeCategory> incomeCategoryList = incomeDao.getIncomeCategory(userName);
        request.setAttribute("categoryNames", incomeCategoryList);

        // getting income transactions
        String startFilterDate = request.getParameter("startFilterDate");
        String endFilterDate = request.getParameter("endFilterDate");

        LocalDate localDate = LocalDate.now();
        if (startFilterDate == null || startFilterDate.isEmpty()) {
            startFilterDate = localDate.toString();
        }

        List<Transaction> transactions = incomeDao.getIncomeTransaction(userName, startFilterDate, endFilterDate);
        request.setAttribute("transactions", transactions);

        List<GraphData> graphTransaction = incomeDao.getIncomesDataWithAmountAndCategory(userName, startFilterDate, endFilterDate);
        request.setAttribute("pieChartData", graphTransaction);
        graphTransaction.forEach(System.out::println);

        List<GraphData> graphDataByDay = incomeDao.getIncomeByDay(userName, startFilterDate, endFilterDate);
        request.setAttribute("incomeByDay", graphDataByDay);

        List<GraphData> topFiveCategory = incomeDao.getTopFiveIncomeByCategory(userName, startFilterDate, endFilterDate);
        request.setAttribute("topFiveCategory", topFiveCategory);


        RequestDispatcher view = request.getRequestDispatcher("income_transaction.jsp");
        view.forward(request, response);
    }

    // this method will insert income in database
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        IncomeDao incomeDao = new IncomeDao();
        String command = request.getParameter("submit");
        // add income category
        if (command.equals("addCategory")) {
            String categoryName = request.getParameter("categoryName");
            incomeDao.addIncomeCategory(categoryName, userName);
        }
        // add income transactions
        if (command.equals("addIncomeTransaction")) {
            int amount = Integer.parseInt(request.getParameter("amount"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String remarks = request.getParameter("remarks");

            Income income = new Income();
            income.setAmount(amount);
            income.setCategoryId(categoryId);
            income.setRemarks(remarks);
            incomeDao.addIncome(income, userName);
        }
        response.sendRedirect("income");
    }
}
