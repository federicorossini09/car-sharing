package car.sharing

import car.sharing.exceptions.HostCannotRequestHisPublication

import java.time.LocalDateTime

class Guest {

    User user
    static hasMany = [requests: Request]
    Score score = new Score()
    static constraints = {
    }

    def addRequest(Publication publication, String deliveryPlace, String returnPlace, LocalDateTime startDate, LocalDateTime endDate) {

        checkUserCanRequestPublication(publication)

        //TODO: tal vez esta validacion es mejor que esté dentro del método "publication.addRequest" asi siempre queda adentro de publication
        if (publication.areDatesAvailable(startDate, endDate)) {
            def newRequest = new Request(deliveryPlace: deliveryPlace, returnPlace: returnPlace, startDateTime: startDate, endDateTime: endDate, guest: this)
            publication.addRequest(newRequest)
            requests << newRequest
            newRequest
        }
    }

    def checkUserCanRequestPublication(Publication publication) {
        if (publication.isHostedBy(this.user)) {
            throw new HostCannotRequestHisPublication()
        }
    }

    void reportUndelivered(Request request, Publication publication) {
        request.reportUndelivered()
        // todo solo puedo reportar si yo soy el guest
        //todo esto de abajo deberia ir en el metodo de arriba
        publication.penalize(PenaltyReason.NotDeliverOnTime)
    }

    void reportSuccessfulDeliver(Request request, Integer currentKilometers) {
        request.reportSuccessfulDeliver(currentKilometers)

    }

    boolean cancelRent(Rent rent) {
        rent.cancel()
    }

    def ownsRequest(Request request) {
        requests.any { it.isSameAs(request) }
    }
}
