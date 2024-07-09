package car.sharing

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification
import java.time.Instant
import java.time.Period

class CarSpec extends Specification implements DomainUnitTest<Car> {

    @Shared Integer year
    @Shared String brand
    @Shared String model
    @Shared String variant
    @Shared Instant vtvExpirationDate
    @Shared String licensePlate
    @Shared Integer kilometers

    def setup() {
        year = 2018
        kilometers = 50000
        brand = "Ford"
        model = "Focus"
        variant = "1.6 Titanium"
        vtvExpirationDate = Instant.now() + Period.ofDays(5)
        licensePlate = "AC933WP"
    }

    def cleanup() {
    }

    void "car cannot be crated without license plate"() {
        when: "create a car without license plate"
        def car = new Car(year: year, brand: brand, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "licensePlate"
    }

    void "car cannot be crated with blank license plate"() {
        when: "create a car with blank license plate"
        def blankLicense = ""
        def car = new Car(year: year, brand: brand, model: model, licensePlate: blankLicense, variant: variant, vtvExpirationDate: vtvExpirationDate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "licensePlate"
    }

    void "car cannot be crated without year"() {
        when: "create a car without year"
        def car = new Car(brand: brand, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "year"
    }

    void "car cannot be crated if year is too old"() {
        when: "create an old car"
        def oldYear = 1970
        def car = new Car(brand: brand, year: oldYear, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "year"
    }


    void "car cannot be crated without brand"() {
        when: "create a car without license plate"
        def car = new Car(year: year, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "brand"
    }

    void "car cannot be crated without model"() {
        when: "create a car without model"
        def car = new Car(year: year, brand: brand, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "model"
    }

    void "car cannot be crated without variant"() {
        when: "create a car without variant"
        def car = new Car(year: year, brand: brand, model: model, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "variant"
    }

    void "car cannot be crated without vtvExpirationDate"() {
        when: "create a car without vtvExpirationDate"
        def car = new Car(year: year, brand: brand, model: model, variant: variant, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "vtvExpirationDate"
    }

    void "car cannot be crated with expired vtvExpirationDate"() {
        when: "create a car without license plate"
        def dateFiveDaysAgo = Instant.now() - Period.ofDays(5)
        def car = new Car(year: year, brand: brand, model: model, variant: variant, vtvExpirationDate: dateFiveDaysAgo, licensePlate: licensePlate, kilometers: kilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "vtvExpirationDate"
    }

    void "car cannot be crated if has more than 200000 kilometers"() {
        when: "create a car with 202000 kilometers"
        def manyKilometers = 202000
        def car = new Car(brand: brand, year: year, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: manyKilometers)
        def validCar = car.validate()
        then: "Car is not valid"
        !validCar && car.errors.getFieldError().getField() == "kilometers"
    }

    void "car creation success"() {
        when: "a car is created with valid data"
            def car = new Car(brand: brand, year: year, model: model, variant: variant, vtvExpirationDate: vtvExpirationDate, licensePlate: licensePlate, kilometers: kilometers)
            def carIsValid = car.validate()
        then: "a valid car is created successfully"
            carIsValid
    }

}
