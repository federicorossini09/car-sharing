package car.sharing

class AbstractController {


    def handleException(Exception exception) {
        flash.errorMessage = "Ocurrió un error inesperado"
        redirect(controller: 'home', action: 'index')
    }

}
