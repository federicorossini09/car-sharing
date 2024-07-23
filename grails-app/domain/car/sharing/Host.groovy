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

    def updatePublicationPrice(Publication publication, BigDecimal newFinalValue) {
        this.ownsPublication(publication)
        publication.updatePrice(newFinalValue)
    }

    def ownsPublication(Publication publication) {
        if (publication.host.user.username != this.user.username)
            throw new HostDoesNotOwnPublicationException()
    }

    def publish(Publication publication) {
        this.ownsPublication(publication)
        publication.publish()
    }
}
