package car.sharing

import car.sharing.exceptions.*
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
        } catch (CarVtvExpired ignored) {
            flash.errorMessage = "La VTV se vencerá en las fechas solicitadas."
        } catch (PublicationNotAvailableException ignored) {
            flash.errorMessage = "El auto no está disponible en las fechas seleccionadas"
            flash.values = params
            redirect(action: 'newRequest', params: [publicationId: params.publicationId])
        } catch (HostCannotRequestHisPublication ignored) {
            flash.errorMessage = "No podés solicitar una publicación propia"
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
        try {
            [requests   : hostService.getMyPublicationRequests(Long.valueOf(params.publicationId)),
             publication: publicationService.getById(Long.valueOf(params.publicationId))
            ]
        } catch (HostNotFoundException | HostDoesNotOwnPublicationException ignored) {
            return [error: 'No tenés permiso para ver este contenido']
        }
    }

    def accept(params) {
        try {
            hostService.acceptPublicationRequest(params.id)
            flash.successMessage = "Solicitud aceptada"
        } catch (PublicationNotAvailableException ignored) {
            flash.errorMessage = "No se puede aceptar la solicitud. El auto ya no está disponible en las fechas solicitadas."
        } catch (RequestExpiredException ignored) {
            flash.errorMessage = "La solicitud ya está vencida."
        } catch (CarVtvExpired ignored) {
            flash.errorMessage = "La VTV se vencerá en las fechas solicitadas."
        } catch (HostNotFoundException | HostDoesNotOwnPublicationException ignored) {
            flash.errorMessage = "No tenés permiso para realizar esta acción"
        } finally {
            redirect(action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def reject(params) {
        try {
            hostService.rejectPublicationRequest(Long.valueOf(params.id))
            flash.successMessage = "Solicitud rechazada"
        } catch (RequestCannotBeRejectedException ignored) {
            flash.errorMessage = 'La solicitud solo puede ser rechazada en estado Esperando'
        } catch (HostNotFoundException | HostDoesNotOwnPublicationException ignored) {
            flash.errorMessage = "No tenés permiso para realizar esta acción"
        } finally {
            redirect(action: 'viewRequest', params: [requestId: params.id])
        }
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
