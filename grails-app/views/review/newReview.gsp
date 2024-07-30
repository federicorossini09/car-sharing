<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Nueva Reseña</title>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
</head>

<g:set var="car" value="${request.publication.car}"/>
<g:set var="guest" value="${request.guest}"/>
<g:set var="errors" value="${flash.errors}"/>
<g:set var="values" value="${flash.values}"/>
<g:set var="errorMessageHelper" bean="errorMessageHelper"/>

<body>
<div class="container">

    <div class="row">
        <div class="col-sm-12 col-md-10 col-lg-8 mx-auto">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Nueva Reseña</h4>
                    <g:if test="${isGuestRequest || isHostPublication}">
                        <g:if test="${isGuestRequest}">
                            <h5>Para la publicación del ${car.brand} ${car.model} que solicitaste</h5>
                        </g:if>
                        <g:if test="${isHostPublication}">
                            <h5>Para ${guest.user.username.toUpperCase()}, el huésped de tu ${car.brand} ${car.model}</h5>
                        </g:if>
                        <g:form action="${isGuestRequest ? 'createPublicationReview' : isHostPublication ? 'createGuestReview' : null}">
                            <g:hiddenField name="requestId" value="${request.id}"/>
                            <g:hiddenField name="publicationId" value="${request.publication.id}"/>
                            <div class="form-group mt-3">
                                <g:set var="scoreError"
                                       value="${errors?.getFieldError("score")}"/>
                                <div class="rating">
                                    <input type="radio" name="score" value="5" id="5"><label
                                        for="5">☆</label>
                                    <input type="radio" name="score" value="4" id="4"><label
                                        for="4">☆</label>
                                    <input type="radio" name="score" value="3" id="3"><label
                                        for="3">☆</label>
                                    <input type="radio" name="score" value="2" id="2"><label
                                        for="2">☆</label>
                                    <input type="radio" name="score" value="1" id="1"><label
                                        for="1">☆</label>
                                </div>
                                <g:if test="${scoreError}">
                                    <div style="width: 100%; margin-top: 0.25rem; font-size: 80%; color: #dc3545;">
                                        ${errorMessageHelper.getMessage(scoreError)}
                                    </div>
                                </g:if>
                            </div>

                            <div class="form-group">
                                <g:set var="textError"
                                       value="${errors?.getFieldError("text")}"/>
                                <label for="text">Explicanos brevemente el motivo de tu puntaje</label>
                                <input type="text"
                                       class="form-control ${textError ? 'is-invalid' : ''}"
                                       id="text"
                                       name="text"
                                       value="${values?.text}"
                                       placeholder="Escribí acá">
                                <g:if test="${textError}">
                                    <div class="invalid-feedback">
                                        ${errorMessageHelper.getMessage(textError)}
                                    </div>
                                </g:if>
                            </div>
                            <g:submitButton class="btn btn-info float-right" name="Submit"
                                            value="Enviar"/>
                        </g:form>
                    </g:if>
                    <g:else>
                        <div class="alert alert-danger" role="alert">
                            Para hacer una reseña tenés que haber solicitado o ser el creador de la publicación
                        </div>
                    </g:else>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
