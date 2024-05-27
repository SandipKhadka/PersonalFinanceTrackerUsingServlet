package org.app.finance.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.finance.dao.TransactionDao;
import org.app.finance.model.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/incometransaction")
public class IncomeTransactionServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = (String) session.getAttribute("user");
        String filterDate = request.getParameter("filterDate");

        if (filterDate == null || filterDate.equals("")) {
            LocalDate localDate = LocalDate.now();
            filterDate = localDate.toString();
        }

        TransactionDao transactionDao = new TransactionDao();
        List<Transaction> transactions = transactionDao.getIncomeTransaction(userName, filterDate);
        request.setAttribute("transactions", transactions);
        RequestDispatcher view = request.getRequestDispatcher("income_transaction.jsp");
        view.forward(request, response);
    }
}
