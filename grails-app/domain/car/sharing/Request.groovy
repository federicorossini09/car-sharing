package car.sharing

import car.sharing.exceptions.RentIsNotActiveException
import car.sharing.exceptions.RentNotReturnedNotAvailableException
import car.sharing.exceptions.RentNotScheduledException
import car.sharing.exceptions.RentUndeliverNotAvailableException

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Request {

    String deliveryPlace
    String returnPlace
    LocalDateTime startDateTime
    LocalDateTime endDateTime
    static belongsTo = [guest: Guest, publication: Publication]
    Rent rent
    RequestStatus status = RequestStatus.WAITING


    def accept() {
        this.setStatus(RequestStatus.ACCEPTED)
        this.rent = new Rent()
    }

    def reject() {
        this.setStatus(RequestStatus.REJECTED)
    }

    def dateCollision(LocalDateTime date) {
        return date >= this.startDateTime && date <= this.endDateTime
    }

    static constraints = {
        deliveryPlace blank: false
        returnPlace blank: false
        startDateTime blank: false, validator: { val, obj -> val < obj.endDateTime }
        endDateTime blank: false, validator: { val, obj -> val > obj.startDateTime }
        rent nullable: true
    }

    boolean isOccupying(LocalDateTime startDate, LocalDateTime endDate) {
        return this.rent && this.rent.isScheduledOrActive()
                && (this.dateCollision(startDate) | this.dateCollision(endDate))
    }

    def isSameAs(Request request) {
        request.id == this.id
    }

    void reportUndelivered() {
        if (!this.rent.isScheduled()) {
            throw new RentNotScheduledException()
        } else if (!this.couldReportUndeliver()) {
            throw new RentUndeliverNotAvailableException()
        }
        rent.reportUndelivered()


    }

    def reportSuccessfulDeliver(Integer currentKilometers) {
        rent.activate(currentKilometers)
    }

    def reportNotReturned() {
        if (!this.rent.isActive()) {
            throw new RentIsNotActiveException()
        } else if (!this.couldReportNotReturned()) {
            throw new RentNotReturnedNotAvailableException()
        }
        rent.reportNotReturned()
    }

    def reportSuccessfulReturn(Integer kilometers) {
        //todo penalizar al guest si aca nos pasamos
        rent.finish(kilometers)
        //todo publication actualizar precio
    }

    def isFinished() {
        this.rent.isFinished()
    }

    def cancelFromHost() {
        this.rent.cancelFromHost()
    }

    def cancelFromGuest() {
        this.rent.cancelFromGuest(this.startDateTime)
    }

    def couldReportUndeliver() {
        def now = LocalDateTime.now()
        long hourDifference = ChronoUnit.HOURS.between(this.startDateTime, now)
        return hourDifference > 2
    }

    def couldReportNotReturned() {
        def now = LocalDateTime.now()
        long hourDifference = ChronoUnit.HOURS.between(this.endDateTime, now)
        return hourDifference > 2
    }
}
