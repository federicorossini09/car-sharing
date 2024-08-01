<%@ page import="java.time.format.FormatStyle; java.time.format.DateTimeFormatter; car.sharing.PublicationStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Publicación</title>
    <meta name="layout" content="${'main'}"/>
</head>

<g:set var="car" value="${publication.car}"/>
<g:set var="price" value="${publication.price}"/>
<body>

<div class="container">
    <div class="row">
        <div class="col-sm-8 mx-auto">
            <div class="card">
                <div class="card-body">
                    <g:if test='${flash.successMessage}'>
                        <div class="alert alert-success alert-dismissible" role="alert">
                            ${flash.successMessage}
                            <button type="button" class="close" data-dismiss="alert"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </g:if>
                    <div class="row">
                        <div class="col">
                            <span class="text-muted">#${publication.id}</span>
                        </div>

                        <div class="col text-right">
                            <g:if test="${isHost}">
                                <g:if test="${publication.status == PublicationStatus.ACTIVE}">
                                    <span class="badge badge-success">Activada</span>
                                </g:if>
                                <g:if test="${publication.status == PublicationStatus.PENDING}">
                                    <span class="badge badge-warning">Desactivada</span>
                                </g:if>
                            </g:if>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <h3 class="card-title">${car.brand} ${car.model} ${car.variant}</h3>
                        </div>

                        <div class="col text-right">
                            <h3 class="card-title">u$s${price.finalValue} /día</h3>
                        </div>
                    </div>
                </div>

                <div class="card-body">
                    <div class="row">
                        <div class="col border-right">
                            <div class="row no-gutters align-items-center mb-3">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primary text-uppercase">
                                        Año</div>

                                    <div class="h5 mb-1 font-weight-bold text-gray-800">${car.year}</div>
                                </div>
                            </div>

                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primary text-uppercase">
                                        Kilometraje</div>

                                    <div class="h5 mb-1 font-weight-bold text-gray-800">${car.kilometers}km</div>
                                </div>
                            </div>
                        </div>

                        <div class="col text-right">
                            <div class="row no-gutters align-items-center mb-3">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primary text-uppercase">
                                        VTV vigente hasta</div>

                                    <div class="h5 mb-1 font-weight-bold text-gray-800">${DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(car.vtvExpirationDate)}</div>
                                </div>
                            </div>

                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primary text-uppercase">
                                        Patente</div>

                                    <div class="h5 mb-1 font-weight-bold text-gray-800">${car.licensePlate}</div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="card-footer">
                    <g:if test="${isHost}">
                        <g:if test="${publication.status == PublicationStatus.PENDING}">
                            <g:form name="publish" action="publish" id="${publication.id}">
                                <g:submitButton class="btn btn-success float-right" name="Submit"
                                                value="Activar"/>
                            </g:form>
                        </g:if>
                        <g:else>
                            <g:form name="unpublish" action="unpublish" id="${publication.id}">
                                <g:submitButton class="btn btn-warning float-right" name="Submit"
                                                value="Desactivar"/>
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

    <g:if test="${!publication.score.reviews.isEmpty()}">
        <div class="row mt-4">
            <div class="col-sm-8 mx-auto">
                <h5 class="text-muted">Reseñas sobre esta publicación</h5>
                <ul class="list-group list-group-flush">
                    <g:each in="${publication.score.reviews.reverse()}" var="review">
                        <li class="list-group-item">
                            <div class="row">
                                <div class="col">
                                    <div class="text-muted"
                                         style="font-size: 14px">
                                        ${review.request.guest.user.username} | ${review.createdAt.format(DateTimeFormatter.ISO_DATE)}
                                    </div>
                                    <g:each var="i" in="${(0..<review.score)}">
                                        <span class="rating-show">★</span>
                                    </g:each>
                                    <p>${review.text}</p>
                                </div>
                            </div>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </g:if>
</div>

</body>
</html>
