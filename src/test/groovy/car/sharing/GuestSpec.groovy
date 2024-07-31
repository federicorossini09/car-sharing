package car.sharing

import car.sharing.exceptions.DeliveryNotifiedWithoutKilometersException
import car.sharing.exceptions.HostCannotRequestHisPublication
import car.sharing.exceptions.KilometersDeliveredBelowPublishedException
import car.sharing.exceptions.ReviewAlreadySent
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
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
        def guest = new Guest(user: user2)
        guest.addRequest(newPublication, "place1", "place2", LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-01-03T00:00:00"), 500)
        then: "publication requests size is 1"
        newPublication.lengthOfRequests() == 1
    }

    void "host cannot request own publication"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user1)
        guest.addRequest(newPublication, "place1", "place2", LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-01-03T00:00:00"), 500)
        then: "publication requests size is 1"
        thrown HostCannotRequestHisPublication
    }

    void "guest gets penalized "() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest"
        def newRequest = guest.addRequest(newPublication, "place1", "place2", LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-01-03T00:00:00"))
        and: "the guest has no penalties"
        guest.score.penalties.size() == 0
        and: "its acepted and delivered"
        newRequest.accept()
        newRequest.reportSuccessfulDeliver(10000)
        and: "is reported not returned"
        newRequest.reportNotReturned()
        then: "the guest is penalized"
        guest.score.penalties.size() == 1
    }


    void "guest reports rent undelivered"() {
        given: "an existing car"
        when: "an existing publication with two reviews"
        def newPublication = new Publication(host: host, car: car)
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "muy malo", score: 1)
        newPublication.receiveReview(review1)
        newPublication.receiveReview(review2)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user2)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        and: "its accepted"
        def request = guest.requests[0]
        request.accept()
        and: "the guest reports it as undelivered"
        guest.reportUndelivered(request, newPublication)
        then: "the rent is canceled"
        request.rent.isCanceled()
        and: "the score of the publication has decreased 10%"
        newPublication.score.value == 2.7
    }

    void "guest reports rent delivered"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user2)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        and: "its accepted"
        def request = guest.requests[0]
        request.accept()
        and: "the guest reports the delivery"
        guest.reportSuccessfulDelivery(request, 55000)
        then: "the rent is active"
        request.rent.isActive()
    }

    void "cannot report delivery with less kilometers than published"() {
        given: "a publication with a car with 50000km"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user2)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        and: "its accepted"
        def request = guest.requests[0]
        request.accept()
        and: "the guest reports the delivery with 45000km"
        guest.reportSuccessfulDelivery(request, 45000)
        then: "a kilometers delivered below published exception is thrown"
        thrown KilometersDeliveredBelowPublishedException
    }

    void "cannot report delivery without kilometers returned"() {
        given: "a publication with a car with 50000km"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest "
        def guest = new Guest(user: user2)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        and: "its accepted"
        def request = guest.requests[0]
        request.accept()
        and: "the guest reports the delivery without kilometers"
        guest.reportSuccessfulDelivery(request, null)
        then: "a delivery notified without kilometers exception is thrown"
        thrown DeliveryNotifiedWithoutKilometersException
    }

    void "review publication"() {
        given: "a guest that requested a publication and the rent is finished"
        def newPublication = new Publication(host: host, car: car)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        def request = guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        request.setId(1)
        request.accept()
        request.reportSuccessfulDeliver(50000)
        request.reportSuccessfulReturn(50500)
        when: "when the guest reviews the publication"
        def review = new Review(score: 3, text: 'something', request: request)
        guest.reviewPublication(request, review)
        then: "the review is added to guest reviews sent"
        guest.isReviewAlreadySent(request)
    }

    void "cannot review publication twice for the same request"() {
        given: "a guest that already reviewed a publication"
        def newPublication = new Publication(host: host, car: car)
        def startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-01-03T00:00:00")
        def request = guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 500)
        request.setId(1)
        request.accept()
        request.reportSuccessfulDeliver(50000)
        request.reportSuccessfulReturn(50500)
        guest.reviewPublication(request, new Review(score: 3, text: 'something', request: request))
        when: "he tries to review the publication a second time"
        guest.reviewPublication(request, new Review(score: 4, text: 'another thing', request: request))
        then: "a review already sent exception is thrown"
        thrown ReviewAlreadySent
    }

}
