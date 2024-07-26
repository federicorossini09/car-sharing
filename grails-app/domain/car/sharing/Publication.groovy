package car.sharing

import java.time.LocalDate

class Publication {

    static belongsTo = [host: Host]
    Car car
    Price price
    PublicationStatus status = PublicationStatus.PENDING
    List<Request> requests = []
    static hasMany = [requests: Request]
    Score score = new Score()
    List<Review> reviews = []


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
        if (areDatesAvailable(requestToAccept.startDate, requestToAccept.endDate)) {
            requestToAccept.accept()
        }
    }

    void addRequest(Request new_request) {
        this.requests.add(new_request)
    }

    int lengthOfRequests() {
        return requests.size()
    }




    List getRents() {
        this.requests.findAll { it.rent }.collect { it.rent }
    }

    boolean areDatesAvailable(LocalDate startDate, LocalDate endDate) {
        requests.every { request -> !request.isOccupying(startDate, endDate) }
    }

    def penalize() {
        score.penalize()
    }

    def sendReview(Review review) {
        this.reviews<<review
    }

    def calculateScore() {
        //obtenemos un promedio
        //todo hablar con fede como podemos hacer el tema de la penalizacion con esta manera
        def total = reviews.sum { it.score.getValue() }
        return total / reviews.size()
    }
}
