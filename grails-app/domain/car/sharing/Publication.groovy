package car.sharing

class Publication {

    static belongsTo = [host: Host]
    Car car
    PublicationStatus status = PublicationStatus.PENDING

    static constraints = {
        car validator: {it.validate()}
    }
}
