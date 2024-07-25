package car.sharing

import java.time.LocalDate

class Request {

    String deliveryPlace
    String returnPlace
    LocalDate startDate
    LocalDate endDate
    static belongsTo = [guest: Guest]
    Rent rent
    RequestStatus status = RequestStatus.WAITING




    def accept() {
        this.status = RequestStatus.ACCEPTED
        this.rent = new Rent()
    }


    def dateCollision(LocalDate date) {
        return date >= this.startDate && date <= this.endDate
    }

    static constraints = {
        deliveryPlace blank: false
        returnPlace blank: false
        startDate blank: false
        endDate blank: false
        rent nullable: true
    }

    boolean isOccupying(LocalDate startDate, LocalDate endDate) {
        return this.rent && this.rent.isScheduledOrActive()
                && (this.dateCollision(startDate) | this.dateCollision(endDate))
    }
}
