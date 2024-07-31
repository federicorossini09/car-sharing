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
        <g:each in="${activePublications}" var="publication">
            <div class="col-sm-3 mx-3">

                <div class="card car-sharing-card">
                    <div class="card-body">
                        <div class="row">
                            <div class="col">
                                <h5 class="card-title">${publication.car.brand} ${publication.car.model}</h5>
                            </div>
                            <g:if test="${publication.isFeatured()}">
                                <div class="col">
                                    <p class="text-success float-right">Destacado</p>
                                </div>
                            </g:if>
                        </div>

                        <p class="card-text">${publication.car.variant} - ${publication.car.year} - ${publication.car.kilometers}km</p>
                        <h6 class="card-text">u$s${publication.price.finalValue} /día</h6>
                        <g:link class="stretched-link" controller="publication"
                                action="viewPublication"
                                id="${publication.id}"/>
                    </div>
                </div>
            </div>
        </g:each>
    </div>
</g:if>
<g:else>
    Aún no hay autos publicados
</g:else>

</body>
</html>
