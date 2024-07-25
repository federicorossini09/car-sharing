<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Crear Publicación</title>
    <meta name="layout" content="${'main'}"/>
</head>

<g:set var="errors" value="${flash.errors}"/>
<g:set var="values" value="${flash.values}"/>
<g:set var="errorMessageHelper" bean="errorMessageHelper"/>

<body>

<h3>Crear Publicación</h3>
<br>

<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-10 col-lg-8 mx-auto">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Datos del Auto</h4>
                    <g:if test='${errors}'>
                        <div class="alert alert-danger" role="alert">
                            No se pudo crear la Publicación! Revisá los datos y volvé a intentar
                        </div>
                    </g:if>
                    <g:form name="createPublication" action="createPublication">
                        <div class="form-group">
                            <g:set var="yearError"
                                   value="${errors?.getFieldError("year")}"/>
                            <label for="year">Año</label>
                            <input type="number"
                                   class="form-control ${yearError ? 'is-invalid' : ''}"
                                   id="year" name="year"
                                   value="${values?.year}"
                                   placeholder="El año de tu vehículo">
                            <g:if test="${yearError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(yearError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="brandError"
                                   value="${errors?.getFieldError("brand")}"/>
                            <label for="brand">Marca</label>
                            <g:select name="brand" from="${brands}" optionValue="${{ it }}"
                                      multiple="false"
                                      class="form-control ${brandError ? 'is-invalid' : ''}"
                                      value="${values?.brand}"/>
                            <g:if test="${brandError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(brandError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="modelError"
                                   value="${errors?.getFieldError("model")}"/>
                            <label for="model">Modelo</label>
                            <input type="text"
                                   class="form-control ${modelError ? 'is-invalid' : ''}" id="model"
                                   name="model"
                                   value="${values?.model}"
                                   placeholder="Ej: Focus, Corolla, Golf">
                            <g:if test="${modelError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(modelError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="variantError"
                                   value="${errors?.getFieldError("variant")}"/>
                            <label for="variant">Variante</label>
                            <input type="text"
                                   class="form-control ${variantError ? 'is-invalid' : ''}"
                                   id="variant" name="variant"
                                   value="${values?.variant}"
                                   placeholder="Ej: Titanium, XEI, Comfortline">
                            <g:if test="${variantError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(variantError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="kilometersError"
                                   value="${errors?.getFieldError("kilometers")}"/>
                            <label for="kilometers">Kilometraje</label>
                            <input type="number"
                                   class="form-control ${kilometersError ? 'is-invalid' : ''}"
                                   id="kilometers" name="kilometers"
                                   value="${values?.kilometers}"
                                   placeholder="">
                            <g:if test="${kilometersError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(kilometersError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="licensePlateError"
                                   value="${errors?.getFieldError("licensePlate")}"/>
                            <label for="licensePlate">Patente</label>
                            <input type="text"
                                   class="form-control ${kilometersError ? 'is-invalid' : ''}"
                                   id="licensePlate" name="licensePlate"
                                   value="${values?.licensePlate}"
                                   placeholder="">
                            <g:if test="${licensePlateError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(licensePlateError)}
                                </div>
                            </g:if>
                        </div>

                        <div class="form-group">
                            <g:set var="vtvExpirationDateError"
                                   value="${errors?.getFieldError("vtvExpirationDate")}"/>
                            <label for="vtvExpirationDate">Fecha de expiración de la VTV</label>
                            <input type="date"
                                   class="form-control ${vtvExpirationDateError ? 'is-invalid' : ''}"
                                   id="vtvExpirationDate" name="vtvExpirationDate"
                                   value="${values?.vtvExpirationDate}"
                                   placeholder="">
                            <g:if test="${vtvExpirationDateError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(vtvExpirationDateError)}
                                </div>
                            </g:if>
                        </div>
                        <g:submitButton class="btn btn-info float-right" name="Submit"
                                        value="Continuar"/>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>
