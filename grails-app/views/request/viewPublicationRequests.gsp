<%@ page import="car.sharing.RequestStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Solicitudes a tu publicación</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<body>

<g:if test="${error}">
    <div class="row mt-5">
        <div class="col d-flex justify-content-center">
            <div class="alert alert-warning" role="alert">
                ${error}
            </div>
        </div>
    </div>
</g:if>
<g:else>
    <span class="text-muted">Publicación #${publication.id}</span>

    <h3>Solicitudes para tu ${publication.car.brand.toUpperCase()} ${publication.car.model.toUpperCase()}</h3>

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
                                        params="[requestId: request.id]"/>
                            </div>
                        </div>
                    </div>
                </g:each>
            </div>
        </g:if>
        <g:else>
            <div class="row mt-5">
                <div class="col d-flex justify-content-center">
                    <div class="alert alert-warning" role="alert">
                        Aún no hay solicitudes
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col d-flex justify-content-center">
                    <g:link name="publish" controller="publication" action="viewPublication"
                            params="[id: publication.id]">
                        <button class="btn btn-info float-left">Ver Publicación</button>
                    </g:link>
                </div>
            </div>
        </g:else>
    </div>

</g:else>

</body>
</html>
