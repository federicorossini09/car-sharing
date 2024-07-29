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

        def request = guest.addRequest(publication, params.deliveryPlace, params.returnPlace, params.startDateTime, params.endDateTime).save(failOnError: true)

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
        guest.requests
    }

    def notifyDelivery(Long requestId) {
        def guest = getLoggedGuest()
        def request = requestService.getById(requestId)
        //TODO: receive real kilometers
        guest.reportSuccessfulDeliver(request, 10)
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
}
