package car.sharing

import car.sharing.exceptions.GuestCannotBeReviewedYet
import car.sharing.exceptions.PublicationCannotBeReviewedYet

import java.time.LocalDateTime

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
        /*
        if (this.isNotScheduled()) {
            //todo throw error porque tiene que estar programada
        } else if (this.couldReportUndeliver()) {
            //todo throw error porque tiene que esperar cierto tiempo
        }
        //todo chequear que este en estado cancelable

         */
        rent.cancel()


    }

    def reportSuccessfulDeliver(Integer currentKilometers) {
        rent.activate(currentKilometers)
    }

    def reportNotReturned() {
        /*if (this.isNotActive()) {
            //todo throw error porque tiene que estar activa
        } else if (this.couldReportNotReturned()) {
            //todo throw error porque tiene que
        } else if (!rent.isActive()) {
            //todo validar qeu este activa
        }*/
        rent.cancel()
    }

    def reportSuccessfulReturn(Integer kilometers) {
        //todo penalizar al guest si aca nos pasamos
        rent.finish(kilometers)
        //todo publication actualizar precio
    }

    def isFinished() {
        this.rent.isFinished()
    }

    def sendPublicationReview(Review review) {
        if (!this.rent.isFinished()) {
            throw new PublicationCannotBeReviewedYet()
        }
        this.publication.receiveReview(review)
    }

    def sendGuestReview(Review review) {
        if (!this.rent.isFinished()) {
            throw new GuestCannotBeReviewedYet()
        }
        this.guest.receiveReview(review)
    }
}
