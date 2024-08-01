package car.sharing

import car.sharing.exceptions.HostDoesNotOwnPublicationException
import car.sharing.exceptions.KilometersReturnedBelowDeliveredException
import car.sharing.exceptions.ReturnNotifiedWithoutKilometersException
import car.sharing.exceptions.ReviewAlreadySent
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Period

class HostSpec extends Specification implements DomainUnitTest<Host> {

    @Shared
    User user1
    @Shared
    User user2
    @Shared
    Host host
    @Shared
    Host host2
    @Shared
    Car car
    @Shared
    Price price
    @Shared
    Publication publication1
    @Shared
    Publication publication2
    @Shared
    Request request1
    @Shared
    Guest guest2

    def setup() {
        user1 = new User(username: "username1", password: "password")
        user2 = new User(username: "username2", password: "password")
        host = new Host(user: user1)
        host2 = new Host(user: user2)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDateTime.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
        price = new Price(car.year, car.kilometers)
        publication1 = new Publication(car: car, price: price)
        publication1.id = 1
        publication2 = new Publication(car: car, price: price)
        publication1.id = 2
        guest2 = new Guest(user: user2)
    }

    def cleanup() {
    }

    void "create host success"() {
        given: "an existing user"
        when: "a host is created"
        def newHost = new Host(user: user1)
        def hostIsValid = newHost.validate()
        then: "the host is created successfully with the user associated"
        hostIsValid && (newHost.user.username == user1.username)
    }

    void "create publication success"() {
        given: "an existing host, car and price"
        when: "a publication is created"
        def newPublication = host.createPublication(car, price)
        then: "the publication is created successfully with PENDING status"
        newPublication.status == PublicationStatus.PENDING
        and: "the host has publications"
        (newPublication.host.publications.size() >= 1)
    }

    void "check host owns a publication"() {
        given: "an existing host that has a publication"
        host.addToPublications(publication1)
        when: "checking if he owns that publication"
        host.checkHostsPublication(publication1)
        then: "an exception is not thrown"
        noExceptionThrown()
    }

    void "check host owns a publication fails"() {
        given: "an existing host that has a publication and another host that has another"
        host.addToPublications(publication1)
        host2.addToPublications(publication2)
        when: "checking if he owns another publication"
        host.checkHostsPublication(publication2)
        then: "a host does not own publication exception is thrown"
        thrown HostDoesNotOwnPublicationException
    }

    void "update publication price"() {
        given: "an existing host that has a publication"
        host.addToPublications(publication1)
        when: "the price of the publication is updated to 110"
        host.updatePublicationPrice(publication1, BigDecimal.valueOf(110))
        then: "the price us updated successfully"
        publication1.price.finalValue == 110
    }

    void "publish publication"() {
        given: "an existing host that has a publication"
        host.addToPublications(publication1)
        when: "the publication is published"
        host.publish(publication1)
        then: "the publication is published successfully"
        publication1.status == PublicationStatus.ACTIVE
    }

    void "report successful return"() {
        given: "a host that owns a publication with an active rent"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest2)
        guest2.addToRequests(request1)
        publication1.addRequest(request1)
        request1.accept()
        guest2.reportSuccessfulDelivery(request1, 50000)
        when: "the return is reported"
        host.reportSuccessfulReturn(request1, 50500)
        then: "the rent is finished"
        request1.rent.isFinished()
    }

    void "cannot report return with less kilometers than delivered"() {
        given: "a host that owns a publication with an active rent"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest2)
        guest2.addToRequests(request1)
        publication1.addRequest(request1)
        request1.accept()
        guest2.reportSuccessfulDelivery(request1, 50000)
        when: "the return is reported with 40000km"
        host.reportSuccessfulReturn(request1, 40000)
        then: "a kilometers returned below delivered exception is thrown"
        thrown KilometersReturnedBelowDeliveredException
    }

    void "cannot report return without kilometers returned"() {
        given: "a host that owns a publication with an active rent"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest2)
        guest2.addToRequests(request1)
        publication1.addRequest(request1)
        request1.accept()
        guest2.reportSuccessfulDelivery(request1, 50000)
        when: "the return is reported without kilometers"
        host.reportSuccessfulReturn(request1, null)
        then: "a return notified without kilometers exception is thrown"
        thrown ReturnNotifiedWithoutKilometersException
    }

    void "host reports not returned car"() {
        given: "an existing host that has an accepted publication"
        host.addToPublications(publication1)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")

        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: startDate, endDateTime: returnDate, guest: guest2)
        publication1.addRequest(request1)
        request1.accept()
        and: "the  car was delivered by the host"
        request1.reportSuccessfulDeliver(50000)
        when: "the host reports that the car was not returned by the guest"
        request1.reportNotReturned()
        then: "the publication is (canceled?)"
        request1.rent.isCanceled()
    }

    void "review guest"() {
        given: "a host that owns a publication with a rent finished"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest2)
        guest2.addToRequests(request1)
        publication1.addRequest(request1)
        request1.accept()
        guest2.reportSuccessfulDelivery(request1, 50000)
        host.reportSuccessfulReturn(request1, 50500)
        when: "he reviews the guest"
        host.reviewGuest(request1, new Review(score: 3, text: 'something', request: request1))
        then: "the review is added to reviews sent"
        host.isReviewAlreadySent(request1)
    }

    void "cannot review guest twice for the same request"() {
        given: "a host already reviewed the guest"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest2)
        guest2.addToRequests(request1)
        publication1.addRequest(request1)
        request1.accept()
        guest2.reportSuccessfulDelivery(request1, 50000)
        host.reportSuccessfulReturn(request1, 50500)
        host.reviewGuest(request1, new Review(score: 3, text: 'something', request: request1))
        when: "he tries to review the guest a second time"
        host.reviewGuest(request1, new Review(score: 4, text: 'another thing', request: request1))
        then: "a review already sent exception is thrown"
        thrown ReviewAlreadySent
    }

}
