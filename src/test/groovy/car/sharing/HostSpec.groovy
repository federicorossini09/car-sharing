package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.Period

class HostSpec extends Specification implements DomainUnitTest<Host> {

    @Shared User user1
    @Shared User user2
    @Shared Host host
    @Shared Car car

    def setup() {
        user1 = new User(username: "username1", password: "password")
        user2 = new User(username: "username2", password: "password")
        host = new Host(user: user2)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: Instant.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
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
        given: "an existing host and car"
        when: "a publication is created"
            def newPublication = host.createPublication(car)
        then: "the publication is created successfully with PENDING status"
            newPublication.status == PublicationStatus.PENDING
        and: "the host has publications"
            (newPublication.host.publications.size() >= 1)
    }
}
