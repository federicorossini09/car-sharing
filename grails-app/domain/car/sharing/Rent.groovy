package car.sharing

import car.sharing.exceptions.RentAlreadyActiveException
import car.sharing.exceptions.RentAlreadyFinishedException
import car.sharing.exceptions.RentCannotBeActivatedException
import car.sharing.exceptions.RentCannotBeFinishedException
import car.sharing.exceptions.RentIsNotActiveException
import car.sharing.exceptions.RentNotReturnedNotAvailableException
import car.sharing.exceptions.RentNotScheduledException
import car.sharing.exceptions.RentUndeliverNotAvailableException
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

    def couldReportUndeliver(LocalDateTime startDateTime) {
        def now = LocalDateTime.now()
        long hourDifference = ChronoUnit.HOURS.between(startDateTime, now)
        return hourDifference > 2
    }

    def couldReportNotReturned(LocalDateTime endDateTime) {
        def now = LocalDateTime.now()
        long hourDifference = ChronoUnit.HOURS.between(endDateTime, now)
        return hourDifference > 2
    }

    def reportUndelivered() {

        if (!this.isScheduled()) {
            throw new RentNotScheduledException()
        } else if (!this.couldReportUndeliver()) {
            throw new RentUndeliverNotAvailableException()
        }
        this.cancel()
        this.cancellationReason = CancellationReason.NotDelivered
    }


    def reportNotReturned(LocalDateTime endDateTime) {
        if (!this.isActive()) {
            throw new RentIsNotActiveException()
        } else if (!this.couldReportNotReturned(endDateTime)) {
            throw new RentNotReturnedNotAvailableException()
        }
        this.cancel()
        this.cancellationReason = CancellationReason.NotReturned

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
