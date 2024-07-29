package car.sharing

import car.sharing.exceptions.RentAlreadyActiveException
import car.sharing.exceptions.RentAlreadyFinishedException
import car.sharing.exceptions.RentCannotBeActivatedException
import car.sharing.exceptions.RentCannotBeFinishedException
import grails.plugin.springsecurity.annotation.Secured

@Secured('isAuthenticated()')
class RentController {

    HostService hostService

    GuestService guestService

    def notifyDelivery(params) {
        try {
            guestService.notifyDelivery(Long.valueOf(params.id))
            flash.successMessage = 'Entrega notificada'
        } catch (RentCannotBeActivatedException ignored) {
            flash.errorMessage = 'La renta debe estar programada para poder notificar la entrega'
        } catch (RentAlreadyActiveException ignored) {
            flash.errorMessage = 'La renta ya se encuentra en curso'
        } finally {
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.id])
        }
    }

    def notifyReturn(params) {
        try {
            hostService.notifyReturn(Long.valueOf(params.id))
            flash.successMessage = 'Devolución notificada'
        } catch (RentCannotBeFinishedException ignored) {
            flash.errorMessage = 'La renta debe estar en curso para poder notificar la devolución'
        } catch (RentAlreadyFinishedException ignored) {
            flash.errorMessage = 'La renta ya se encuentra finalizada'
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
}
