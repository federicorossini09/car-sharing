package car.sharing

import car.sharing.exceptions.HostDoesNotOwnPublicationException

class Host {

    User user

    static hasMany = [publications: Publication]

    static constraints = {
    }

    Publication createPublication(Car car, Price price) {

        def newPublication = new Publication(car: car, price: price)

        this.addToPublications(newPublication)

        newPublication
    }

    def hostsPublication(Publication publication) {
        publications.any { it.isSameAs(publication) }
    }

    def checkHostsPublication(Publication publication) {
        if (!hostsPublication(publication))
            throw new HostDoesNotOwnPublicationException()
    }

    def updatePublicationPrice(Publication publication, BigDecimal newFinalValue) {
        this.checkHostsPublication(publication)
        publication.updatePrice(newFinalValue)
    }

    def publish(Publication publication) {
        this.checkHostsPublication(publication)
        publication.publish()
    }

    def unpublish(Publication publication) {
        this.checkHostsPublication(publication)
        publication.unpublish()
    }

    def acceptPublicationRequest(Publication publication, Request request) {
        this.checkHostsPublication(publication)
        publication.acceptRequest(request)
    }

    def rejectPublicationRequest(Request request, Publication publication) {
        this.checkHostsPublication(publication)
        publication.rejectRequest(request)
    }


    def hasUser(User user) {
        this.user.isSameAs(user)
    }

    void reportNotReturned(Request request, Publication publication) {
        this.checkHostsPublication(publication)
        request.reportNotReturned()
        //todo guest.penalize()
    }

    void reportSuccessfulReturn(Request request, Integer kilometers) {
        this.checkHostsPublication(request.publication)
        request.reportSuccessfulReturn(kilometers)
    }

    void doReview(Request request, Publication publication, Review review) {
        if (request.isFinished()) {
            publication.sendReview(review);
        }
    }
}
