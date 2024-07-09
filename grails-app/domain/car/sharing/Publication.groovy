package car.sharing

class Publication {

    Host host
    Car car
    PublicationStatus status = PublicationStatus.PENDING

    static constraints = {
        car validator: {it.validate()}
    }
}
