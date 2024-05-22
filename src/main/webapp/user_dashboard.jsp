<%--
  Created by IntelliJ IDEA.
  User: khadk
  Date: 5/21/2024
  Time: 7:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Expense Trend Dashboard</title>
    <link rel="stylesheet" href="css/dashboard.css"/>
</head>
<body>
<div class="container">
    <h1>Dashboard</h1>

    <div class="summary">
        <div class="summary-item">
            <div class="summary-title">Income</div>
            <div class="summary-value">Rs . ${income}</div>
        </div>
        <div class="summary-item">
            <div class="summary-title">Expenses</div>
            <div class="summary-value">Rs. ${expenses}</div>
        </div>
        <div class="summary-item">
            <div class="summary-title">Net Income</div>
            <div class="summary-value">Rs. ${netIncome}</div>
        </div>
    </div>
    <div class="charts">
        <div class="chart" id="expenses-by-group">
            <!-- Placeholder for Pie Chart -->
        </div>
        <div class="chart" id="top-expense-categories">
            <!-- Placeholder for Bar Chart -->
        </div>
        <div class="chart" id="expenses-by-week">
            <!-- Placeholder for Line Chart -->
        </div>
        <div class="chart" id="expenses-by-month">
            <!-- Placeholder for Bar Chart -->
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="scripts.js"></script>
<a href="income">
    <button>Add Income</button>
</a>
<a href="expenses">
    <button>Add Expenses</button>
</a>
<a href="incometransaction">
    <button>Income Transaction</button>
</a>
<a href="expensestransaction">
    <button>Expenses Transaction</button>
</a>
</body>
</html>

