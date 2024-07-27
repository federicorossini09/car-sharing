package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.Period

class GuestSpec extends Specification implements DomainUnitTest<Guest> {

    @Shared
    User user1
    @Shared
    User user2
    @Shared
    Guest guest
    @Shared
    Car car
    @Shared
    Host host

    def setup() {
        user1 = new User(username: "username1", password: "password")
        user2 = new User(username: "username2", password: "password")
        host = new Host(user: user1)
        guest = new Guest(user: user2)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDate.now() + Period.ofDays(5), kilometers: 50000, licensePlate: "AC933WP")
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

    void "publication request success"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user1)
        def startDate = LocalDate.parse("2024-01-01")
        def returnDate = LocalDate.parse("2024-01-03")
        guest.addRequest(newPublication, "place1", "place2", startDate , returnDate)
        then: "publication requests size is 1"
        newPublication.lengthOfRequests() == 1
    }


    void "guest reports rent undelivered"() {
        given: "an existing car"
        when: "an existing publication with two reviews"
        def newPublication = new Publication(host: host, car: car)
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "muy malo", score: 1)
        newPublication.sendReview(review1)
        newPublication.sendReview(review2)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user1)
        def startDate = LocalDate.parse("2024-01-01")
        def returnDate = LocalDate.parse("2024-01-03")
        guest.addRequest(newPublication, "place1", "place2", startDate , returnDate)
        and: "its accepted"
        def request = (Request) guest.requests.get(0)
        request.accept()
        and: "the guest reports it as undelivered"
        guest.reportUndelivered(request, newPublication)
        then: "the rent is canceled"
        request.rent.isCanceled()
        and: "the score of the publication has decreased 10%"
        newPublication.score.value == 2.7
    }

    void "guest reporsts rent delivered"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user1)
        def startDate = LocalDate.parse("2024-01-01")
        def returnDate = LocalDate.parse("2024-01-03")
        guest.addRequest(newPublication, "place1", "place2", startDate , returnDate)
        and: "its accepted"
        def request = (Request) guest.requests.get(0)
        request.accept()
        and: "the guest reports it as undelivered"
        guest.reportSuccessfulDeliver(request)
        then: "the rent is active"
        request.rent.isActive()
    }

}
