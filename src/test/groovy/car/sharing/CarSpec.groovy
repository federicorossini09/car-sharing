package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification
import car.sharing.exceptions.CarCreationException

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CarSpec extends Specification implements DomainUnitTest<Car> {

    @Shared String year
    @Shared String brand
    @Shared String model
    @Shared String variant
    @Shared LocalDateTime vtvExpirationDate

    def setup() {
        year = 2018
        brand = "Ford"
        model = "Focus"
        variant = "1.6 Titanium"
        vtvExpirationDate = LocalDateTime.of(2020, 1, 1, 1, 0)
    }

    def cleanup() {
    }

    void "car cannot be crated without license plate"() {
        when: "create a car without license plate"
        def a_car = new Car(year: year, brand: brand, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate)
        def validCar = a_car.validate()
        then: "Car is not valid"
        !validCar && a_car.errors.getFieldError().getField() == "licensePlate"
    }
}
