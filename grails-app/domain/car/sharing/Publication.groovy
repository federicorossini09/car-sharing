package car.sharing

class Publication {

    static belongsTo = [host: Host]
    Car car
    Price price
    PublicationStatus status = PublicationStatus.PENDING

    static constraints = {
        car validator: {it.validate()}
        price validator: {it.validate()}
    }

    Price updatePrice(Integer newValue) {
        this.price.updateFinalValue(newValue)
    }
}
