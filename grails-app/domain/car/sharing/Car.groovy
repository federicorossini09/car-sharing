package car.sharing

import car.sharing.exceptions.CarCreationException

class Car {

    String licensePlate
    String year
    String brand
    String model
    String variant
    Date vtvExpirationDate


    Car(String licensePlate, String year, String brand, String model, String variant, Date vtvExpirationDate){
        if(!licensePlate)
            throw new CarCreationException("License Plate is empty")
        this.licensePlate = licensePlate
        this.year = year
        this.brand = brand
        this.model = model
        this.variant = variant
        this.vtvExpirationDate = vtvExpirationDate
    }



    static constraints = {
    }
}
