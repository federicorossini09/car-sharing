<%@ page import="car.sharing.RequestStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Solicitud</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<g:set var="car" value="${publication.car}"/>
<g:set var="price" value="${publication.price}"/>

<body>

<div class="container">
    <div class="row">
        <div class="col-sm-8 mx-auto">
            <g:if test="${isGuestRequest || isHostPublication}">
                <g:if test='${flash.errorMessage}'>
                    <div class="alert alert-danger" role="alert">
                        ${flash.errorMessage}
                    </div>
                </g:if>
                <g:if test='${flash.successMessage}'>
                    <div class="alert alert-success" role="alert">
                        ${flash.successMessage}
                    </div>
                </g:if>
                <div class="card">
                    <div class="card-body">

                        <h4 class="card-title text-center">${car.brand} ${car.model} ${car.variant}</h4>

                        <h3 class="card-title text-center text-muted">
                            Solicitud
                            <g:if test="${request.status == RequestStatus.WAITING}">
                                <span class="badge badge-warning">Esperando</span>
                            </g:if>
                            <g:if test="${request.status == RequestStatus.ACCEPTED}">
                                <span class="badge badge-success">Aceptada</span>
                            </g:if>
                            <g:if test="${request.status == RequestStatus.REJECTED}">
                                <span class="badge badge-danger">Rechazada</span>
                            </g:if>
                        </h3>

                        <div class="row mt-2">
                            <div class="col border-right">
                                <h4>Entrega</h4>

                                <div class="row mb-2">
                                    <div class="col mr-2">
                                        <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                            Lugar</div>

                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${request.deliveryPlace}</div>
                                    </div>
                                </div>

                                <div class="row mb-2">
                                    <div class="col">
                                        <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                            Fecha</div>

                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${request.startDateTime}</div>
                                    </div>
                                </div>

                            </div>

                            <div class="col text-right">
                                <h4>Devolución</h4>

                                <div class="row mb-2">
                                    <div class="col mr-2">
                                        <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                            Lugar</div>

                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${request.returnPlace}</div>
                                    </div>
                                </div>

                                <div class="row mb-2">
                                    <div class="col">
                                        <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                            Fecha</div>

                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${request.endDateTime}</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="card-footer">
                        <g:link name="publish" controller="publication" action="viewPublication"
                                params="[id: publication.id, *: params]">
                            <button class="btn btn-info float-left">Ver Publicación</button>
                        </g:link>
                        <g:if test="${isHostPublication}">
                            <g:if test="${request.status == RequestStatus.WAITING}">
                                <g:form name="accept" action="accept" id="${request.id}"
                                        params="[publicationId: publication.id]">
                                    <g:submitButton class="btn btn-info float-right" name="Submit"
                                                    value="Aceptar"/>
                                </g:form>
                                <g:form name="unpublish" action="reject" id="${request.id}"
                                        params="[publicationId: publication.id]">
                                    <g:submitButton class="btn btn-danger float-right" name="Submit"
                                                    value="Rechazar"/>
                                </g:form>
                            </g:if>
                        </g:if>
                        <g:if test="${isGuestRequest}">
                            <g:link name="publish" controller="request" action="newRequest"
                                    params="[publicationId: publication.id, *: params]">
                                <button class="btn btn-warning float-right disabled">Cancelar</button>
                            </g:link>
                        </g:if>
                    </div>
                </div>
            </g:if>
            <g:else>
                <div class="alert alert-danger" role="alert">
                    Para ver una solicitud tenés que haberla hecho o ser el creador de la publicación
                </div>
            </g:else>
        </div>
    </div>
</div>

</body>
</html>
