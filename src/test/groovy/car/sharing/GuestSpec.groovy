package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.Period

class GuestSpec extends Specification implements DomainUnitTest<Guest> {

    @Shared User user1
    @Shared User user2
    @Shared Guest guest
    @Shared Car car

    def setup() {
        user1 = new User(username: "username1", password: "password")
        user2 = new User(username: "username2", password: "password")
        guest = new Guest(user: user2)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
    }

    def cleanup() {
    }

    void "guest creation success"() {
        given: "an existing user"
        when: "a guest is created"
            def newGuest = new Guest(user: user1)
            def guestIsValid = newGuest.validate()
        then: "the guest is created successfully with the user associated"
            guestIsValid && (newGuest.user.username == "username1")
    }

    void "create first publication success"() {
        given: "an existing guest and valid car"
        when: "a publication is created"
            def publication = guest.createFirstPublication(car)
        then: "the publication is created successfully with PENDING status"
            publication.status == PublicationStatus.PENDING
        and: "a new host is created and associated to the publication and the user"
            (publication.host.user == guest.user) && (publication.host.publications.size() == 1)
    }
}
