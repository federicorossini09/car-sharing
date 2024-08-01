package car.sharing

import car.sharing.exceptions.HostNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class HostService {

    UserService userService

    PublicationService publicationService

    RequestService requestService

    Host createHost() {
        User user = userService.getLoggedUser()
        new Host(user: user).save()
    }

    Host getLoggedHost() {

        User user = userService.getLoggedUser()

        def host = Host.findByUser(user)

        host
    }

    def getLoggedUserPublications() {

        def host = getLoggedHost()

        if (!host)
            throw new HostNotFoundException()

        host.publications.sort({ r1, r2 -> r1.id == r2.id ? 0 : r1.id < r2.id ? 1 : -1 })
    }

    Publication createPublication(params) {
        def car = new Car(params)
        car.save(failOnError: true)

        def price = new Price(car.year, car.kilometers)
        price.save(failOnError: true)

        def host = getLoggedHost() ?: createHost()

        def newPublication = host.createPublication(car, price)
        newPublication.save(failOnError: true)
    }

    def setPublicationPrice(Long publicationId, BigDecimal newValue) {
        def publication = publicationService.getById(publicationId)
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        publication = host.updatePublicationPrice(publication, newValue)
        publication.save(failOnError: true)
    }

    def publish(Long publicationId) {
        def host = getLoggedHost()
        def publication = publicationService.getById(publicationId)
        host.publish(publication)
    }

    def unpublish(Long publicationId) {
        def host = getLoggedHost()
        def publication = publicationService.getById(publicationId)
        host.unpublish(publication)
    }

    def acceptPublicationRequest(requestId) {
        def host = getLoggedHost()
        def request = requestService.getById(requestId)
        def publication = request.publication

        host.acceptPublicationRequest(publication, request)

        request.save(failOnError: true)
        publication.save(failOnError: true)
    }

    def rejectPublicationRequest(Long requestId) {
        def host = getLoggedHost()
        if (!host) {
            throw new HostNotFoundException()
        }
        def request = requestService.getById(requestId)
        host.rejectPublicationRequest(request, request.publication)
    }


    def getMyPublicationRequests(publicationId) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def publication = publicationService.getById(publicationId)
        host.checkHostsPublication(publication)
        publication.requests.sort({ r1, r2 -> r1.id == r2.id ? 0 : r1.id < r2.id ? 1 : -1 })
    }

    def isLoggedHostPublication(Long publicationId) {
        def host = getLoggedHost()
        if (!host) {
            false
        } else {
            def publication = publicationService.getById(publicationId)
            host.hostsPublication(publication)
        }
    }

    def notifyReturn(params) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def request = requestService.getById(params.id)
        host.reportSuccessfulReturn(request, params.kilometersReturned)
        request.guest.save(failOnError: true)
        request.publication.save(failOnError: true)
        request.save(failOnError: true)
    }

    def reportNotReturned(Long requestId) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def request = requestService.getById(requestId)
        def publication = request.publication
        host.reportNotReturned(request, publication)
        publication.save(failOnError: true)
        request.save(failOnError: true)
    }

    def reviewGuest(Long requestId, params) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def request = requestService.getById(requestId)

        def review = new Review(score: params.score, text: params.text, request: request)
        review.save(failOnError: true)

        host.reviewGuest(request, review)
        request.save(failOnError: true)
    }

    def cancelFromHost(Long requestId) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def request = requestService.getById(requestId)
        host.cancelRent(request)
    }
}
