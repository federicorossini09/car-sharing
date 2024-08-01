package car.sharing

import car.sharing.exceptions.CarVtvExpired
import car.sharing.exceptions.PublicationNotAvailableException
import car.sharing.exceptions.RentCannotBeActivatedException
import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class PublicationSpec extends Specification implements DomainUnitTest<Publication> {

    @Shared
    Car car
    @Shared
    Host host
    @Shared
    User user
    @Shared
    Price price

    def setup() {
        user = new User(username: "username1", password: "password")
        host = new Host(user: user)
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDateTime.now() + Period.ofMonths(5), kilometers: 20000, licensePlate: "AC933WP")
        price = new Price(car.year, car.kilometers)
    }

    def cleanup() {
    }

    void "publication creation success"() {
        given: "an existing car and host"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car, price: price)
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
        def publication = new Publication(host: host, car: car, price: price)
        when: "its price is updated to 110"
        publication.updatePrice(110)
        then: "the price is updated successfully"
        publication.price.finalValue == 110
    }

    void "publish success"() {
        given: "an existing pending publication"
        def publication = new Publication(host: host, car: car, price: price)
        when: "it's published"
        publication.publish()
        then: "its status is change to active"
        publication.status == PublicationStatus.ACTIVE
    }

    void "unpublish success"() {
        given: "an existing active publication"
        def publication = new Publication(host: host, car: car, price: price, status: PublicationStatus.ACTIVE)
        when: "it's unpublished"
        publication.unpublish()
        then: "its status is change to pending"
        publication.status == PublicationStatus.PENDING
    }

    void "request accept success"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car, price: price)
        def newPublicationIsValid = newPublication.validate()
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-09-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-09-03T00:00:00"), guest: guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        then: "the request status is accepted"
        request.status == RequestStatus.ACCEPTED
    }


    void "date validation returns false if dates collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car, price: price)
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)

        def date0 = LocalDateTime.parse("2024-01-01T00:00:00")
        def date1 = LocalDateTime.parse("2024-01-03T00:00:00")
        def date2 = LocalDateTime.parse("2024-01-04T00:00:00")

        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: date0, endDateTime: date1, guest: guest)
        newPublication.addToRequests(request)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        newPublication.areDatesAvailable(date0, date2)
        then: "the dates are not valid"
        thrown PublicationNotAvailableException
    }

    void "date validation returns true if dates dont collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car, price: price)
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-10-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-10-03T00:00:00"), guest: guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        def date1 = LocalDateTime.parse("2024-10-05T00:00:00")
        def date2 = LocalDateTime.parse("2024-10-06T00:00:00")
        then: "the dates are valid"
        newPublication.areDatesAvailable(date1, date2)
    }

    void "i penalize a publication and it decreases its score"() {
        given: "an existing publication"
        def newPublication = new Publication(host: host, car: car)
        when: "it has two review"
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "muy malo", score: 1)
        newPublication.receiveReview(review1)
        newPublication.receiveReview(review2)
        and: "i penalize it"
        newPublication.penalize(PenaltyReason.NotDeliverOnTime)
        then: "its score decreases 10%"
        newPublication.score.value == 2.7
    }


    void "sending a review to a publication success"() {
        given: "an existing publication and an accepted requeest"
        def newPublication = new Publication(host: host, car: car, price: price)
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-01-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-01-03T00:00:00"), guest: guest)
        request.accept()
        when: "i send a review for that requst"
        def review2 = new Review(text: "no me fue tan bien", score: 3)
        newPublication.receiveReview(review2)
        then: "the review was added to the list"
        newPublication.score.reviews.size() == 1
    }


    void "if i have two reviews the score is the average"() {
        given: "an existing publication"
        def newPublication = new Publication(host: host, car: car)
        when: "i it has two reviews"
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "no me fue tan bien", score: 1)
        newPublication.receiveReview(review1)
        newPublication.receiveReview(review2)
        and: "i ask for the score of the publication"
        def score = newPublication.calculateScore()
        then: "its the average"
        score == 3
    }

    void "is featured"() {
        given: "an existing publication with one review of score 5"
        def newPublication = new Publication(host: host, car: car)
        def review1 = new Review(text: "muy bueno", score: 5)
        newPublication.receiveReview(review1)
        when: "is reviewed again with a review of score 5"
        def review2 = new Review(text: "excelente", score: 5)
        newPublication.receiveReview(review2)
        then: "it becomes featured"
        newPublication.isFeatured()
    }

    void "is not featured"() {
        given: "an existing publication with one review of score 4"
        def newPublication = new Publication(host: host, car: car)
        def review1 = new Review(text: "muy bueno", score: 4)
        newPublication.receiveReview(review1)
        when: "is reviewed again with a review of score 4"
        def review2 = new Review(text: "excelente", score: 4)
        newPublication.receiveReview(review2)
        then: "it remains not featured"
        !newPublication.isFeatured()
    }

    void "cant activate rent if there is another active rent"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car, price: price)
        def newPublicationIsValid = newPublication.validate()
        and: "two request are sent by a Guest"
        def guest = new Guest(user: user)
        def request1 = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-09-01T00:00:00"), endDateTime: LocalDateTime.parse("2024-09-03T00:00:00"), guest: guest)
        def request2 = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDateTime: LocalDateTime.parse("2024-09-04T00:00:00"), endDateTime: LocalDateTime.parse("2024-09-08T00:00:00"), guest: guest)
        newPublication.addRequest(request1)
        newPublication.addRequest(request1)
        and: "the requests are accepted"
        newPublication.acceptRequest(request1)
        newPublication.acceptRequest(request2)
        and: "both are reported delivered"
        request1.reportSuccessfulDeliver(20000)
        request2.reportSuccessfulDeliver(21000)
        then: "cant activate rent if there is another active rent"
        thrown RentCannotBeActivatedException

    }


    void "cant requests an expired vtv car date"() {
        given: "an existing publication"
        def car2 = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDateTime.now() - Period.ofDays(5), kilometers: 20000, licensePlate: "AC933WP")
        def newPublication = new Publication(host: host, car: car2, price: price)
        def user2 = new User(username: "username2", password: "password")

        def guest = new Guest(user: user2)
        when: "i send a request for a date that is expired"

        def startDate = LocalDateTime.parse("2024-10-01T00:00:00")
        def returnDate = LocalDateTime.parse("2024-10-03T00:00:00")
        guest.addRequest(newPublication, "place1", "place2", startDate, returnDate, 20200)
        then:
        thrown CarVtvExpired
    }
}
