<%@ page import="car.sharing.PublicationStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>My Publications</title>
</head>

<body>
<h3>Mis Publicaciones</h3>
<g:if test="${myPublications.empty}">
    <div class="mb-3">
        No tenés autos publicados
    </div>
</g:if>
<g:if test="${error != null}">
    <g:link controller="publication" action="newPublication">
        <button class="btn btn-info">
            Publicá tu primer auto!
        </button>
    </g:link>
</g:if>
<g:else>
    <g:link controller="publication" action="newPublication">
        <button class="btn btn-info">
            Publicá un nuevo auto
        </button>
    </g:link>
</g:else>
<g:if test="${!myPublications.empty}">
    <div class="row mt-3">
        <div class="col-sm-3 mx-3">
            <g:each in="${myPublications}" var="publication">
                <div class="card car-sharing-card">
                    <div class="card-body">
                        <g:if test="${publication.status == PublicationStatus.ACTIVE}">
                            <span class="badge badge-success">Activada</span>
                        </g:if>
                        <g:if test="${publication.status == PublicationStatus.PENDING}">
                            <span class="badge badge-warning">Desactivada</span>
                        </g:if>
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
</body>
</html>
