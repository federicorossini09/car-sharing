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

    void "publication request succest"() {
        given: "an existing car"
        when: "a publication is created"
            def newPublication = new Publication(host: host, car: car)
            def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
            def guest = new Guest(user)
            newPublication.request("place1", "place2", "2024-01-01", "2024-01-03", guest)
        then: "publication requests size is 1"
            newPublication.lengthOfRequests() == 1

    }
}
