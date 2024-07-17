package car.sharing

import java.text.DateFormat
import java.text.SimpleDateFormat


class Request {

    String deliveryPlace
    String returnPlace
    Date startDate
    Date endDate
    RequestStatus status
    static belongsTo = [guest: Guest]

    Request(Publication publication, String deliveryPlace, String returnPlace, String startDate, String endDate, Guest guest) {
        this.status = RequestStatus.WAITING
        this.deliveryPlace = deliveryPlace
        this.returnPlace = returnPlace
        this.startDate = stringToDate(startDate)
        this.endDate = stringToDate(endDate)
        this.guest = guest
        //le pregunto a la publicacion si esta disponible en mis fechas, sino no me puedo construir
        //y creo un self con lo recibido por parametro
        //y llamo desde aca a publicacion.request
        if (publication.areDatesValid(this.startDate, this.endDate)) {
            publication.addRequest(this)
        }
    }


    def stringToDate(String dateString) {
        def datePattern = "yyyy-MM-dd"; // define the desired date/time format
        DateFormat formatter = new SimpleDateFormat(datePattern);
        return formatter.parse(dateString)
    }

    def dateCollision(Date date) {
        return date > this.startDate && date < this.endDate
    }

    static constraints = {
        deliveryPlace blank: false
        returnPlace blank: false
        startDate blank: false
        endDate blank: false
    }
}
