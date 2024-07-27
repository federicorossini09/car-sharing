<%@ page import="car.sharing.RequestStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Solicitudes a tu publicación</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<body>
<h3>Solicitudes para tu ${publication.car.brand} ${publication.car.model}</h3>

<div class="container container-fluid">
    <g:if test="${!requests.isEmpty()}">
        <div class="row">
            <g:each in="${requests}" var="request">
                <div class="col-sm-3 mx-3">
                    <div class="card car-sharing-card">
                        <div class="card-body">
                            <div class="mb-2">
                                <g:if test="${request.status == RequestStatus.ACCEPTED}">
                                    <span class="badge badge-success">Aceptada</span>
                                </g:if>
                                <g:if test="${request.status == RequestStatus.WAITING}">
                                    <span class="badge badge-warning">Esperando</span>
                                </g:if>
                                <g:if test="${request.status == RequestStatus.REJECTED}">
                                    <span class="badge badge-danger">Rechazada</span>
                                </g:if>
                            </div>

                            <div class="row mb-2">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primar mb-1">
                                        Entrega</div>

                                    <div class="mb-0 font-weight-bold text-gray-800">${request.startDateTime}</div>
                                </div>
                            </div>

                            <div class="row mb-2">
                                <div class="col mr-2">
                                    <div class="text-xs text-muted text-primary mb-1">
                                        Devolución</div>

                                    <div class="mb-0 font-weight-bold text-gray-800">${request.endDateTime}</div>
                                </div>
                            </div>

                            <g:link class="stretched-link" action="viewRequest"
                                    params="[requestId: request.id, publicationId: publication.id]"/>
                        </div>
                    </div>
                </div>
            </g:each>
        </div>
    </g:if>
    <g:else>
        Tu publicación aún no tiene solicitudes.
    </g:else>
</div>
</body>
</body>
</html>
