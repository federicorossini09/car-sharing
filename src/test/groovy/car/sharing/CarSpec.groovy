package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import car.sharing.exceptions.CarCreationException

class CarSpec extends Specification implements DomainUnitTest<Car> {

    def setup() {
    }

    def cleanup() {
    }

    void "car cannot be crated without license plate"() {
        //given:
        when: "create a car without license plate"
        def a_car = new Car("")
        then: "CarCreationException is received"
        def exception = thrown(CarCreationException)
        exception.getMessage() == "License Plate is empty"
    }
}
