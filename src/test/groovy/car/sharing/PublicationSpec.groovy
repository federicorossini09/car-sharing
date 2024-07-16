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
}
