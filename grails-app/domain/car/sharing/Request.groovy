package car.sharing

import car.sharing.exceptions.GuestCannotBeReviewedYet
import car.sharing.exceptions.PublicationCannotBeReviewedYet
import car.sharing.exceptions.RentNotExistsException

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
    Integer kilometers


    def accept() {
        this.setStatus(RequestStatus.ACCEPTED)
        def totalPrice = new TotalPrice(this.calculateDays(), publication.pricePerDay(), this.kilometers, this.guest.isFeatured())
        this.rent = new Rent(totalPrice: totalPrice)
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
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        rent.reportUndelivered(this.startDateTime)
    }

    def reportSuccessfulDeliver(Integer kilometersDelivered) {
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        publication.checkKilometersDelivered(kilometersDelivered)
        rent.activate(kilometersDelivered)
    }

    def reportNotReturned() {
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        guest.penalize(PenaltyReason.NotReturnOnTime)
        rent.reportNotReturned(this.endDateTime)
    }

    def reportSuccessfulReturn(Integer kilometersReturned) {
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        rent.finish(kilometersReturned)
        if (rent.getKilometersDriven() > this.kilometers)
            guest.penalize(PenaltyReason.KilometersRequestedExceeded)
        publication.updateCar(kilometersReturned)
    }

    def isFinished() {
        this.rent && this.rent.isFinished()
    }

    def cancelFromHost() {
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        this.rent.cancelFromHost()
    }

    def cancelFromGuest() {
        if (!this.rent) {
            throw new RentNotExistsException();
        }
        this.rent.cancelFromGuest(this.startDateTime)
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

    def calculateDays() {
        ChronoUnit.DAYS.between(this.startDateTime, this.endDateTime).toInteger()
    }
}
