package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TotalPriceSpec extends Specification implements DomainUnitTest<TotalPrice> {

    def setup() {
    }

    def cleanup() {
    }

    void "total price with less than 100 kilometers and no featured guest"() {
        given: "5 days, a per day value of 100, 50 kilometers and no featured guest"
        when: "a total price is created"
        def totalPrice = new TotalPrice(5, 100, 50, false)
        then: "the value is 500"
        totalPrice.value == 500
    }

    void "total price with more than 100 kilometers and no featured guest"() {
        given: "5 days, a per day value of 100, 150 kilometers and no featured guest"
        when: "a total price is created"
        def totalPrice = new TotalPrice(5, 100, 150, false)
        then: "the value is 505"
        totalPrice.value == 505
    }

    void "total price with more than 100 kilometers and featured guest"() {
        given: "5 days, a per day value of 100, 150 kilometers and featured guest"
        when: "a total price is created"
        def totalPrice = new TotalPrice(5, 100, 150, true)
        then: "the value is 429.25"
        totalPrice.value == 429.25
    }

    void "total price for same day"() {
        given: "0 days, a per day value of 100, 50 kilometers and no featured guest"
        when: "a total price is created"
        def totalPrice = new TotalPrice(0, 100, 50, false)
        then: "the value is 100"
        totalPrice.value == 100
    }
}
