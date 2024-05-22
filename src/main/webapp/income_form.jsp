<%--
  Created by IntelliJ IDEA.
  User: khadk
  Date: 5/21/2024
  Time: 8:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
<form action="income" method="post">
    <input required type="number" name="amount" placeholder="Enter amount">
    <select name="categoryId" id="categoryId" required>
        <option value="" selected>Select the Category</option>
        <c:forEach var="category" items="${categoryNames}">
            <option value="${category.categoryId}">${category.categoryName}</option>
        </c:forEach>

        <input required type="text" name="remarks" placeholder="Enter remarks">
        <input type="submit" name="submit">
</form>
</body>

</html>
