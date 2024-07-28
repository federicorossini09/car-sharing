package car.sharing

import car.sharing.exceptions.PublicationNotAvailableException

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
        if (areDatesAvailable(requestToAccept.startDateTime, requestToAccept.endDateTime)) {
            requestToAccept.accept()
        } else {
            th
        }
    }

    void addRequest(Request request) {
        this.addToRequests(request)
    }

    int lengthOfRequests() {
        return requests.size()
    }

    List getRents() {
        this.requests.findAll { it.rent }.collect { it.rent }
    }

    boolean areDatesAvailable(LocalDateTime startDate, LocalDateTime endDate) {
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

    def sendReview(Review review) {
        score.sendReview(review)

    }

    def calculateScore() {
        score.calculate()
    }
}
