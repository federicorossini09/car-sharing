package car.sharing


import java.time.LocalDateTime

class Review {

    String text
    Integer score
    Request request
    LocalDateTime createdAt = LocalDateTime.now()

    static constraints = {
    }

    def sentForRequest(Request request) {
        this.request.id == request.id
    }
}
