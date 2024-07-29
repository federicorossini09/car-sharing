package car.sharing

import car.sharing.exceptions.RentAlreadyActiveException
import car.sharing.exceptions.RentAlreadyFinishedException
import car.sharing.exceptions.RentCannotBeActivatedException
import car.sharing.exceptions.RentCannotBeFinishedException

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
        if (this.status == RentStatus.ACTIVE)
            throw new RentAlreadyActiveException()
        if (this.status != RentStatus.SCHEDULED)
            throw new RentCannotBeActivatedException()
        this.setKilometersDelivered(currentKilometers)
        this.status = RentStatus.ACTIVE
    }

    boolean finish(Integer kilometers) {
        if (this.status == RentStatus.FINISHED)
            throw new RentAlreadyFinishedException()
        if (this.status != RentStatus.ACTIVE) {
            throw new RentCannotBeFinishedException()
        }
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
