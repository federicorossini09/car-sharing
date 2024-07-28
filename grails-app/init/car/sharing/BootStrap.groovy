package car.sharing

import java.time.LocalDate
import java.time.Period

class BootStrap {

    def init = { servletContext ->
        def user = new User(username: 'admin', password: 'admin').save()
        new Guest(user: user).save()
        def host = new Host(user: user).save()
        def car = new Car(year: 2018, brand: 'Ford', model: 'Focus', variant: '1.6 Titanium', vtvExpirationDate: LocalDate.now() + Period.ofDays(5), kilometers: 20000, licensePlate: "AC933WP").save()
        def price = new Price(car.year, car.kilometers).save()
        def publication = new Publication(car: car, price: price, status: PublicationStatus.ACTIVE)
        host.addToPublications(publication)
        publication.save()
    }
    def destroy = {
    }
}
