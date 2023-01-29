package car.sharing

import car.sharing.exceptions.CarCreationException

class Car {

    String licensePlate
    String year
    String brand
    String model
    String variant
    Date vtvExpirationDate


    static constraints = {
        licensePlate nullable: false, blank: false

    }
}
