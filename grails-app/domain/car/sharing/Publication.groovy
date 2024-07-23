package car.sharing

import java.time.LocalDate

class Publication {

    static belongsTo = [host: Host]
    Car car
    Price price
    PublicationStatus status = PublicationStatus.PENDING
    List<Request> requests = []
    static hasMany = [requests: Request]


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

    def unPublish() {
        this.setStatus(PublicationStatus.PENDING)
    }

    def acceptRequest(Request requestToAccept) {
        //aca ver si recibimos el id o se puede directamente el request
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

}
