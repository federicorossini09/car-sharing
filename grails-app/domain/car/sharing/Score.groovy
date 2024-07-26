package car.sharing

class Score {

    Integer value = 100

    static constraints = {
    }

    def penalize() {
        value *= 0.9
    }

    def getValue() {
        return value
    }
}
