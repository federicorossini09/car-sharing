package car.sharing

import car.sharing.exceptions.*
import grails.plugin.springsecurity.annotation.Secured

@Secured('isAuthenticated()')
class RentController extends AbstractController {

    HostService hostService

    GuestService guestService

    def notifyDelivery(params) {
        try {
            params = parseNotifyDeliveryParams(params)
            guestService.notifyDelivery(params)
            flash.successMessage = 'Entrega notificada'
        } catch (RentCannotBeActivatedException ignored) {
            flash.errorMessage = 'La renta debe estar programada para poder notificar la entrega'
        } catch (RentAlreadyActiveException ignored) {
            flash.errorMessage = 'La renta ya se encuentra en curso'
        } catch (DeliveryNotifiedWithoutKilometersException ignored) {
            flash.errorMessage = 'Es necesario indicar con cuántos kilómetros recibiste el auto'
        } catch (KilometersDeliveredBelowPublishedException ignored) {
            flash.errorMessage = 'El kilometraje entregado no puede ser inferior al publicado'
        } catch (GuestDoesNotOwnsRequestException ignored) {
            flash.errorMessage = 'No tenés permiso para realizar esta acción'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def notifyReturn(params) {
        try {
            params = parseNotifyReturnParams(params)
            hostService.notifyReturn(params)
            flash.successMessage = 'Devolución notificada'
        } catch (RentCannotBeFinishedException ignored) {
            flash.errorMessage = 'La renta debe estar en curso para poder notificar la devolución'
        } catch (RentAlreadyFinishedException ignored) {
            flash.errorMessage = 'La renta ya se encuentra finalizada'
        } catch (ReturnNotifiedWithoutKilometersException ignored) {
            flash.errorMessage = 'Es necesario indicar con cuántos kilómetros te devolvieron el auto'
        } catch (KilometersReturnedBelowDeliveredException ignored) {
            flash.errorMessage = 'El kilometraje en la devolución no puede ser inferior al indicado en la entrega'
        } catch (HostNotFoundException ignored) {
            flash.errorMessage = 'No tenés permiso para realizar esta acción'
        } catch (RentNotExistsException ignored) {
            flash.errorMessage = 'La renta no existe'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def reportNotDelivered(params) {
        try {
            guestService.reportNotDelivered(Long.valueOf(params.id))
            flash.successMessage = 'Denuncia realizada'
        } catch (GuestDoesNotOwnsRequestException ignored) {
            flash.errorMessage = 'No tenés permiso para realizar esta acción'
        } catch (RentNotExistsException ignored) {
            flash.errorMessage = 'La renta no existe'
        } catch (RentNotScheduledException ignored) {
            flash.errorMessage = 'Para realizar la denuncia la renta debe estar en estado Programada'
        } catch (RentUndeliverNotAvailableException ignored) {
            flash.errorMessage = 'Todavía no se puede realizar la denuncia'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def reportNotReturned(params) {
        try {
            hostService.reportNotReturned(Long.valueOf(params.id))
            flash.successMessage = 'Denuncia realizada'
        } catch (RentNotReturnedNotAvailableException ignored) {
            flash.errorMessage = 'Todavía no se puede realizar la denuncia'
        } catch (RentIsNotActiveException ignored) {
            flash.errorMessage = 'La renta debe estar activa para poder realizar la denuncia'
        } catch (HostNotFoundException ignored) {
            flash.errorMessage = 'No tenés permiso para realizar esta acción'
        } catch (RentNotExistsException ignored) {
            flash.errorMessage = 'La renta no existe'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def cancelFromGuest(params) {
        try {
            guestService.cancelFromGuest(Long.valueOf(params.id))
            flash.successMessage = 'Renta cancelada'
        } catch (RentTooLateToCancelException ignored) {
            flash.errorMessage = 'Ya no se puede cancelar la renta'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def cancelFromHost(params) {
        try {
            hostService.cancelFromHost(Long.valueOf(params.id))
            flash.successMessage = 'Renta cancelada'
        } catch (RentNotScheduledException ignored) {
            flash.errorMessage = 'Solo podés cancelar la renta cuando se encuentra programada'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def parseNotifyDeliveryParams(params) {
        params.id = Long.valueOf(params.id)
        params.kilometersDelivered = params.kilometersDelivered ? Integer.valueOf(params.kilometersDelivered) : null
        params
    }

    def parseNotifyReturnParams(params) {
        params.id = Long.valueOf(params.id)
        params.kilometersReturned = params.kilometersReturned ? Integer.valueOf(params.kilometersReturned) : null
        params
    }
}
