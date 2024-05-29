<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Expense Trend Dashboard</title>
    <link rel="stylesheet" href="css/dashboard.css"/>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>


    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});

        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Topping');
            data.addColumn('number', 'Slices');
            <c:forEach items="${transactions}" var="item">
            data.addRow(['${item.name}', ${item.amount}]);
            </c:forEach>

            var options = {
                'title': 'How Much Pizza I Ate Last Night',
                'width': 400,
                'height': 300
            };
            var chart = new google.visualization.PieChart(document.getElementById('expenses-by-group'));
            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'day');
            data.addColumn('number', 'amount');
            <c:forEach var="expenses" items="${expensesByDay}">
            data.addRow(['${expenses.day}', ${expenses.amount}]);
            </c:forEach>
            var options = {
                title: 'Company Performance',
                curveType: 'function',
                legend: {position: 'bottom'}
            };

            var chart = new google.visualization.LineChart(document.getElementById('expenses-by-day'));

            chart.draw(data, options);
        }
    </script>
    <script type="text/javascript">
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'day');
            data.addColumn('number', 'amount');
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
                title: "Density of Precious Metals, in g/cm^3",
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
        <div class="chart" id="expenses-by-day">
            <!-- Placeholder for Bar Chart -->
        </div>
    </div>
</div>

<a href="income">
    <button>Income Transaction</button>
</a>
<a href="expenses">
    <button>Expenses Transaction</button>
</a>
<a href="spendlimit">
    <button>Spend Limit</button>
</a>

</body>

</html>