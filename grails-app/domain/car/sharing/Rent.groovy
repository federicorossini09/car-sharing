package car.sharing

class Rent {
    RentStatus status = RentStatus.SCHEDULED

    static constraints = {
    }

    boolean cancel() {
        this.status = RentStatus.CANCELED
    }

    boolean isScheduledOrActive() {
        return (this.status == RentStatus.SCHEDULED | this.status == RentStatus.ACTIVE)
    }

}
