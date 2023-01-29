package car.sharing

import java.time.LocalDateTime

class Car {

    String licensePlate
    String year
    String brand
    String model
    String variant
    LocalDateTime vtvExpirationDate


    static constraints = {
        licensePlate nullable: false, blank: false
    }
}
