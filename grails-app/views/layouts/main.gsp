<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
    <g:layoutTitle default="Car Sharing"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/home">Car Sharing</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <g:link class="nav-link" controller="request" action="viewMyRequests">
                    Mis Solicitudes
                </g:link>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="#">Mis Rentas <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item active">
                <g:link class="nav-link" controller="publication" action="myPublications">
                    Mis Publicaciones
                </g:link>
            </li>
        </ul>
        <span class="navbar-text" href="#">
            <g:set var="userService" bean="userService"/>
            <g:set var="username" value="${userService.getLoggedUsername()}"/>
            <g:if test="${userService.getLoggedUsername() != null}">
                Hola, ${username}
                <g:form controller="logout">
                    <g:submitButton class="btn btn-outline-info" name="Submit"
                                    value="Cerrar sesión"/>
                </g:form>
            </g:if>
        </span>
    </div>
</nav>

<div class="container my-5">
    <div class="row">
        <div class="col-sm-12">
            <g:layoutBody/>
        </div>
    </div>
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>
