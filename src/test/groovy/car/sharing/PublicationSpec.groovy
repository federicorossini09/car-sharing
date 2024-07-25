package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate
import java.time.Period

class PublicationSpec extends Specification implements DomainUnitTest<Publication> {

    @Shared Car car
    @Shared Host host
    @Shared User user
    @Shared Price price

    def setup() {
        user = new User(username: "username1", password: "password")
        host = new Host(user: user)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 20000, licensePlate: "AC933WP")
        price = new Price(car.year, car.kilometers)
    }

    def cleanup() {
    }

    void "publication creation success"() {
        given: "an existing car and host"
        when: "a publication is created"
            def newPublication = new Publication(host: host, car: car, price:price)
            def newPublicationIsValid = newPublication.validate()
        then: "the publication is created successfully"
            newPublicationIsValid
        and: "with a car"
            newPublication.car.brand == car.brand
        and: "with a host"
            newPublication.host.user.username == host.user.username
        and: "with a price"
            newPublication.price.finalValue == 95
    }

    void "update price success"() {
        given: "an existing publication with price final value 100"
            def publication = new Publication(host: host, car: car, price:price)
        when: "its price is updated to 110"
            publication.updatePrice(110)
        then: "the price is updated successfully"
            publication.price.finalValue == 110
    }

    void "request accept success"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
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

        def date0 = LocalDate.parse("2024-01-01")
        def date1 = LocalDate.parse("2024-01-03")
        def date2 = LocalDate.parse("2024-01-04")

        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDate: date0, endDate: date1, guest: guest)
        newPublication.requests<<request
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        then: "the dates are not valid"
        newPublication.areDatesAvailable(date0,date2) == false
    }

    void "date validation returns true if dates dont collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        def date1 = LocalDate.parse("2024-01-05")
        def date2 = LocalDate.parse("2024-01-06")
        then: "the dates are not valid"
        newPublication.areDatesAvailable(date1,date2) == true
    }

}
