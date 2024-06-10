<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>


    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});

        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Amount');
            <c:forEach items="${pieChartData}" var="item">
            data.addRow(['${item.name}', ${item.amount}]);
            </c:forEach>

            var options = {
                'title': 'Expenses of this month',
                'width': 400,
                'height': 300
            };
            var chart = new google.visualization.PieChart(document.getElementById('expenses-by-group'));
            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['line']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('number', 'Day');
            data.addColumn('number', 'Amount');
            <c:forEach var="expenses" items="${expensesByDay}">
            data.addRows([
                [${expenses.day}, ${expenses.amount}]
            ]);
            </c:forEach>
            var options = {
                chart: {
                    title: 'Expenses by day',
                    subtitle: 'in millions of dollars (USD)'
                },
                width: 400,
                height: 300
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
            data.addRow(['${expenses.name}', ${expenses.amount}, 'blue']);
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
                title: "Expenses top five category",
                width: 600,
                height: 400,
                bar: {groupWidth: "95%"},
                legend: {position: "none"},
            };
            var chart = new google.visualization.BarChart(document.getElementById("top-expense-categories"));
            chart.draw(view, options);
        }
    </script>
</head>

<body>
<div>

    <form action="expenses" method="post">
        <input name="amount" placeholder="Enter the expenses amount" required>
        <select name="categoryId" id="categoryId" required>
            <option selected disabled value="">Select category</option>
            <c:forEach var="category" items="${categoryNames}">
                <option value="${category.categoryId}">${category.categoryName}</option>
            </c:forEach>
        </select>
        <input name="remarks" placeholder="Enter expenses remarks" required>
        <button type="submit" name="submit" value="addExpensesTransaction">Add expenses</button>
    </form>
</div>

<div>
    <form action="expenses" method="post">
        <input type="text" name="categoryName" placeholder="Enter new expenses category">
        <button type="submit" name="submit" value="addExpensesCategory">Add Expenses Category</button>
    </form>
    <dir>
        <span>${spendLimitError}</span>
    </dir>
</div>
<div>
    <h1>Expenses</h1>
    <form action="expenses" method="get">
        <input type="month" name="filterDate">
        <button type="submit">Filter</button>
    </form>
    <table border="1">
        <tr>
            <th>Expenses Amount</th>
            <th>Category</th>
            <th>Remarks</th>
            <td>Date</td>
            <td>Time</td>
        </tr>
        <c:forEach var="expenses" items="${transactions}">
            <tr>
                <td>${expenses.amount}</td>
                <td>${expenses.category}</td>
                <td>${expenses.remarks}</td>
                <td>${expenses.date}</td>
                <td>${expenses.time}</td>
            </tr>
        </c:forEach>
    </table>
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
    <div class="chart" id="expenses-by-day">
        <!-- Placeholder for Bar Chart -->
    </div>
</div>

</body>

</html>