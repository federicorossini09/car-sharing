package car.sharing

import car.sharing.exceptions.HostDoesNotOwnPublicationException
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
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
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDate.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
        price = new Price(car.year, car.kilometers)
        publication1 = new Publication(car: car, price: price)
        publication2 = new Publication(car: car, price: price)
        def guest2 = new Guest(user: user2)

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
        host.checkOwnsPublication(publication1)
        then: "an exception is not thrown"
        noExceptionThrown()
    }

    void "check host owns a publication fails"() {
        given: "an existing host that has a publication and another host that has another"
        host.addToPublications(publication1)
        host2.addToPublications(publication2)
        when: "checking if he owns another publication"
        host.checkOwnsPublication(publication2)
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

    void "host reports not returned car"() {
        given: "an existing host that has an accepted publication"
        host.addToPublications(publication1)
        request1 = new Request(deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest2)
        publication1.addRequest(request1)
        request1.accept()
        and: "the  car was delivered by the host"
        request1.reportSuccessfulDeliver()
        when: "the host reports that the car was not returned by the guest"
        request1.reportNotReturned()
        then: "the publication is ____ (canceled?)"
        //todo aca no deberia ser otro tipo?
        request1.rent.isCanceled()
    }
}
