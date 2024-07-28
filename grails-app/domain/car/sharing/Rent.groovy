package car.sharing

class Rent {
    RentStatus status = RentStatus.SCHEDULED

    Integer kilometersDelivered
    Integer kilometersReturned


    static constraints = {
        kilometersDelivered nullable: true
        kilometersReturned nullable: true
    }

    boolean cancel() {
        this.setStatus(RentStatus.CANCELED)
    }

    boolean activate(Integer currentKilometers) {
        this.setKilometersDelivered(currentKilometers)
        this.status = RentStatus.ACTIVE
    }

    boolean finish(Integer kilometers) {
        this.setKilometersReturned(kilometers)
        this.setStatus(RentStatus.FINISHED)
    }

    boolean isScheduledOrActive() {
        return (this.status == RentStatus.SCHEDULED | this.status == RentStatus.ACTIVE)
    }

    boolean isCanceled() {
        return this.status == RentStatus.CANCELED
    }


    boolean isActive() {
        return this.status == RentStatus.ACTIVE
    }

    def isFinished() {
        return this.status == RentStatus.FINISHED
    }
}
