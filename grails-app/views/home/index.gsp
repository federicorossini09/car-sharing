<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>Inicio - Car Sharing</title>
</head>

<body>

<h3>Autos Publicados</h3>
<g:if test="${!activePublications.isEmpty()}">
    <div class="row">
        <div class="col-sm-3 mx-3">
            <g:each in="${activePublications}" var="publication">
                <div class="card car-sharing-card">
                    <div class="card-body">
                        <h4 class="card-title">u$s${publication.price.finalValue} /día</h4>
                        <h6 class="card-text">${publication.car.brand} ${publication.car.model} ${publication.car.variant}</h6>

                        <p class="card-text">${publication.car.year} - ${publication.car.kilometers}km</p>
                        <g:link class="stretched-link" controller="publication"
                                action="viewPublication"
                                id="${publication.id}"/>
                    </div>
                </div>
            </g:each>
        </div>
    </div>
</g:if>
<g:else>
    Aún no hay autos publicados
</g:else>

</body>
</html>
