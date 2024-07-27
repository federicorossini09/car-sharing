package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
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
        car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDate.now() + Period.ofDays(5), kilometers: 20000, licensePlate: "AC933WP")
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
        newPublication.requests << request
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        then: "the dates are not valid"
        !newPublication.areDatesAvailable(date0, date2)
    }

    void "date validation returns true if dates dont collide with accepted request"() {
        given: "an existing car"
        when: "a publication is created"
        def newPublication = new Publication(host: host, car: car)
        and: "a request is sent by a Guest"
        def guest = new Guest(user: user)
        // todo arreglar esto
        //def date0 = LocalDate.parse("2024-01-01")
        //def date1 = LocalDate.parse("2024-01-03")
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
        and: "the request is accepted"
        newPublication.acceptRequest(request)
        and: "i try to validate dates that collide with that accepted request"
        def date1 = LocalDate.parse("2024-01-05")
        def date2 = LocalDate.parse("2024-01-06")
        then: "the dates are valid"
        newPublication.areDatesAvailable(date1, date2)
    }

    void "i penalize a publication and it decreases its score"() {
        given: "an existing publication"
        def newPublication = new Publication(host: host, car: car)
        when: "i penalize it"
        newPublication.penalize()
        then: "its score decreases 10%"
        newPublication.score.value == 90
    }


    void "sending a review to a publication success"() {
        given: "an existing publication and an accepted requeest"
        def newPublication = new Publication(host: host, car: car)
        def guest = new Guest(user: user)
        def request = new Request(publication: newPublication, deliveryPlace: "place1", returnPlace: "place2", startDate: "2024-01-01", endDate: "2024-01-03", guest: guest)
        request.accept()
        when: "i send a review for that requst"
        def review2 = new Review(text: "no me fue tan bien", score: 3)
        newPublication.sendReview(review2)
        then: "the review was added to the list"
        newPublication.score.reviews.size()==1
    }


    void "if i have two reviews the score is the average"() {
        given: "an existing publication"
        def newPublication = new Publication(host: host, car: car)
        when: "i it has two reviews"
        def review1 = new Review(text: "muy bueno loco", score: 5)
        def review2 = new Review(text: "no me fue tan bien", score: 1)
        newPublication.sendReview(review1)
        newPublication.sendReview(review2)
        and: "i ask for the score of the publication"
        def score = newPublication.calculateScore()
        then: "its the average"
        score == 3
    }
}