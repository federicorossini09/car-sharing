package car.sharing

import java.time.LocalDate

class Guest {

    User user
    List requests = []
    static hasMany = [requests: Request]
    Score score = new Score(value: 100)
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
        publication.penalize()
    }

    void reportSuccessfulDeliver(Request request) {
        request.reportSuccessfulDeliver()

    }

    boolean cancelRent(Rent rent) {
        rent.cancel()
    }
}
