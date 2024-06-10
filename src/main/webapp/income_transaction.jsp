<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/income_transaction.css">
    <title>Income Transaction</title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <script type="text/javascript">
        google.charts.load("current", {packages: ["corechart"]});

        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn("string", "Category");
            data.addColumn("number", "Amount");
            <c:forEach items="${pieChartData}" var="item">
            data.addRow(['${item.name}', ${item.amount}]);
            </c:forEach>

            var options = {
                title: "Income of this month",
                width: 400,
                height: 300,
            };
            var chart = new google.visualization.PieChart(
                document.getElementById("income-by-group")
            );
            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript">
        google.charts.load("current", {packages: ["line"]});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn("number", "Day");
            data.addColumn("number", "Amount");
            <c:forEach var="income" items="${incomeByDay}">
            data.addRows([[${income.day}, ${income.amount}]]);
            </c:forEach>
            var options = {
                chart: {
                    title: "Income by day",
                    subtitle: "in millions of dollars (USD)",
                },
                width: 400,
                height: 300,
            };

            var chart = new google.charts.Line(
                document.getElementById("income-by-day")
            );

            chart.draw(data, google.charts.Line.convertOptions(options));
        }
    </script>
    <script type="text/javascript">
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn("string", "Category");
            data.addColumn("number", "Amount");
            data.addColumn({role: "style"});
            <c:forEach var="income" items="${topFiveCategory}">
            data.addRow(['${income.name}', ${income.amount}, 'blue']);
            </c:forEach>

            var view = new google.visualization.DataView(data);
            view.setColumns([
                0,
                1,
                {
                    calc: "stringify",
                    sourceColumn: 1,
                    type: "string",
                    role: "annotation",
                },
                2,
            ]);

            var options = {
                title: "Income top five category",
                width: 600,
                height: 400,
                bar: {groupWidth: "95%"},
                legend: {position: "none"},
            };
            var chart = new google.visualization.BarChart(
                document.getElementById("top-income-categories")
            );
            chart.draw(view, options);
        }
    </script>
</head>
<body>
<div class="container">
    <div class="income-form">
        <form action="income" method="post">
            <h2>Add Income</h2>
            <input
                    required
                    type="number"
                    name="amount"
                    placeholder="Enter amount"
            />
            <select name="categoryId" id="categoryId" required>
                <option value="" selected>Select the Category</option>
                <c:forEach var="category" items="${categoryNames}">
                    <option value="${category.categoryId}">
                            ${category.categoryName}
                    </option>
                </c:forEach>
            </select>
            <input
                    required
                    type="text"
                    name="remarks"
                    placeholder="Enter remarks"
            />
            <button type="submit" name="submit" value="addIncomeTransaction">
                Add income
            </button>
        </form>

        <form action="income" method="post">
            <h2>Add New Category</h2>
            <input
                    type="text"
                    name="categoryName"
                    placeholder="Enter New category"
            />
            <button type="submit" name="submit" value="addCategory">
                Add Category
            </button>
        </form>
    </div>

    <div class="charts">
        <div class="chart" id="top-income-categories">
            <!-- Placeholder for Bar Chart -->
        </div>
        <div class="chart" id="income-by-group">
            <!-- Placeholder for Pie Chart -->
        </div>
        <div class="chart" id="income-by-day">
            <!-- Placeholder for Line Chart -->
        </div>
        <div class="chart" id="income-by-week">
            <!-- Placeholder for Line Chart -->
        </div>
    </div>
    <div class="transactions">
        <h2>Transactions</h2>
        <div class="filter-section">
            <form action="income" method="get">
                <input type="month" name="filterDate"/>
                <button type="submit">Filter</button>
            </form>
        </div>
    </div>

    <table class="transactions-table">
        <tr>
            <th>Income Amount</th>
            <th>Category</th>
            <th>Remarks</th>
            <td>Date</td>
            <td>Time</td>
        </tr>
        <c:forEach var="income" items="${transactions}">
            <tr>
                <td>${income.amount}</td>
                <td>${income.category}</td>
                <td>${income.remarks}</td>
                <td>${income.date}</td>
                <td>${income.time}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>