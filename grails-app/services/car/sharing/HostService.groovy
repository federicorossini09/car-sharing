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

        host.publications
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


    def acceptPublicationRequest(publicationId, requestId) {
        def host = getLoggedHost()
        def publication = publicationService.getById(publicationId)
        def request = requestService.getById(requestId)

        host.acceptPublicationRequest(publication, request)

        request.save()
        publication.save()
    }

    def getMyPublicationRequests(publicationId) {
        def host = getLoggedHost()
        if (!host)
            throw new HostNotFoundException()
        def publication = publicationService.getById(publicationId)
        host.checkHostsPublication(publication)
        publication.requests
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

}
