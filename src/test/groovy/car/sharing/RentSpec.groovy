package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RentSpec extends Specification implements DomainUnitTest<Rent> {

    def setup() {
    }

    def cleanup() {
    }

    void "validate a rent is scheduled or active"() {
        given: "an existing rent"
        def rent = new Rent()
        when: "i ask uf its scheduled or active"
        def isScheduledOrActive = rent.isScheduledOrActive()
        then: "the dates are not valid"
        isScheduledOrActive == true
    }


    void "cancel a rent that is scheduled or active"() {
        given: "an existing rent"
        def rent = new Rent()
        when: "i cancel it"
        rent.cancel()
        rent.isScheduledOrActive()
        then: "its not neither scheduled nor active"
        rent.isScheduledOrActive() == false
    }
}
