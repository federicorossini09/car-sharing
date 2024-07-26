package car.sharing

class Rent {
    RentStatus status = RentStatus.SCHEDULED

    static constraints = {
    }

    boolean cancel() {
        this.status = RentStatus.CANCELED
    }

    boolean activate() {
        //todo falta chequeo que tenia que estar scheduled antes!!
        //en ese caso convendria hacer un throw?
        this.status = RentStatus.ACTIVE
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
}
