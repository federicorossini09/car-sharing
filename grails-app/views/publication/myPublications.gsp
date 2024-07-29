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
        <g:each in="${myPublications}" var="publication">
            <div class="col-sm-3 mx-3">

                <div class="card car-sharing-card">
                    <div class="card-body">
                        <div class="row">
                            <div class="col">
                                <span class="text-muted">#${publication.id}</span>
                            </div>

                            <div class="col text-right">
                                <g:if test="${publication.status == PublicationStatus.ACTIVE}">
                                    <span class="badge badge-success">Activada</span>
                                </g:if>
                                <g:if test="${publication.status == PublicationStatus.PENDING}">
                                    <span class="badge badge-warning">Desactivada</span>
                                </g:if>
                            </div>
                        </div>
                        <h5 class="card-title">${publication.car.brand} ${publication.car.model}</h5>

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
</body>
</html>
