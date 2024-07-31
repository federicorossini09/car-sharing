package car.sharing

class Score {

    BigDecimal value
    List<Review> reviews = []
    List<Penalty> penalties = []

    static constraints = {
        value nullable: true
    }

    static final Integer MIN_REVIEWS_NEEDED_TO_SCORE = 1
    static final BigDecimal MIN_FEATURED_VALUE = BigDecimal.valueOf(4.5)

    def penalize(PenaltyReason reason) {
        def penalty = new Penalty(reason: reason)
        this.penalties.add(penalty)
        this.calculate()
    }

    def isReady() {
        this.reviews.size() > MIN_REVIEWS_NEEDED_TO_SCORE
    }

    def calculate() {
        if (this.isReady()) {
            def total = BigDecimal.ZERO
            total = reviews.sum { it.score } / reviews.size()
            penalties.each { penalty ->
                total = penalty.apply(total)
            }
            this.setValue(total)
        }
        this.value
    }

    def receiveReview(Review review) {
        this.reviews << review
        this.calculate()
    }

    def isFeatured() {
        if (this.isReady())
            this.value >= MIN_FEATURED_VALUE
        else
            false
    }
}
