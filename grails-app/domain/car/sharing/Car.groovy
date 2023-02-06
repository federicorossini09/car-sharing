package car.sharing
import java.time.Instant

class Car {
    // todo llevar kms maximos a constante (en una clase)
    // todo y año

    String licensePlate
    Integer year
    String brand
    String model
    String variant
    Instant vtvExpirationDate
    Integer kilometers


    static constraints = {
        licensePlate blank: false
        brand  blank: false
        model blank: false
        variant blank: false
        vtvExpirationDate blank: false, validator: {val -> return Instant.now() < val}
        kilometers blank: false, validator: {kms -> return kms < 200000}
        year blank: false, validator: { y -> return y >= 1980 }
    }



}
