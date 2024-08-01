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
            flash.errorMessage = 'Es necesario indicar con cuántos kilometros recibiste el auto'
        } catch (KilometersDeliveredBelowPublishedException ignored) {
            flash.errorMessage = 'El kilometraje entregado no puede ser inferior al publicado'
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
            flash.errorMessage = 'Es necesario indicar con cuántos kilometros te devolvieron el auto'
        } catch (KilometersReturnedBelowDeliveredException ignored) {
            flash.errorMessage = 'El kilometraje en la devolución no puede ser inferior al indicado en la entrega'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def reportNotDelivered(params) {
        guestService.reportNotDelivered(Long.valueOf(params.id))
        flash.successMessage = 'Denuncia realizada'
        redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
    }

    def reportNotReturned(params) {
        hostService.reportNotReturned(Long.valueOf(params.id))
        flash.successMessage = 'Denuncia realizada'
        redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
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
