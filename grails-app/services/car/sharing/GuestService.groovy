package car.sharing

import grails.gorm.transactions.Transactional

@Transactional
class GuestService {

    UserService userService

    PublicationService publicationService

    RequestService requestService

    Guest getLoggedGuest() {
        def user = userService.getLoggedUser()
        Guest.findByUser(user)
    }

    def requestPublication(Long publicationId, params) {
        def publication = publicationService.getById(publicationId)
        def guest = getLoggedGuest()

        def request = guest.addRequest(publication,
                params.deliveryPlace, params.returnPlace, params.startDateTime, params.endDateTime,
                params.kilometers).save(failOnError: true)

        publication.save(failOnError: true)

        request
    }

    def isLoggedGuestRequest(Long requestId) {
        def guest = getLoggedGuest()
        def request = requestService.getById(requestId)
        guest.ownsRequest(request)
    }

    def getLoggedUserRequests() {
        def guest = getLoggedGuest();
        guest.requests.sort({ r1, r2 -> r1.id == r2.id ? 0 : r1.id < r2.id ? 1 : -1 })
    }

    def notifyDelivery(params) {
        def guest = getLoggedGuest()
        def request = requestService.getById(params.id)
        guest.reportSuccessfulDelivery(request, params.kilometersDelivered)
        request.save(failOnError: true)
    }

    def reportNotDelivered(Long requestId) {
        def guest = getLoggedGuest()
        def request = requestService.getById(requestId)
        def publication = request.publication
        guest.reportUndelivered(request, publication)
        publication.save(failOnError: true)
        request.save(failOnError: true)
    }

    def reviewPublication(Long requestId, params) {
        def guest = getLoggedGuest()
        def request = requestService.getById(requestId)

        def review = new Review(score: params.score, text: params.text, request: request)
        review.save(failOnError: true)

        guest.reviewPublication(request, review)
        request.save(failOnError: true)
    }

    def cancelFromGuest(Long requestId) {
        def guest = getLoggedGuest()
        def request = requestService.getById(requestId)
        guest.cancelRent(request)
    }
}
