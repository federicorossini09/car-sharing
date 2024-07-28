package car.sharing

class Score {

    //todo ver que hacer con el value
    BigDecimal value = 3
    List<Review> reviews = []
    List<Penalty> penalties = []


    static constraints = {
    }

    def penalize(PenaltyReason reason) {
        def penalty = new Penalty(reason)
        penalties<<penalty
        this.calculate()
    }

    def getValue() {
        return value
    }

    def isReady() {
        this.reviews.size() > 1
    }
    def calculate() {
        if (this.isReady()) {
            def total = reviews.sum { it.score } / reviews.size()
            penalties.each { penalty ->
                total = penalty.apply(total)
            }
            value = total
        }
    }

    def sendReview(Review review) {
        this.reviews<<review
        this.calculate()
    }
}
