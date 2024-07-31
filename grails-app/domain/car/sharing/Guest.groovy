package car.sharing

import car.sharing.exceptions.DeliveryNotifiedWithoutKilometersException
import car.sharing.exceptions.GuestDoesNotOwnsRequestException
import car.sharing.exceptions.HostCannotRequestHisPublication
import car.sharing.exceptions.ReviewAlreadySent
import org.springframework.lang.Nullable

import java.time.LocalDateTime

class Guest {

    User user
    static hasMany = [requests: Request, reviewsSent: Review]
    Score score = new Score()

    static constraints = {
    }

    def addRequest(Publication publication, String deliveryPlace, String returnPlace, LocalDateTime startDate, LocalDateTime endDate, @Nullable Integer kilometers) {

        checkUserCanRequestPublication(publication)

        //TODO: tal vez esta validacion es mejor que esté dentro del método "publication.addRequest" asi siempre queda adentro de publication
        if (publication.areDatesAvailable(startDate, endDate)) {
            def newRequest = new Request(deliveryPlace: deliveryPlace, returnPlace: returnPlace, startDateTime: startDate, endDateTime: endDate, kilometers: kilometers, guest: this)
            publication.addRequest(newRequest)
            this.addToRequests(newRequest)
            newRequest
        }
    }

    def checkUserCanRequestPublication(Publication publication) {
        if (publication.isHostedBy(this.user)) {
            throw new HostCannotRequestHisPublication()
        }
    }

    void reportUndelivered(Request request, Publication publication) {
        this.checkOwnsRequest(request)
        request.reportUndelivered()
        //todo esto de abajo deberia ir en el metodo de arriba
        publication.penalize(PenaltyReason.NotDeliverOnTime)
    }

    void reportSuccessfulDelivery(Request request, @Nullable Integer kilometersDelivered) {
        this.checkOwnsRequest(request)
        if (!kilometersDelivered)
            throw new DeliveryNotifiedWithoutKilometersException()
        request.reportSuccessfulDeliver(kilometersDelivered)
    }

    def cancelRent(Request request) {
        request.cancelFromGuest()
    }

    def ownsRequest(Request request) {
        requests.any { it.isSameAs(request) }
    }

    def checkOwnsRequest(Request request) {
        if (!this.ownsRequest(request))
            throw new GuestDoesNotOwnsRequestException()
    }

    def reviewPublication(Request request, Review review) {
        this.checkOwnsRequest(request)
        this.checkReviewAlreadySent(request)
        request.sendPublicationReview(review)
        this.addToReviewsSent(review)
    }

    def receiveReview(Review review) {
        this.score.receiveReview(review)
    }

    def checkReviewAlreadySent(Request request) {
        if (isReviewAlreadySent(request))
            throw new ReviewAlreadySent()
    }

    def isReviewAlreadySent(Request request) {
        reviewsSent.any { it.sentForRequest(request) }
    }

    def penalize(PenaltyReason reason) {
        this.score.penalize(reason)
    }

    def isFeatured() {
        this.score.isFeatured()
    }
}
