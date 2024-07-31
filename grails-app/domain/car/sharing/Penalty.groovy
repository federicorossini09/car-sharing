package car.sharing

class Penalty {

    PenaltyReason reason

    static constraints = {
    }

    def apply(BigDecimal score) {
        def value;
        switch (reason) {
            case PenaltyReason.NotDeliverOnTime:
                value = 0.9
                break
            case PenaltyReason.NotReturnOnTime:
                value = 0.9
                break
            case PenaltyReason.KilometersRequestedExceeded:
                value = 0.99
                break
        }
        score * value
    }
}
