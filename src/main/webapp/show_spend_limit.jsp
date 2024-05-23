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
    <a href="addspendlimit">
        <button>Add Spend Limit</button>
    </a>
</body>

</html>