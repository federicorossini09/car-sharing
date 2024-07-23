<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="${'main'}"/>
</head>

<g:set var="errors" value="${flash.errors}"/>
<g:set var="values" value="${flash.values}"/>
<g:set var="errorMessageHelper" bean="errorMessageHelper"/>

<body>
<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-10 col-lg-8 mx-auto">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Precio</h4>
                    <span>Antes de publicar tu auto, indicá el precio del alquiler diario.</span>
                    <br>
                    <h5>Valor mínimo: u$s${publication.price.minimumValue} - Valor Máximo: u$s${publication.price.maximumValue}</h5>
                    <g:form name="setPublicationPrice" action="setPublicationPrice"
                            id="${publication.id}">
                        <g:set var="finalValueError"
                               value="${errors?.getFieldError("price.finalValue")}"/>
                        <div class="form-group">
                            <label for="finalValue">Ingresá el Precio Final</label>
                            <input type="number"
                                   class="form-control ${finalValueError ? 'is-invalid' : ''}"
                                   id="finalValue" name="finalValue" value="${values?.finalValue}"
                                   placeholder="Entre los valores mínimo y máximo">
                            <g:if test="${finalValueError}">
                                <div class="invalid-feedback">
                                    ${errorMessageHelper.getMessage(finalValueError)}
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
