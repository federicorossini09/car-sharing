<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>Inicio - Car Sharing</title>
</head>

<body>

<h3>Autos Publicados</h3>
<g:if test="${!activePublications.isEmpty()}">
    <g:each in="${activePublications}" var="publication">
        <div class="card" style="width: 18rem;">
            <div class="card-body">
                <h4 class="card-title">u$s${publication.price.finalValue} /día</h4>
                <h6 class="card-text">${publication.car.brand} ${publication.car.model} ${publication.car.variant}</h6>
                <p class="card-text">${publication.car.year} - ${publication.car.kilometers}km</p>
                <a href="#" class="btn btn-primary">Solicitar</a>
            </div>
        </div>
    </g:each>
</g:if>
<g:else>
    Aún no hay autos publicados
</g:else>

</body>
</html>
