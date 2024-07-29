package car.sharing

import car.sharing.exceptions.RentAlreadyActiveException
import car.sharing.exceptions.RentAlreadyFinishedException
import car.sharing.exceptions.RentCannotBeActivatedException
import car.sharing.exceptions.RentCannotBeFinishedException
import org.apache.tomcat.jni.Local

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Rent {
    RentStatus status = RentStatus.SCHEDULED

    Integer kilometersDelivered
    Integer kilometersReturned
    CancellationReason cancellationReason


    static constraints = {
        kilometersDelivered nullable: true
        kilometersReturned nullable: true
    }

    def reportUndelivered() {
        if (this.isScheduled()) {
            this.cancel()
            this.cancellationReason = CancellationReason.NotDelivered
        } else {
            //TODO THROW EXCEPTION
        }
    }

    def reportNotReturned() {
        if (this.isActive()) {
            this.cancel()
            this.cancellationReason = CancellationReason.NotReturned
        } else {
            //TODO THROW EXCEPTION
        }

    }

    def cancelFromHost() {
        if (isScheduled()) {
            this.cancel()
            this.cancellationReason = CancellationReason.Other
        } else {
            //TODO THROW EXCEPTION

        }
    }

    def cancelFromGuest(LocalDateTime scheduledStartDate) {
        def now = LocalDateTime.now()
        long dayDifference = ChronoUnit.DAYS.between(now, scheduledStartDate)
        if (dayDifference > 1) {
            this.cancel()
            this.cancellationReason = CancellationReason.Other
        } else {
            //TODO THROW EXCEPTION
        }

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

    def isScheduled() {
        return this.status == RentStatus.SCHEDULED

    }
}
