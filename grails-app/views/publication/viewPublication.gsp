<%@ page import="car.sharing.PublicationStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Publicación</title>
    <meta name="layout" content="${'main'}"/>
</head>

<g:set var="car" value="${publication.car}"/>
<g:set var="price" value="${publication.price}"/>
<g:set var="isHost" value="${publication.host.user.username == user.username}"/>

<body>

<div class="container">
    <div class="row">
        <div class="col-sm-8 mx-auto">
            <div class="card">
                <div class="card-body">
                    <span class="text-muted">#${publication.id}</span>

                    <h2 class="card-title">${car.brand} ${car.model} ${car.variant}</h2>
                    <h4 class="card-subtitle text-muted">${car.year} - ${car.kilometers}km</h4>
                </div>

                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                Fecha de Expiracion de la VTV</div>

                            <div class="h5 mb-0 font-weight-bold text-gray-800">${car.vtvExpirationDate}</div>
                        </div>
                    </div>

                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                Patente</div>

                            <div class="h5 mb-0 font-weight-bold text-gray-800">${car.licensePlate}</div>
                        </div>
                    </div>
                </div>

                <div class="card-footer">
                    <g:if test="${isHost}">
                        <g:if test="${publication.status == PublicationStatus.PENDING}">
                            <g:form name="publish" action="publish" id="${publication.id}">
                                <g:submitButton class="btn btn-info float-right" name="Submit"
                                                value="Publicar"/>
                            </g:form>
                        </g:if>
                        <g:else>
                            <g:form name="unpublish" action="unpublish" id="${publication.id}">
                                <g:submitButton class="btn btn-warning float-right" name="Submit"
                                                value="Despublicar"/>
                            </g:form>
                        </g:else>
                        <g:link name="viewRequests" controller="request"
                                action="viewPublicationRequests"
                                params="[publicationId: publication.id]">
                            <button class="btn btn-info float-left">Ver Solicitudes</button>
                        </g:link>
                    </g:if>
                    <g:else>
                        <g:link name="publish" controller="request" action="newRequest"
                                params="[publicationId: publication.id, *: params]">
                            <button class="btn btn-info float-right">Solicitar</button>
                        </g:link>
                    </g:else>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
