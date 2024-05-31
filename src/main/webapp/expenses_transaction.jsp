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
    <body>
    <form action="expenses" method="post">
        <input name="amount" placeholder="Enter the expenses amount" required>
        <select name="categoryId" id="categoryId" required>
            <option selected disabled value="">Select category</option>
            <c:forEach var="category" items="${categoryNames}">
                <option value="${category.categoryId}">${category.categoryName}</option>
            </c:forEach>
        </select>
        <input name="remarks" placeholder="Enter expenses remarks" required>
        <input type="submit" name="submit">
        <div>
            <span>${spendLimitError}</span>
        </div>
    </form>
</div>

<div>
    <form action="expenses" method="post">
        <input type="text" name="categoryName" placeholder="Enter new expenses category">
        <input type="submit" name="submit" value="addExpensesCategory">
    </form>
</div>
<div>
    <h1>Expenses</h1>
    <form action="expenses" method="get">
        <input type="month" name="filterDate">
        <input type="submit" name="submit" value="addCategory">
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