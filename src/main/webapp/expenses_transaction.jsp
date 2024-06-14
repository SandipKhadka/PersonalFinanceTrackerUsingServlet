<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/expenses_transaction.css">
    <title>Expenses</title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});

        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Amount');

            <c:forEach items="${pieChartData}" var="item">
            data.addRow(['${item.name}', ${item.amount}])

            </c:forEach>

            var options = {
                title: "Expense by Category",
                titleTextStyle: {
                    color: 'white',
                    bold: false
                },
                width: 600,
                height: 400,
                backgroundColor: "#333",
                is3D: false,
                colors: ["#f5a623", "#e6e6e6", "#6decaf", "#f54394", "#66d8ed"],
            };
            var chart = new google.visualization.PieChart(document.getElementById('expense-by-category'));
            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['line']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Day');
            data.addColumn('number', 'Amount');
            <c:forEach var="expenses" items="${expensesByDay}">
            data.addRows([
                ['${expenses.day}', ${expenses.amount}]
            ]);
            </c:forEach>
            var options = {
                title: 'Expenses by day',
                titleTextStyle: {
                    color: 'white',
                    bold: false
                },
                curveType: 'function',
                width: 600,
                height: 400,
                legend: {position: 'bottom'},
                backgroundColor: "#333"
            };

            var chart = new google.charts.Line(document.getElementById('expenses-by-day'));

            chart.draw(data, google.charts.Line.convertOptions(options));
        }
    </script>
    <script type="text/javascript">
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Amount');
            data.addColumn({role: "style"});
            <c:forEach var="expenses" items="${topFiveCategory}">
            data.addRow(['${expenses.name}', ${expenses.amount}, 'orange']);
            </c:forEach>

            var view = new google.visualization.DataView(data);
            view.setColumns([0, 1,
                {
                    calc: "stringify",
                    sourceColumn: 1,
                    type: "string",
                    role: "annotation"
                },
                2]);

            var options = {
                title: "Top 5 Expense Categories",
                titleTextStyle: {
                    color: 'white',
                    bold: false
                },
                width: 600,
                height: 400,
                bar: {groupWidth: "95%"},
                legend: {position: "none"},
                backgroundColor: "#333",
                colors: ["#f5a623"],
            };

            var chart = new google.visualization.BarChart(document.getElementById("top-expense-categories"));
            chart.draw(view, options);
        }
    </script>
</head>

<body>
<div class="container">
    <div class="expense-form">
        <form action="expenses" method="post">
            <h2>Add New Transaction</h2>
            <input type="number" name="amount" placeholder="Enter amount" required/>
            <select name="categoryId" id="categoryId" required>
                <option selected disabled value="">Select category</option>
                <c:forEach var="category" items="${categoryNames}">
                    `<option value="${category.categoryId}">
                    ${category.categoryName}
                    </option>`
                </c:forEach>
            </select>
            <input type="text" name="remarks" placeholder="Enter expenses remarks" required/>
            <button type="submit" name="submit" value="addExpensesTransaction">
                Add expenses
            </button>
        </form>

        <div>
            <span>${spendLimitError}</span>
        </div>

        <form action="expenses" method="post">
            <h2>Add New Category</h2>
            <input type="text" name="categoryName" placeholder="Enter new category"/>
            <button type="submit" name="submit" value="addExpensesCategory">
                Add Category
            </button>
        </form>
    </div>
</div>
<div class="charts">
    <div class="chart" id="top-expense-categories">
        <!-- Placeholder for Top 5 Expense Categories Chart -->
    </div>
    <div class="chart" id="expense-by-category">
        <!-- Placeholder for Expense by Category Chart -->
    </div>
    <div class="chart" id="expenses-by-day"></div>
</div>

<div class="transactions">
    <h2>Transactions</h2>
    <div class="filter-section">
        <form action="expenses" method="get">
            <input type="month" name="startFilterDate"/>
            <input type="month" name="endFilterDate">
            <button type="submit">Filter</button>
        </form>
    </div>
    <div>
        <span>${updateError}</span>
    </div>
    <table class="transactions-table">
        <tr>
            <th>Expenses Amount</th>
            <th>Category</th>
            <th>Remarks</th>
            <td>Date</td>
            <td>Time</td>
            <th>Action</th>
        </tr>
        <c:forEach var="expense" items="${transactions}">
            <tr>
                <form action="expenses" method="post">
                    <td>
                        <label for="amount-${expense.transactionId}"
                               id="defaultAmount-${expense.transactionId}">${expense.amount}</label>
                        <input type="number" name="amount" id="amount-${expense.transactionId}"
                               value="${expense.amount}" hidden="hidden"></td>
                    <td>
                        <span id="defaultCategory-${expense.transactionId}">${expense.category}</span>
                        <select name="categoryId" id="categoryId-${expense.transactionId}" required hidden="hidden">
                            <option selected value="${expense.categoryId}">${expense.category}</option>
                            <c:forEach var="category" items="${categoryNames}">
                                <option value="${category.categoryId}">
                                        ${category.categoryName}
                                </option>
                                `
                            </c:forEach>
                        </select>

                    </td>
                    <td>
                        <label for="remarks-${expense.transactionId}"
                               id="defaultRemarks-${expense.transactionId}">${expense.remarks}</label>
                        <input type="text" name="remarks" hidden="hidden" id="remarks-${expense.transactionId}"
                               value="${expense.remarks}"></td>
                    <td>${expense.date}</td>
                    <td>${expense.time}</td>
                    <td>
                        <input type="number" value="${expense.transactionId}" hidden="hidden" name="expensesId"
                               id="id-${expense.transactionId}">
                        <button type="submit" name="submit" id="delete-${expense.transactionId}" value="delete">Delete
                            now
                        </button>
                        <button type="button" id="edit-${expense.transactionId}"
                                onclick="edit(${expense.transactionId})">Edit
                        </button>
                        <button type="submit" name="submit" id="update-${expense.transactionId}" value="update"
                                hidden="hidden">Update Now
                        </button>

                        <button type="button" id="back-${expense.transactionId}" hidden="hidden"
                                onclick="back(${expense.transactionId})">Back
                        </button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </table>

</div>
</body>

</html>

<script src="js/editButton.js"></script>
