<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Document</title>
        </head>

        <body>
            <div>
                <h1>Expenses</h1>
                <form action="expensestransaction" method="get">
                    <input type="month" name="filterDate">
                    <input type="submit" name="submit">
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

        </body>

        </html>