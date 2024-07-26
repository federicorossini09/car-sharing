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
        then: "its default value is 100"
        score.value == 100
    }

    void "penalizing reduces 10 percent the score"() {
        given: "an existing score"
        def score = new Score(value: 100)
        when: "the score is penalized"
        score.penalize()
        then: "it is reduced 10 percent"
        score.value == 90
    }
}
