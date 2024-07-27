package car.sharing

class Penalty {

    PenaltyReason reason
    BigDecimal value


    Penalty(PenaltyReason reason) {
        switch(reason) {
            case PenaltyReason.NotDeliverOnTime:
                value = 0.9
                break
            case PenaltyReason.NotReturnOnTime:
                value = 0.9
                break
            case PenaltyReason.RentCancellation:
                value = 0.8
                break
        }
    }
    static constraints = {
    }

    def apply(BigDecimal score) {
        score*value
    }
}
