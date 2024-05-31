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
    <form action="income" method="post">
        <input required type="number" name="amount" placeholder="Enter amount">
        <select name="categoryId" id="categoryId" required>
            <option value="" selected>Select the Category</option>
            <c:forEach var="category" items="${categoryNames}">
                <option value="${category.categoryId}">${category.categoryName}</option>
            </c:forEach>
        </select>
        <input required type="text" name="remarks" placeholder="Enter remarks">
        <input type="submit" name="submit" value="addIncomeTransaction">
    </form>

</div>
<div>
    <form action="income" method="post">
        <input type="text" name="categoryName" placeholder="Enter New category">
        <input type="submit" name="submit" value="addCategory">
    </form>
</div>
<div>

    <h1>Income</h1>
    <form action="income" method="get">
        <input type="month" name="filterDate">
        <input type="submit" name="submit">
    </form>
    <table border="1">
        <tr>
            <th>Income Amount</th>
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