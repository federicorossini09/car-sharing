package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.Period

class RequestSpec extends Specification implements DomainUnitTest<Request> {

    @Shared
    User user1
    @Shared
    Guest guest
    @Shared
    User user2
    @Shared
    Car car
    @Shared
    Host host
    @Shared
    Publication publication

    def setup() {
        user1 = new User(username: "username1", password: "password")
        host = new Host(user: user1)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
        user2 = new User(username: "username2", password: "password")
        guest = new Guest(user: user2)
        publication = new Publication(host: host, car: car)
    }

    def cleanup() {
    }

    void "create request success"() {
        given: "an existing guest"
        when: "a request is created"
        def newRequest = new Request(publication: publication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: "2024-01-01T00:00:00", endDateTime: "2024-01-03T00:00:00", guest: guest)
        def requestIsValid = newRequest.validate()
        then: "the request is created successfully "
        requestIsValid && (newRequest.guest.user.username == user2.username)
    }


    void "request accept"() {
        given: "an existing request"
        def newRequest = new Request(publication: publication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: "2024-01-01T00:00:00", endDateTime: "2024-01-03T00:00:00", guest: guest)
        when: "the request is accepted"
        newRequest.accept()
        then: "the status changes "
        newRequest.status == RequestStatus.ACCEPTED
    }

    void "report successful deliver"() {
        given: "an existing accepted request"
        def newRequest = new Request(publication: publication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
        newRequest.accept()
        when: "the  deliver is reported"
        newRequest.reportSuccessfulDeliver()
        then: "rent is activated"
        newRequest.rent.status == RentStatus.ACTIVE
    }


    void "report undeliver"() {
        given: "an existing accepted request"
        def newRequest = new Request(publication: publication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
        newRequest.accept()
        when: "the undeliver is reported"
        newRequest.reportUndelivered()
        then: "rent is canceled"
        newRequest.rent.isCanceled()
    }


}
