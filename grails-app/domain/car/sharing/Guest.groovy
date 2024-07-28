package car.sharing

import java.time.LocalDate

class Guest {

    User user
    List requests = []
    static hasMany = [requests: Request]
    Score score = new Score()
    static constraints = {
    }

    void addRequest(Publication publication, String deliveryPlace, String returnPlace, LocalDate startDate, LocalDate endDate) {
        if (publication.areDatesAvailable(startDate, endDate)) {
            def newRequest = new Request(deliveryPlace: deliveryPlace, returnPlace: returnPlace, startDate: startDate, endDate: endDate, guest: this)
            publication.addRequest(newRequest)
            requests<<newRequest
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
}
