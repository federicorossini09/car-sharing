package car.sharing

import car.sharing.exceptions.PublicationNotAvailableException
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import java.time.LocalDateTime

@Secured('isAuthenticated()')
class RequestController extends AbstractController {

    PublicationService publicationService

    RequestService requestService

    UserService userService

    GuestService guestService

    HostService hostService

    def newRequest(params) {
        [publication: publicationService.getById(Long.valueOf(params.publicationId))]
    }

    def createRequest(params) {
        try {
            def requestParams = parseRequestParams(params)
            def request = guestService.requestPublication(Long.valueOf(requestParams.publicationId), requestParams)
            flash.successMessage = "Solicitud realizada con éxito!"
            redirect(action: 'viewRequest', params: [requestId: request.id])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            redirect(action: 'newRequest', params: [publicationId: params.publicationId])
        } catch (PublicationNotAvailableException ignored) {
            flash.errorMessage = "El auto no está disponible en las fechas seleccionadas"
            flash.values = params
            redirect(action: 'newRequest', params: [publicationId: params.publicationId])
        }
    }

    def viewRequest(params) {
        def requestId = Long.valueOf(params.requestId)
        def request = requestService.getById(requestId)
        [request          : request,
         isGuestRequest   : guestService.isLoggedGuestRequest(requestId),
         isHostPublication: hostService.isLoggedHostPublication(request.publication.id)
        ]
    }

    def viewPublicationRequests(params) {
        //TODO: atrapar excepcion de que el host no es el dueño de la publicacion o que el host no existe
        [requests   : hostService.getMyPublicationRequests(Long.valueOf(params.publicationId)),
         publication: publicationService.getById(Long.valueOf(params.publicationId))
        ]
    }

    def accept(params) {
        try {
            hostService.acceptPublicationRequest(params.publicationId, params.id)
            flash.successMessage = "Solicitud aceptada"
            redirect(action: 'viewRequest', params: [requestId: params.id])
        } catch (ValidationException e) {
            //TODO: lanzar excpeciones especificas para distinguir cuál fue el motivo
            flash.errors = e.errors
            flash.errorMessage = "No se pudo aceptar la solicitud!"
            redirect(action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def reject(params) {
        hostService.rejectPublicationRequest(params.id)
        flash.successMessage = "Solicitud rechazada"
        redirect(action: 'viewRequest', params: [requestId: params.id])
    }

    def viewMyRequests() {
        [requests: guestService.getLoggedUserRequests()]
    }

    def parseRequestParams(params) {
        params.startDateTime = params.startDateTime ? LocalDateTime.parse(params.startDateTime) : null
        params.endDateTime = params.endDateTime ? LocalDateTime.parse(params.endDateTime) : null
        params.kilometers = params.kilometers ? Integer.valueOf(params.kilometers) : null
        params
    }
}
