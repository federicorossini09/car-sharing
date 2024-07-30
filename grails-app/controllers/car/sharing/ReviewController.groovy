package car.sharing

import car.sharing.exceptions.GuestCannotBeReviewedYet
import car.sharing.exceptions.PublicationCannotBeReviewedYet
import car.sharing.exceptions.ReviewAlreadySent
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

@Secured('isAuthenticated()')
class ReviewController {

    GuestService guestService

    HostService hostService

    RequestService requestService

    def newReview(params) {
        def requestId = Long.valueOf(params.requestId)
        def request = requestService.getById(requestId)
        def isGuestRequest = guestService.isLoggedGuestRequest(requestId)
        def isHostPublication = hostService.isLoggedHostPublication(request.publication.id)
        [request: request, isGuestRequest: isGuestRequest, isHostPublication: isHostPublication]
    }

    def createPublicationReview(params) {
        def requestId = Long.valueOf(params.requestId)
        try {
            guestService.reviewPublication(Long.valueOf(params.requestId), params)
            flash.successMessage = 'Reseña realizada'
            redirect(controller: 'publication', action: 'viewPublication', params: [id: params.publicationId])
        } catch (PublicationCannotBeReviewedYet ignored) {
            flash.errorMessage = 'La renta debe estar finalizada para realizar una reseña de la publicación'
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.requestId])
        } catch (ReviewAlreadySent ignored) {
            flash.errorMessage = 'Ya enviaste una reseña para esta publicación'
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.requestId])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            def isGuestRequest = guestService.isLoggedGuestRequest(requestId)
            def request = requestService.getById(requestId)
            def isHostPublication = hostService.isLoggedHostPublication(request.publication.id)
            redirect(action: 'newReview', controller: 'review', params: [requestId: requestId, isGuestRequest: isGuestRequest, isHostPublication: isHostPublication])
        }
    }

    def createGuestReview(params) {
        def requestId = Long.valueOf(params.requestId)
        try {
            hostService.reviewGuest(requestId, params)
            flash.successMessage = 'Reseña realizada'
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.requestId])
        } catch (GuestCannotBeReviewedYet ignored) {
            flash.errorMessage = 'La renta debe estar finalizada para realizar una reseña del huésped'
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.requestId])
        } catch (ReviewAlreadySent ignored) {
            flash.errorMessage = 'Ya enviaste una reseña para el huésped'
            redirect(controller: 'request', action: 'viewRequest', params: [requestId: params.requestId])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            def isGuestRequest = guestService.isLoggedGuestRequest(requestId)
            def request = requestService.getById(requestId)
            def isHostPublication = hostService.isLoggedHostPublication(request.publication.id)
            redirect(action: 'newReview', controller: 'review', params: [requestId: requestId, isGuestRequest: isGuestRequest, isHostPublication: isHostPublication])
        }
    }
}
