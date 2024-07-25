<html>
<head>
    <title>Registrate en Car Sharing</title>
    <asset:stylesheet src="application.css"/>
</head>

<body>
<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <h1 class="card-title text-center">Bienvenido a Car Sharing</h1>
        <div class="card card-signin my-5">
            <div class="card-body">
                <h5 class="card-title text-center">Registrate Acá</h5>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <form class="form-signin" action="registration" method="POST" id="loginForm" autocomplete="off">
                    <div class="form-group">
                        <label for="username">Nombre de Usuario</label>
                        <input type="text" placeholder="Nombre de Usuario" class="form-control" name="username" id="username" autocapitalize="none"/>
                    </div>

                    <div class="form-group">
                        <label for="password">Contraseña</label>
                        <input type="password" placeholder="Contraseña" class="form-control" name="password" id="password"/>
                    </div>

                    <div class="form-group">
                        <label for="password">Re-ingresa tu Contraseña</label>
                        <input type="password" placeholder="Re-ingresa tu Contraseña" class="form-control" name="repassword" id="repassword"/>
                    </div>

                    <button id="submit" class="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Registrate</button>
                    <hr class="my-4">
                    <p>¿Ya tenés una cuenta? <g:link controller="login" action="auth">Ingresá acá</g:link></p>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
  document.addEventListener("DOMContentLoaded", function(event) {
    document.forms['loginForm'].elements['username'].focus();
  });
</script>
</body>
</html>
