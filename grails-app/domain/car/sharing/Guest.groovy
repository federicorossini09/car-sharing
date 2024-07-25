package car.sharing

import java.time.LocalDate

class Guest {

    User user
    List requests = []
    static hasMany = [requests: Request]
    static constraints = {
    }

    Publication createFirstPublication(Car car) {

        def newHost = new Host(user: this.user)

        newHost.createPublication(car)
    }

    void addRequest(Publication publication, String deliveryPlace, String returnPlace, LocalDate startDate, LocalDate endDate) {
        if (publication.areDatesAvailable(startDate, endDate)) {
            def newRequest = new Request(deliveryPlace: deliveryPlace, returnPlace: returnPlace, startDate: startDate, endDate: endDate, guest: this)
            publication.addRequest(newRequest)
            requests<<newRequest
        }
    }

    boolean cancelRent(Rent rent) {
        rent.cancel()
    }
}
