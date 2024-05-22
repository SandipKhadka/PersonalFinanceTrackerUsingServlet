<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: khadk
  Date: 5/22/2024
  Time: 7:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

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
</form>


</body>

</html>