package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.Period

class PublicationSpec extends Specification implements DomainUnitTest<Publication> {

    @Shared Car car
    @Shared Host host
    @Shared User user

    def setup() {
        user = new User(username: "username1", password: "password")
        host = new Host(user: user)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
    }

    def cleanup() {
    }

    void "publication creation success"() {
        given: "an existing car"
        when: "a publication is created"
            def newPublication = new Publication(host: host, car: car)
            def newPublicationIsValid = newPublication.validate()
        then: "the publication is created successfully"
            newPublicationIsValid
        and: "with a car associated"
            newPublication.car.brand == car.brand
        and: "with a host associated"
            newPublication.host.user.username == host.user.username
    }

    void "publication request success"() {
        given: "an existing car"
        when: "a publication is created"
            def newPublication = new Publication(host: host, car: car)
            def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
            def guest = new Guest(user: user)
            def request = new Request(newPublication, "place1", "place2", "2024-01-01", "2024-01-03", guest)
        then: "publication requests size is 1"
            newPublication.lengthOfRequests() == 1
    }

    void "request accept success"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(newPublication, "place1", "place2", "2024-01-01", "2024-01-03", guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        then: "the request status is accepted"
        request.status == RequestStatus.ACCEPTED
    }


    void "date validation returns false if dates collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(newPublication, "place1", "place2", "2024-01-01", "2024-01-03", guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        def date1 = request.stringToDate("2024-01-02")
        def date2 = request.stringToDate("2024-01-04")
        then: "the dates are not valid"
        newPublication.areDatesValid(date1,date2) == false
    }

    void "date validation returns true if dates dont collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(newPublication, "place1", "place2", "2024-01-01", "2024-01-03", guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        def date1 = request.stringToDate("2024-01-05")
        def date2 = request.stringToDate("2024-01-06")
        then: "the dates are not valid"
        newPublication.areDatesValid(date1,date2) == true
    }

}
