<%@ page import="java.time.format.DateTimeFormatter; car.sharing.RentStatus; car.sharing.RequestStatus" contentType="text/html;charset=UTF-8" %>
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

        <h2 class="text-center text-muted">${car.brand} ${car.model} ${car.variant}</h2>
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

                            <div class="row mt-2">
                                <div class="col">
                                    <div class="row mb-2">
                                        <div class="col mr-2">
                                            <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                                Precio Total Final</div>

                                            <div class="h5 mb-0 font-weight-bold text-gray-800">u$s${rent.totalPrice.value}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row mt-2">
                                <div class="col border-right">
                                    <div class="row mb-2">
                                        <div class="col mr-2">
                                            <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                                Kilometros Entregados</div>

                                            <div class="h5 mb-0 font-weight-bold text-gray-800">${rent.kilometersDelivered ? rent.kilometersDelivered + 'km' : '-'}</div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col text-right">

                                    <div class="row mb-2">
                                        <div class="col mr-2">
                                            <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                                Kilometros Devueltos</div>

                                            <div class="h5 mb-0 font-weight-bold text-gray-800">${rent.kilometersReturned ? rent.kilometersReturned + 'km' : '-'}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card-footer">
                            <div class="row d-flex">
                                <g:if test="${isGuestRequest}">
                                    <div class="col d-flex justify-content-center">
                                        <button type="button" class="btn btn-info"
                                                data-toggle="modal"
                                                data-target="#notifyModal">
                                            Notificar Entrega
                                        </button>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="reportNotDelivered"
                                                action="reportNotDelivered"
                                                controller="rent"
                                                id="${request.id}">
                                            <g:submitButton class="btn btn-warning text-center"
                                                            name="Submit"
                                                            value="Denunciar No Entregado"/>
                                        </g:form>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="cancelFromGuest" action="cancelFromGuest"
                                                controller="rent"
                                                id="${request.id}">
                                            <g:submitButton class="btn btn-danger"
                                                            name="Submit"
                                                            value="Cancelar"/>
                                        </g:form>
                                    </div>
                                </g:if>
                                <g:if test="${isHostPublication}">
                                    <div class="col d-flex justify-content-center">
                                        <button type="button" class="btn btn-info"
                                                data-toggle="modal"
                                                data-target="#notifyModal">
                                            Notificar Devolución
                                        </button>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="reportNotReturned" action="reportNotReturned"
                                                controller="rent"
                                                id="${request.id}">
                                            <g:submitButton class="btn btn-warning"
                                                            name="Submit"
                                                            value="Denunciar No Devuelto"/>
                                        </g:form>
                                    </div>

                                    <div class="col d-flex justify-content-center">
                                        <g:form name="cancelFromHost" action="cancelFromHost"
                                                controller="rent"
                                                id="${request.id}">
                                            <g:submitButton class="btn btn-danger"
                                                            name="Submit"
                                                            value="Cancelar"/>
                                        </g:form>
                                    </div>
                                </g:if>
                                <div class="col d-flex justify-content-center">
                                    <g:link name="newReview" controller="review"
                                            action="newReview"
                                            params="[requestId: request.id, isGuestRequest: isGuestRequest, isHostPublication: isHostPublication]">
                                        <button class="btn btn-primary">Hacer una Reseña</button>
                                    </g:link>
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
                            <div class="col">
                                <div class="row mb-2">
                                    <div class="col mr-2">
                                        <div class="text-xs text-muted text-primary text-uppercase mb-1">
                                            Kilometros a recorrer</div>

                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${request.kilometers}km</div>
                                    </div>
                                </div>
                            </div>
                        </div>

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
                        <g:link name="viewPublication" controller="publication"
                                action="viewPublication"
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

        <div class="row mt-4">
            <div class="col-sm-8 mx-auto">
                <h5 class="text-muted">Reseña del ${isGuestRequest ? 'anfitrión' : 'huésped'}</h5>
                <g:set var="review"
                       value="${isGuestRequest ? request.guest.score.reviews.find { it.request.id == request.id } :
                               request.publication.score.reviews.find { it.request.id == request.id }}"/>
                <g:if test="${review}">
                    <div class="row">
                        <div class="col">
                            <div class="text-muted"
                                 style="font-size: 14px">
                                <g:set var="reviewUser"
                                       value="${isGuestRequest ? review.request.publication.host.user.username :
                                               review.request.guest.user.username}"/>
                                ${reviewUser} | ${review.createdAt.format(DateTimeFormatter.ISO_DATE)}
                            </div>
                            <g:each var="i" in="${(0..<review.score)}">
                                <span class="rating-show">★</span>
                            </g:each>
                            <p>${review.text}</p>
                        </div>
                    </div>
                </g:if>
                <g:else>
                    <p>Todavía no recibiste una reseña</p>
                </g:else>
            </div>
        </div>

    </g:if>
    <g:else>
        <div class="alert alert-danger" role="alert">
            Para ver una solicitud tenés que haberla hecho o ser el creador de la publicación
        </div>
    </g:else>
</div>

<div class="modal fade" id="notifyModal" tabindex="-1" aria-labelledby="notifyLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"
                    id="notifyLabel">Notificar ${isGuestRequest ? 'Entrega' : 'Devolución'}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <g:if test="${isGuestRequest}">
                <g:form name="notifyDelivery" action="notifyDelivery"
                        controller="rent"
                        id="${request?.rent?.id}">
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="form-group">
                                <label for="kilometersDelivered">¿Con cuántos kilometros te entregaron el auto?</label>
                                <input type="number"
                                       class="form-control"
                                       id="kilometersDelivered"
                                       name="kilometersDelivered"
                                       placeholder="Ingresá la cantidad acá">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <g:submitButton class="btn btn-info float-left"
                                        name="Submit"
                                        value="Enviar"/>
                    </div>
                </g:form>
            </g:if>
            <g:else>
                <g:form name="notifyReturn" action="notifyReturn"
                        controller="rent"
                        id="${request?.rent?.id}">
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="form-group">
                                <label for="kilometersReturned">¿Con cuántos kilometros te devolvieron el auto?</label>
                                <input type="number"
                                       class="form-control"
                                       id="kilometersReturned"
                                       name="kilometersReturned"
                                       placeholder="Ingresá la cantidad acá">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <g:submitButton class="btn btn-info float-left"
                                        name="Submit"
                                        value="Enviar"/>
                    </div>
                </g:form>
            </g:else>
        </div>
    </div>
</div>

</body>
</html>
