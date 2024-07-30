package car.sharing

class Review {

    String text
    Integer score
    Request request

    static constraints = {
    }

    def sentForRequest(Request request) {
        this.request.id == request.id
    }
}
