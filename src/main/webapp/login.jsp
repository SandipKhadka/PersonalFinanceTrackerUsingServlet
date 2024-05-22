<%--
  Created by IntelliJ IDEA.
  User: khadk
  Date: 5/21/2024
  Time: 4:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css"/>
</head>

<body>
<form action="login" method="get">
    <div class="imgcontainer">
        <!-- Add an image or logo here if needed -->
    </div>
    <div class="container">
        <label for="uname"><b>Username</b></label>
        <input
                type="text"
                placeholder="Enter Username"
                id="uname"
                name="userName"
                required
        />
        <div>
            <span class="error">${userNameError}</span>
        </div>
        <label for="psw"><b>Password</b></label>
        <input
                type="password"
                placeholder="Enter Password"
                id="psw"
                name="password"
                required
        />
        <div>
            <span class="error">${passwordError}</span>
        </div>
        <button type="submit">Login</button>
        <label>
            <input type="checkbox" checked="checked" name="remember"/> Remember
            me
        </label>
    </div>

    <p class="psw"><a href="#">Forgot password?</a></p>

    <div class="container" id="register-text">
        <span>Don't have an account?</span>
        <a href="register.jsp">
            <button type="button" class="register">Register Now</button>
        </a>
    </div>
</form>
</body>
</html>

