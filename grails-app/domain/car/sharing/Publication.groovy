package car.sharing

import java.text.DateFormat
import java.text.SimpleDateFormat

class Publication {

    static belongsTo = [host: Host]
    Car car
    PublicationStatus status = PublicationStatus.PENDING
    List requests = []
    static hasMany = [requests: Request]


    static constraints = {
        car validator: {it.validate()}
    }

    def acceptRequest(Request requestToAccept) {
        //aca ver si recibimos el id o se puede directamente el request
        if (areDatesValid(requestToAccept.startDate, requestToAccept.endDate)) {
            requestToAccept.accept()
        }

    }

    void addRequest(Request new_request) {
        this.requests.add(new_request)
    }

    int lengthOfRequests() {
        return requests.size()
    }

    boolean areDatesValid(Date startDate, Date endDate) {
        for (request in requests) {
            if (request.status == RequestStatus.ACCEPTED && (request.dateCollision(startDate) | request.dateCollision(endDate))) {
                return false
            }
        }
        return true
    }

}
