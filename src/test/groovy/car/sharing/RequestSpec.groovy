package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.Period

class RequestSpec extends Specification implements DomainUnitTest<Request> {

    @Shared User user1
    @Shared Guest guest
    @Shared User user2
    @Shared Car car
    @Shared Host host

    def setup() {
        user1 = new User(username: "username1", password: "password")
        host = new Host(user: user1)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
        user2 = new User(username: "username2", password: "password")
        guest = new Guest(user: user2)
        publication = new Publication(host, car)
    }

    def cleanup() {
    }

    void "create request success"() {
        given: "an existing guest"
        when: "a request is created"
        def newRequest = new Request("place1", "place2", "2024-01-01", "2024-01-03", RequestStatus.PENDING, guest)
        def requestIsValid = newRequest.validate()
        then: "the request is created successfully "
        requestIsValid && (newRequest.guest.user.username == user2.username)
    }

}
