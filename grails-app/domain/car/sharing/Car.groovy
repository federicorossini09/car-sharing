package car.sharing

import car.sharing.exceptions.CarCreationException

class Car {

    String licensePlate
    String year
    String brand
    String model
    String version
    Date vtvExpirationDate


    Car(String licensePlate){
        if(!licensePlate)
            throw new CarCreationException("License Plate is empty")
        this.licensePlate = licensePlate
        this.year = year
        this.brand = brand
        this.model = model
        this.version = version
        this.vtvExpirationDate = vtvExpirationDate
    }



    static constraints = {
    }
}
