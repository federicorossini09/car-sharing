package car.sharing

import car.sharing.exceptions.CarVtvExpired
import car.sharing.exceptions.PublicationNotAvailableException
import car.sharing.exceptions.RequestExpiredException

import java.time.LocalDateTime

class Publication {

    static belongsTo = [host: Host]
    Car car
    Price price
    PublicationStatus status = PublicationStatus.PENDING
    static hasMany = [requests: Request]
    Score score = new Score()


    static embedded = ['car', 'price']

    static constraints = {
    }


    Publication updatePrice(BigDecimal newValue) {
        price.updateFinalValue(newValue)
        this
    }

    def publish() {
        this.setStatus(PublicationStatus.ACTIVE)
    }

    def unpublish() {
        this.setStatus(PublicationStatus.PENDING)
    }

    def acceptRequest(Request requestToAccept) {
        if (this.isNotInThePast(requestToAccept.startDateTime) && areDatesAvailable(requestToAccept.startDateTime, requestToAccept.endDateTime)) {
            requestToAccept.accept()
        }
    }

    def rejectRequest(Request request) {
        request.reject()
    }

    void addRequest(Request request) {
        if (areDatesAvailable(request.startDateTime, request.endDateTime))
            this.addToRequests(request)
    }

    int lengthOfRequests() {
        return requests.size()
    }

    List getRents() {
        this.requests.findAll { it.rent }.collect { it.rent }
    }

    boolean isNotInThePast(LocalDateTime requestedStartDate) {
        LocalDateTime now = LocalDateTime.now()
        if (requestedStartDate.isAfter(now))
            true
        else throw new RequestExpiredException()
    }

    boolean areDatesAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        if (car.isVtvValidByDate(endDate)) {
            throw new CarVtvExpired()
        }
        if (requests.every { request -> !request.isOccupying(startDate, endDate) })
            true
        else throw new PublicationNotAvailableException()
    }

    def isSameAs(Publication publication) {
        publication.id == this.id
    }

    def isHostedBy(User user) {
        host.hasUser(user)
    }

    def penalize(PenaltyReason reason) {
        score.penalize(reason)
    }

    def receiveReview(Review review) {
        score.receiveReview(review)
    }

    def calculateScore() {
        score.calculate()
    }

    def isFeatured() {
        this.score.isFeatured()
    }

    def checkKilometersDelivered(Integer kilometersDelivered) {
        this.car.checkKilometersDelivered(kilometersDelivered)
    }

    def pricePerDay() {
        this.price.finalValue
    }

    def thereIsAnyActiveRent() {
        return (requests.any { request -> request.rentIsActive() })
    }

    def updateCar(Integer kilometers) {
        this.car.updateKilometers(kilometers)
        this.price.update(this.car.year, kilometers)
    }
}
