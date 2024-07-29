package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDateTime

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

    void "activate a rent "() {
        given: "an existing rent"
        def rent = new Rent()
        when: "activate"
        rent.activate()
        then: "its active"
        rent.isScheduledOrActive()
    }

    void "report undelivered"() {
        given: "an existing accepted rent"
        def rent = new Rent()
        rent.status = RentStatus.SCHEDULED
        when: "i report undelivered"
        rent.reportUndelivered()
        then: "its not neither scheduled nor active"
        rent.isScheduledOrActive() == false
        and: "its reason  is not delivered"
        rent.cancellationReason == CancellationReason.NotDelivered
    }

    void "report not returned"() {
        given: "an existing active rent"
        def rent = new Rent()
        rent.status = RentStatus.ACTIVE
        when: "i report not returned"
        rent.reportNotReturned()
        then: "its not neither scheduled nor active"
        rent.isScheduledOrActive() == false
        and: "its reason is not delivered"
        rent.cancellationReason == CancellationReason.NotReturned
    }

    void "cancel from host"() {
        given: "an existing accepted rent"
        def rent = new Rent()
        rent.status = RentStatus.SCHEDULED
        when: "the host cancels it"
        rent.cancelFromHost()
        then: "its cancelled"
        rent.isCanceled()
        and: "the reason is other"
        rent.cancellationReason == CancellationReason.Other
    }

    void "cancel from guest in a forbidden date"() {
        given: "an existing active rent"
        def rent = new Rent()
        rent.status = RentStatus.ACTIVE
        when: "the guest tries to cancel it after the permitted date"
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        rent.cancelFromGuest(startDate)
        then: "its not cancelled"
        rent.isCanceled() == false
        and: "there is no reason"
        rent.cancellationReason == null
    }

    void "cancel from guest in an allowed date"() {
        given: "an existing active rent"
        def rent = new Rent()
        rent.status = RentStatus.ACTIVE
        when: "the guest tries to cancel it in an allowed date"
        def startDate = LocalDateTime.parse("2027-01-01T00:00:00")
        rent.cancelFromGuest(startDate)
        then: "its  cancelled"
        rent.isCanceled() == true
        and: "the reason is other"
        rent.cancellationReason == CancellationReason.Other
    }
}

