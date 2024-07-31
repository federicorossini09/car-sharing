package car.sharing

import car.sharing.exceptions.KilometersDeliveredBelowPublishedException
import car.sharing.exceptions.NewKilometersCannotBeLessThanCurrent

import java.time.LocalDate

class Car {
    // todo llevar kms maximos a constante (en una clase)
    // todo y año

    String licensePlate
    Integer year
    String brand
    String model
    String variant
    LocalDate vtvExpirationDate
    Integer kilometers

    static constraints = {
        licensePlate blank: false
        brand blank: false
        model blank: false
        variant blank: false
        vtvExpirationDate nullable: false, validator: { val -> return LocalDate.now() < val }
        kilometers blank: false, validator: { kms -> return kms < 200000 }
        year blank: false, validator: { if (it < 1980) 'tooOld' }
    }

    def checkKilometersDelivered(Integer kilometersDelivered) {
        if (kilometersDelivered < this.kilometers)
            throw new KilometersDeliveredBelowPublishedException()
    }

    def updateKilometers(Integer kilometers) {
        if (kilometers < this.kilometers)
            throw new NewKilometersCannotBeLessThanCurrent()
        this.setKilometers(kilometers)
    }
}
