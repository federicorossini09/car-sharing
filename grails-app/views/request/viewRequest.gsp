<%@ page import="car.sharing.RentStatus; car.sharing.RequestStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Solicitud</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<g:set var="car" value="${request.publication.car}"/>
<g:set var="price" value="${request.publication.price}"/>
<g:set var="rent" value="${request.rent}"/>

<body>

<div class="container">
    <g:if test="${isGuestRequest || isHostPublication}">
        <div class="row">
            <div class="col d-flex justify-content-center">
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
            </div>
        </div>
        <h4 class="text-center">${car.brand} ${car.model} ${car.variant}</h4>
        <g:if test="${request.rent}">
            <div class="row mb-2">
                <div class="col-sm-8 mx-auto">
                    <div class="card">
                        <div class="card-body">
                            <h3 class="card-title text-center text-muted">
                                Renta
                            </h3>
                            <h4 class="text-center">
                                <g:if test="${request.rent.status == RentStatus.SCHEDULED}">
                                    <span class="badge badge-warning">Programada</span>
                                </g:if>
                                <g:if test="${request.rent.status == RentStatus.ACTIVE}">
                                    <span class="badge badge-info">En Curso</span>
                                </g:if>
                                <g:if test="${request.rent.status == RentStatus.FINISHED}">
                                    <span class="badge badge-success">Finalizada</span>
                                </g:if>
                                <g:if test="${request.rent.status == RentStatus.CANCELED}">
                                    <span class="badge badge-danger">Cancelada</span>
                                </g:if>
                            </h4>
                        </div>

                        <div class="card-footer">
                            <div class="row d-flex">
                                <g:if test="${isGuestRequest}">
                                    <div class="col d-flex justify-content-center">
                                        <g:form name="notifyDelivery" action="notifyDelivery"
                                                controller="rent"
                                                id="${request.rent.id}">
                                            <g:submitButton class="btn btn-info float-left"
                                                            name="Submit"
                                                            value="Notificar Entrega"/>
                                        </g:form>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="reportNotDelivered"
                                                action="reportNotDelivered"
                                                controller="rent"
                                                id="${request.rent.id}">
                                            <g:submitButton class="btn btn-warning text-center"
                                                            name="Submit"
                                                            value="Denunciar No Entregado"/>
                                        </g:form>
                                    </div>
                                </g:if>
                                <g:if test="${isHostPublication}">
                                    <div class="col d-flex justify-content-center">
                                        <g:form name="notifyReturn" action="notifyReturn"
                                                controller="rent"
                                                id="${request.rent.id}">
                                            <g:submitButton class="btn btn-info float-left"
                                                            name="Submit"
                                                            value="Notificar Devolución"/>
                                        </g:form>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="reportNotReturned" action="reportNotReturned"
                                                controller="rent"
                                                id="${request.rent.id}">
                                            <g:submitButton class="btn btn-warning"
                                                            name="Submit"
                                                            value="Denunciar No Devuelto"/>
                                        </g:form>
                                    </div>
                                </g:if>
                                <div class="col d-flex justify-content-center">
                                    <g:form name="cancel" action="cancel" id="${request.rent.id}">
                                        <g:submitButton class="btn btn-danger"
                                                        name="Submit"
                                                        value="Cancelar"/>
                                    </g:form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>
        <div class="row">
            <div class="col-sm-8 mx-auto">
                <div class="card">
                    <div class="card-body">

                        <h3 class="card-title text-center text-muted">
                            Solicitud
                        </h3>
                        <h4 class="text-center">
                            <g:if test="${request.status == RequestStatus.WAITING}">
                                <span class="badge badge-warning">Esperando</span>
                            </g:if>
                            <g:if test="${request.status == RequestStatus.ACCEPTED}">
                                <span class="badge badge-success">Aceptada</span>
                            </g:if>
                            <g:if test="${request.status == RequestStatus.REJECTED}">
                                <span class="badge badge-danger">Rechazada</span>
                            </g:if>
                        </h4>

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
                                params="[id: request.publication.id, *: params]">
                            <button class="btn btn-info float-left">Ver Publicación</button>
                        </g:link>
                        <g:if test="${isHostPublication}">
                            <g:if test="${request.status == RequestStatus.WAITING}">
                                <g:form name="accept" action="accept" id="${request.id}"
                                        params="[publicationId: request.publication.id]">
                                    <g:submitButton class="btn btn-success float-right"
                                                    name="Submit"
                                                    value="Aceptar"/>
                                </g:form>
                                <g:form name="reject" action="reject" id="${request.id}"
                                        params="[publicationId: request.publication.id]">
                                    <g:submitButton class="btn btn-danger float-right mr-2"
                                                    name="Submit"
                                                    value="Rechazar"/>
                                </g:form>
                            </g:if>
                        </g:if>
                    </div>
                </div>
            </div>
        </div>

    </g:if>
    <g:else>
        <div class="alert alert-danger" role="alert">
            Para ver una solicitud tenés que haberla hecho o ser el creador de la publicación
        </div>
    </g:else>
</div>

</body>
</html>
