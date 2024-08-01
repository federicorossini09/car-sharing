package car.sharing

import car.sharing.exceptions.RentTooLateToCancelException
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class RentSpec extends Specification implements DomainUnitTest<Rent> {

    @Shared
    TotalPrice totalPrice
    @Shared
    LocalDateTime now

    static final String FIXED_DATE = "2024-08-01T10:00:00Z"

    def setup() {
        totalPrice = new TotalPrice(5, 100, 50, false)
        Clock clock = Clock.fixed(Instant.parse(FIXED_DATE), ZoneId.of("UTC"));
        now = LocalDateTime.now(clock)
    }

    def cleanup() {
    }

    void "validate a rent is scheduled or active"() {
        given: "an existing rent"
        def rent = new Rent(totalPrice: totalPrice)
        when: "i ask uf its scheduled or active"
        def isScheduledOrActive = rent.isScheduledOrActive()
        then: "is scheduled or active"
        isScheduledOrActive
    }


    void "cancel a rent that is scheduled or active"() {
        given: "an existing rent"
        def rent = new Rent(totalPrice: totalPrice)
        when: "i cancel it"
        rent.cancel()
        rent.isScheduledOrActive()
        then: "its not neither scheduled nor active"
        !rent.isScheduledOrActive()
    }

    void "activate a rent "() {
        given: "an existing rent"
        def rent = new Rent(totalPrice: totalPrice)
        when: "activate"
        rent.activate(20000)
        then: "its active"
        rent.isScheduledOrActive()
    }

    void "report undelivered"() {
        given: "an existing accepted rent"
        def rent = new Rent(totalPrice: totalPrice)
        rent.status = RentStatus.SCHEDULED
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        when: "i report undelivered"
        rent.reportUndelivered(startDate)
        then: "its not neither scheduled nor active"
        !rent.isScheduledOrActive()
        and: "its reason  is not delivered"
        rent.cancellationReason == CancellationReason.NotDelivered
    }

    void "report not returned"() {
        given: "an existing active rent"
        def rent = new Rent(totalPrice: totalPrice)
        rent.status = RentStatus.ACTIVE
        when: "i report not returned"
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        rent.reportNotReturned(returnDate)
        then: "its not neither scheduled nor active"
        !rent.isScheduledOrActive()
        and: "its reason is not delivered"
        rent.cancellationReason == CancellationReason.NotReturned
    }

    void "cancel from host"() {
        given: "an existing accepted rent"
        def rent = new Rent(totalPrice: totalPrice)
        rent.status = RentStatus.SCHEDULED
        when: "the host cancels it"
        rent.cancelFromHost()
        then: "its cancelled"
        rent.isCanceled()
        and: "the reason is canceled by host"
        rent.cancellationReason == CancellationReason.CanceledByHost
    }

    void "cancel from guest in a forbidden date"() {
        given: "an existing active rent"
        def rent = new Rent(totalPrice: totalPrice)
        rent.status = RentStatus.ACTIVE
        when: "the guest tries to cancel it after the permitted date"
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        rent.cancelFromGuest(startDate)
        then: "an error is thrown"
        thrown RentTooLateToCancelException
    }

    void "cancel from guest in an allowed date"() {
        given: "an existing active rent"
        def rent = new Rent(totalPrice: totalPrice)
        rent.status = RentStatus.ACTIVE
        when: "the guest tries to cancel it in an allowed date"
        def startDate = LocalDateTime.parse("2027-01-01T00:00:00")
        rent.cancelFromGuest(startDate)
        then: "its  cancelled"
        rent.isCanceled()
        and: "the reason is canceled by guest"
        rent.cancellationReason == CancellationReason.CanceledByGuest
    }
}

