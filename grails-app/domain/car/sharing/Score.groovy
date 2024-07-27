package car.sharing

class Score {

    Integer value = 100
    List<Review> reviews = []

    static constraints = {
    }

    def penalize() {
        value *= 0.9
    }

    def getValue() {
        return value
    }

    def calculate() {
        //obtenemos un promedio
        //todo hablar con fede como podemos hacer el tema de la penalizacion con esta manera
        def total = reviews.sum { it.score }
        return total / reviews.size()
    }

    def sendReview(Review review) {
        this.reviews<<review
        this.calculate()
    }
}
