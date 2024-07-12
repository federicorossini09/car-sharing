package car.sharing

class Request {

    String deliveryPlace
    String returnPlace
    String startDate
    String endDate
    RequestStatus status
    Guest guest
    static constraints = {
    }
}
