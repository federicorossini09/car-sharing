package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ScoreSpec extends Specification implements DomainUnitTest<Score> {

    def setup() {
    }

    def cleanup() {
    }


    void "default score at creation"() {
        when: "a score is created without parameters"
        def score = new Score()
        then: "its default value is 3"
        score.value == 3
    }

    void "score without penalties calculates as average"() {
        given: "an existing score with two reviews"
        def score = new Score()
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "muy malo", score: 1)
        score.sendReview(review1)
        score.sendReview(review2)
        when: "the score is calculated"
        score.calculate()
        then: "it is reduced 10 percent"
        score.value == 3
    }

    void "score with two reviews receives penalty"() {
        given: "an existing score with two reviews"
        def score = new Score()
        def review1 = new Review(text: "muy bueno", score: 5)
        def review2 = new Review(text: "muy malo", score: 1)
        score.sendReview(review1)
        score.sendReview(review2)
        when: "it receives a penalty of NotDeliverOnTime"
        score.penalize(PenaltyReason.NotDeliverOnTime)
        then: "it is reduced 10 percent from the average"
        score.value == 2.7
    }

}
