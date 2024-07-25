<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>My Publications</title>
</head>

<body>
<h3>Mis Publicaciones</h3>
<g:if test="${!myPublications.isEmpty()}">
    <g:each in="${myPublications}" var="publication">
        <div class="card" style="width: 18rem;">
            <div class="card-body">
                <h4 class="card-title">u$s${publication.price.finalValue} /día</h4>
                <h6 class="card-text">${publication.car.brand} ${publication.car.model} ${publication.car.variant}</h6>

                <p class="card-text">${publication.car.year} - ${publication.car.kilometers}km</p>
                <a href="#" class="btn btn-primary">Ver Solicitudes</a>
            </div>
        </div>
    </g:each>
</g:if>
<g:else>
    No tenés autos publicados
</g:else>
<g:if test="${error != null}">
    <g:link controller="publication" action="newPublication">
        <button class="btn btn-info">
            Publicá tu primer auto!
        </button>
    </g:link>
</g:if>
</body>
</html>
