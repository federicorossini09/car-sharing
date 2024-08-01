package car.sharing

import java.time.LocalDate

class Price {

    BigDecimal minimumValue;
    BigDecimal maximumValue;
    BigDecimal finalValue;

    static constraints = {
        finalValue validator: { val, obj -> val >= obj.minimumValue && val <= obj.maximumValue }
    }

    static final BigDecimal BASE_VALUE = 100;
    static final Integer KM_FIRST_STEP = 25000
    static final Integer KM_SECOND_STEP = 50000
    static final Integer KM_THIRD_STEP = 75000
    static final Integer KM_FOURTH_STEP = 100000
    static final Integer YEAR_DIFF_FIRST_STEP = 1
    static final Integer YEAR_DIFF_SECOND_STEP = 5
    static final Integer YEAR_DIFF_THIRD_STEP = 10
    static final Integer YEAR_DIFF_FOURTH_STEP = 20
    static final BigDecimal FIRST_STEP_PENALTY_FACTOR = 0.99
    static final BigDecimal SECOND_STEP_PENALTY_FACTOR = 0.95
    static final BigDecimal THIRD_STEP_PENALTY_FACTOR = 0.90
    static final BigDecimal FOURTH_STEP_PENALTY_FACTOR = 0.80
    static final BigDecimal MIN_VALUE_FACTOR = 0.75
    static final BigDecimal MAX_VALUE_FACTOR = 1.25

    Price(Integer year, Integer kilometers) {

        BigDecimal meanValue = calculateMeanValue(year, kilometers)

        finalValue = meanValue

        minimumValue = calculateMinValue(meanValue)

        maximumValue = calculateMaxValue(meanValue)
    }

    private static BigDecimal calculateMaxValue(BigDecimal meanValue) {
        meanValue * MAX_VALUE_FACTOR
    }

    private static BigDecimal calculateMinValue(BigDecimal meanValue) {
        meanValue * MIN_VALUE_FACTOR
    }

    private static BigDecimal calculateMeanValue(Integer year, Integer kilometers) {
        BigDecimal meanValue = applyYearPenalty(BASE_VALUE, year)
        meanValue = applyKilometersPenalty(meanValue, kilometers)
        meanValue
    }

    private static BigDecimal applyYearPenalty(BigDecimal value, Integer year) {

        Integer currentYear = LocalDate.now().getYear();

        Integer yearDiff = currentYear - year;

        BigDecimal result = value

        if (yearDiff > YEAR_DIFF_FIRST_STEP && yearDiff <= YEAR_DIFF_SECOND_STEP)
            result = value * FIRST_STEP_PENALTY_FACTOR
        if (yearDiff > YEAR_DIFF_SECOND_STEP && yearDiff <= YEAR_DIFF_THIRD_STEP)
            result = value * SECOND_STEP_PENALTY_FACTOR
        if (yearDiff > YEAR_DIFF_THIRD_STEP && yearDiff <= YEAR_DIFF_FOURTH_STEP)
            result = value * THIRD_STEP_PENALTY_FACTOR
        if (yearDiff > YEAR_DIFF_FOURTH_STEP)
            result = value * FOURTH_STEP_PENALTY_FACTOR

        result
    }

    private static BigDecimal applyKilometersPenalty(BigDecimal value, Integer kilometers) {

        BigDecimal result = value

        if (kilometers > KM_FIRST_STEP && kilometers <= KM_SECOND_STEP)
            result = value * FIRST_STEP_PENALTY_FACTOR
        if (kilometers > KM_SECOND_STEP && kilometers <= KM_THIRD_STEP)
            result = value * SECOND_STEP_PENALTY_FACTOR
        if (kilometers > KM_THIRD_STEP && kilometers <= KM_FOURTH_STEP)
            result = value * THIRD_STEP_PENALTY_FACTOR
        if (kilometers > KM_FOURTH_STEP)
            result = value * FOURTH_STEP_PENALTY_FACTOR

        result
    }

    def updateFinalValue(BigDecimal newFinalValue) {
        this.setFinalValue(newFinalValue)
    }

    def update(Integer year, Integer kilometers) {
        def currentFinalValue = this.finalValue

        def newMeanValue = calculateMeanValue(year, kilometers)
        def newMinValue = calculateMinValue(newMeanValue)
        def newMaxValue = calculateMaxValue(newMeanValue)

        if (currentFinalValue > newMaxValue) {
            this.setFinalValue(newMaxValue)
        }

        this.setMinimumValue(newMinValue)
        this.setMaximumValue(newMaxValue)
    }
}
