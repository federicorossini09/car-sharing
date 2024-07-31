<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nueva Solicitud</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<g:set var="errors" value="${flash.errors}"/>
<g:set var="values" value="${flash.values}"/>
<g:set var="errorMessageHelper" bean="errorMessageHelper"/>

<body>

<h3>Crear Solicitud para ${publication.car.brand} ${publication.car.model}</h3>
<br>

<div class="container">
    <div class="row">
        <div class="col mx-auto">
            <div class="card">
                <div class="card-body">
                    <g:if test='${flash.errorMessage}'>
                        <div class="alert alert-danger" role="alert">
                            ${flash.errorMessage}
                        </div>
                    </g:if>
                    <g:form name="createRequest" action="createRequest">
                        <g:hiddenField name="publicationId" value="${publication.id}"/>
                        <div class="row">
                            <div class="col">
                                <div class="form-group">
                                    <g:set var="kilometersError"
                                           value="${errors?.getFieldError("kilometers")}"/>
                                    <label for="kilometers">¿Cuántos kilometros vas a utilizar el auto?</label>
                                    <input type="number"
                                           class="form-control ${kilometersError ? 'is-invalid' : ''}"
                                           id="kilometers"
                                           name="kilometers"
                                           value="${values?.kilometers}"
                                           placeholder="Ingresá la cantidad acá">
                                    <g:if test="${kilometersError}">
                                        <div class="invalid-feedback">
                                            ${errorMessageHelper.getMessage(kilometersError)}
                                        </div>
                                    </g:if>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col border-right">
                                <h5>Entrega</h5>

                                <div class="form-group">
                                    <g:set var="deliveryPlaceError"
                                           value="${errors?.getFieldError("deliveryPlace")}"/>
                                    <label for="deliveryPlace">Lugar</label>
                                    <input type="text"
                                           class="form-control ${deliveryPlaceError ? 'is-invalid' : ''}"
                                           id="deliveryPlace"
                                           name="deliveryPlace"
                                           value="${values?.deliveryPlace}"
                                           placeholder="Punto de encuentro para la entrega del auto">
                                    <g:if test="${deliveryPlaceError}">
                                        <div class="invalid-feedback">
                                            ${errorMessageHelper.getMessage(deliveryPlaceError)}
                                        </div>
                                    </g:if>
                                </div>

                                <div class="form-group">
                                    <g:set var="startDateTimeError"
                                           value="${errors?.getFieldError("startDateTime")}"/>
                                    <label for="startDateTime">Fecha y Hora</label>
                                    <input type="datetime-local"
                                           class="form-control ${startDateTimeError ? 'is-invalid' : ''}"
                                           id="startDateTime" name="startDateTime"
                                           value="${values?.startDateTime}"
                                           placeholder="">
                                    <g:if test="${startDateTimeError}">
                                        <div class="invalid-feedback">
                                            ${errorMessageHelper.getMessage(startDateTimeError)}
                                        </div>
                                    </g:if>
                                </div>

                            </div>

                            <div class="col">
                                <h5>Devolución</h5>

                                <div class="form-group">
                                    <g:set var="returnPlaceError"
                                           value="${errors?.getFieldError("returnPlace")}"/>
                                    <label for="returnPlace">Lugar</label>
                                    <input type="text"
                                           class="form-control ${returnPlaceError ? 'is-invalid' : ''}"
                                           id="returnPlace"
                                           name="returnPlace"
                                           value="${values?.returnPlace}"
                                           placeholder="Punto de encuentro para la devolución del auto">
                                    <g:if test="${returnPlaceError}">
                                        <div class="invalid-feedback">
                                            ${errorMessageHelper.getMessage(returnPlaceError)}
                                        </div>
                                    </g:if>
                                </div>

                                <div class="form-group">
                                    <g:set var="endDateTimeError"
                                           value="${errors?.getFieldError("endDateTime")}"/>
                                    <label for="endDateTime">Fecha y Hora</label>
                                    <input type="datetime-local"
                                           class="form-control ${endDateTimeError ? 'is-invalid' : ''}"
                                           id="endDateTime" name="endDateTime"
                                           value="${values?.endDateTime}"
                                           placeholder="">
                                    <g:if test="${endDateTimeError}">
                                        <div class="invalid-feedback">
                                            ${errorMessageHelper.getMessage(endDateTimeError)}
                                        </div>
                                    </g:if>
                                </div>

                            </div>
                        </div>
                        <g:submitButton class="btn btn-info float-right" name="Submit"
                                        value="Solicitar"/>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
