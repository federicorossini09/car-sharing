package car.sharing

import java.time.LocalDate

class Request {

    String deliveryPlace
    String returnPlace
    LocalDate startDate
    LocalDate endDate
    static belongsTo = [guest: Guest]
    Rent rent
    RequestStatus status = RequestStatus.WAITING


    def accept() {
        this.status = RequestStatus.ACCEPTED
        this.rent = new Rent()
    }


    def dateCollision(LocalDate date) {
        return date >= this.startDate && date <= this.endDate
    }

    static constraints = {
        deliveryPlace blank: false
        returnPlace blank: false
        startDate blank: false
        endDate blank: false
        rent nullable: true
    }

    boolean isOccupying(LocalDate startDate, LocalDate endDate) {
        return this.rent && this.rent.isScheduledOrActive()
                && (this.dateCollision(startDate) | this.dateCollision(endDate))
    }

    void reportUndelivered() {
        /*
        if (this.isNotScheduled()) {
            //todo throw error porque tiene que estar programada
        } else if (this.couldReportUndeliver()) {
            //todo throw error porque tiene que esperar cierto tiempo
        }
        //todo chequear que este en estado cancelable

         */
        rent.cancel()


    }

    def reportSuccessfulDeliver(Integer currentKilometers) {
        rent.activate(currentKilometers)
    }

    def reportNotReturned() {
        /*if (this.isNotActive()) {
            //todo throw error porque tiene que estar activa
        } else if (this.couldReportNotReturned()) {
            //todo throw error porque tiene que
        } else if (!rent.isActive()) {
            //todo validar qeu este activa
        }*/
        rent.cancel()
    }

    def reportSuccessfulReturn(Integer kilometers) {
        //todo penalizar al guest si aca nos pasamos
        rent.finish(kilometers)
        //todo publication actualizar precio
    }

    def isFinished() {
        this.rent.isFinished()
    }

}
