package car.sharing


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

}
