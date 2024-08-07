package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class PriceSpec extends Specification implements DomainUnitTest<Price> {

    @Shared
    Integer currentYear

    def setup() {
        currentYear = LocalDate.now().getYear()
    }

    def cleanup() {
    }

    void "price creation with no penalties success"() {
        given: "the car year is the current year and kilometers is 10000"
        def year = currentYear
        def kilometers = 10000
        when: "a price is created"
        def price = new Price(year, kilometers)
        def priceIsValid = price.validate()
        then: "the price is created successfully"
        priceIsValid
        and: "the final value is 100"
        price.finalValue == 100
        and: "the min value is 75"
        price.minimumValue == 75
        and: "the max value is 125"
        price.maximumValue == 125
    }

    void "price creation with year penalty success"() {
        given: "the car year is 2010 and kilometers is 10000"
        def year = 2010
        def kilometers = 10000
        when: "a price is created"
        def price = new Price(year, kilometers)
        def priceIsValid = price.validate()
        then: "the price is created successfully"
        priceIsValid
        and: "the final value is 90"
        price.finalValue == 90
        and: "the min value is 67.5"
        price.minimumValue == 67.5
        and: "the max value is 112.5"
        price.maximumValue == 112.5
    }

    void "price creation with kilometers penalty success"() {
        given: "the car year is the current year and kilometers is 30000"
        def year = currentYear
        def kilometers = 30000
        when: "a price is created"
        def price = new Price(year, kilometers)
        def priceIsValid = price.validate()
        then: "the price is created successfully"
        priceIsValid
        and: "the final value is 99"
        price.finalValue == 99
        and: "the min value is 74.25"
        price.minimumValue == 74.25
        and: "the max value is 123.75"
        price.maximumValue == 123.75
    }

    void "price creation with year and kilometers penalty success"() {
        given: "the car year is 2010 and kilometers is 30000"
        def year = 2010
        def kilometers = 30000
        when: "a price is created"
        def price = new Price(year, kilometers)
        def priceIsValid = price.validate()
        then: "the price is created successfully"
        priceIsValid
        and: "the final value is 99"
        price.finalValue == 89.1
        and: "the min value is 66.825"
        price.minimumValue == 66.825
        and: "the max value is 111.375"
        price.maximumValue == 111.375
    }

    void "update final value success"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        when: "its final value is updated to 110"
        price.updateFinalValue(110)
        def priceIsValid = price.validate()
        then: "the price is updated successfully"
        priceIsValid && price.finalValue == 110
    }

    void "update final value fails if final value exceeds max value"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        when: "its final value is updated to 150"
        price.updateFinalValue(150)
        def priceIsValid = price.validate()
        then: "the price is invalid"
        !priceIsValid && price.errors.getFieldError().getField() == "finalValue"
    }

    void "update final value fails if final value is below min value"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        when: "its final value is updated to 50"
        price.updateFinalValue(50)
        def priceIsValid = price.validate()
        then: "the price is invalid"
        !priceIsValid && price.errors.getFieldError().getField() == "finalValue"
    }

    void "price update does not change any value"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        when: "it is updated with 15000 km"
        price.update(currentYear, 15000)
        then: "its values do not change"
        price.finalValue == 100 && price.minimumValue == 75 && price.maximumValue == 125
    }

    void "price update changes max & min values"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        when: "it is updated with 26000 km"
        price.update(currentYear, 26000)
        then: "its max & min values change but final value does not"
        price.finalValue == 100 && price.minimumValue != 75 && price.maximumValue != 125
    }

    void "price update changes final value"() {
        given: "an existing price with min value 75 and max value 125 and final value 100"
        def price = new Price(currentYear, 10000)
        price.setFinalValue(125)
        when: "it is updated with 26000 km"
        price.update(currentYear, 26000)
        then: "its final value changes to new max value"
        price.finalValue == price.maximumValue
    }
}
