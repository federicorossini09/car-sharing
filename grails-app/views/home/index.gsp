<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Home</title>
</head>

<body>
<h1>${username}</h1>

<g:form controller="logout">
    <g:submitButton class="dropdown-item navbar-dark color-light" name="Submit" value="Logout" style="color:gray" />
</g:form>
</body>
</html>
