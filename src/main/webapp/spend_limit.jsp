<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
<table border="1">
    <tr>
        <th>Limit</th>
        <th>Category</th>
    </tr>
    <c:forEach var="spend" items="${spendLimitList}">
        <tr>
            <td>${spend.amount}</td>
            <td>${spend.categoryName}</td>
        </tr>
    </c:forEach>
</table>
<div>
    <form action="spendlimit" method="post">
        <input name="amount" placeholder="Enter the spending limit" required>
        <select name="categoryId" id="categoryId" required>
            <option value="">Select category</option>
            <c:forEach items="${categoryNames}" var="category">
                <option value="${category.categoryId}">${category.categoryName}</option>
            </c:forEach>
        </select>
        <button type="submit" name="action" value="add">Add spending limit</button>
    </form>
</div>
</body>

</html>