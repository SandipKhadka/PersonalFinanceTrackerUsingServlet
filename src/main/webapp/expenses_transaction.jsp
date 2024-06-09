<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Expenses</title>
    <link rel="stylesheet" href="src\main\webapp\css\expense_transaction.css" />
    <script
      type="text/javascript"
      src="https://www.gstatic.com/charts/loader.js"
    ></script>

    <script type="text/javascript">
      google.charts.load("current", { packages: ["corechart", "line"] });

      google.charts.setOnLoadCallback(drawCharts);

      function drawCharts() {
        drawTopExpenseCategoryChart();
        drawExpenseByCategoryChart();
        drawExpenseByDayChart();
        drawExpenseByWeekChart();
      }

      function drawTopExpenseCategoryChart() {
        var data = new google.visualization.DataTable();
        data.addColumn("string", "Category");
        data.addColumn("number", "Amount");
        data.addColumn({ role: "style" });
        <c:forEach var="expenses" items="${topFiveCategory}">
          data.addRow(['${expenses.name}', ${expenses.amount}, 'blue']);
        </c:forEach>;

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
          title: "Top 5 Expense Categories",
          width: 600,
          height: 400,
          bar: { groupWidth: "95%" },
          legend: { position: "none" },
          backgroundColor: "#333",
          colors: ["#f5a623"],
        };

        var chart = new google.visualization.BarChart(
          document.getElementById("top-expense-categories")
        );
        chart.draw(view, options);
      }

      function drawExpenseByCategoryChart() {
        var data = new google.visualization.DataTable();
        data.addColumn("string", "Category");
        data.addColumn("number", "Amount");
        <c:forEach items="${pieChartData}" var="item">
          data.addRow(['${item.name}', ${item.amount}]);
        </c:forEach>;

        var options = {
          title: "Expense by Category",
          width: 400,
          height: 300,
          backgroundColor: "#333",
          is3D: true,
          colors: ["#f5a623", "#e6e6e6", "#6decaf", "#f54394", "#66d8ed"],
        };

        var chart = new google.visualization.PieChart(
          document.getElementById("expense-by-category")
        );
        chart.draw(data, options);
      }

      function drawExpenseByDayChart() {
        var data = new google.visualization.DataTable();
        data.addColumn("number", "Day");
        data.addColumn("number", "Amount");
        <c:forEach var="expenses" items="${expensesByDay}">
          data.addRows([ [${expenses.day}, ${expenses.amount}] ]);
        </c:forEach>;

        var options = {
          title: "Expense by Day",
          subtitle: "in millions of dollars (USD)",
          width: 400,
          height: 300,
          backgroundColor: "#333",
          colors: ["#f5a623"],
        };

        var chart = new google.visualization.LineChart(
          document.getElementById("expense-by-day")
        );
        chart.draw(data, options);
      }

      function drawExpenseByWeekChart() {
        var data = new google.visualization.DataTable();
        data.addColumn("number", "Week");
        data.addColumn("number", "Amount");
        <c:forEach var="expenses" items="${expensesByWeek}">
          data.addRows([ [${expenses.week}, ${expenses.amount}] ]);
        </c:forEach>;

        var options = {
          title: "Expense by Week",
          subtitle: "in millions of dollars (USD)",
          width: 400,
          height: 300,
          backgroundColor: "#333",
          colors: ["#f5a623"],
        };

        var chart = new google.visualization.LineChart(
          document.getElementById("expense-by-week")
        );
        chart.draw(data, options);
      }
    </script>
  </head>

  <body>
    <div class="container">
        <div class="expense-form">
          <form action="expenses" method="post">
            <h2>Add New Transaction</h2>
            <input
              name="amount"
              placeholder="Enter the expenses amount"
              required
            />
            <select name="categoryId" id="categoryId" required>
              <option selected disabled value="">Select category</option>
              <c:forEach var="category" items="${categoryNames}">
                <option value="${category.categoryId}">
                  ${category.categoryName}
                </option>
              </c:forEach>
            </select>
            <input
              name="remarks"
              placeholder="Enter expenses remarks"
              required
            />
            <button type="submit" name="submit" value="addExpensesTransaction">
              Add expenses
            </button>
          </form>

          <form action="expenses" method="post">
            <h2>Add New Category</h2>
            <input
              type="text"
              name="categoryName"
              placeholder="Enter new expenses category"
            />
            <button type="submit" name="submit" value="addExpensesCategory">
              Add Expenses Category
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
          <div class="chart" id="expense-by-day">
            <!-- Placeholder for Expense by Day Chart -->
          </div>
          <div class="chart" id="expense-by-week">
            <!-- Placeholder for Expense by Week Chart -->
          </div>
        </div>

        <div class="transactions">
          <h2>Transactions</h2>
          <form action="expenses" method="get">
            <input type="month" name="filterDate" />
            <button type="submit">Filter</button>
          </form>
          <table>
            <tr>
              <th>Expenses Amount</th>
              <th>Category</th>
              <th>Remarks</th>
              <td>Date</td>
              <td>Time</td>
            </tr>
            <c:forEach var="expense" items="${transactions}">
              <tr>
                <td>${expense.amount}</td>
                <td>${expense.category}</td>
                <td>${expense.remarks}</td>
                <td>${expense.date}</td>
                <td>${expense.time}</td>
              </tr>
            </c:forEach>
          </table>
        </div>
    </div>
  </body>
</html>
