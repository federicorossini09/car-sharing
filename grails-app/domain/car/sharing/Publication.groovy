package car.sharing

class Publication {

    Host host
    Car car
    PublicationStatus status = PublicationStatus.PENDING
    List requests

    static constraints = {
        car validator: {it.validate()}
    }

    void request(String deliveryPlace, String returnPlace, String startDate, String endDate, Guest guest) {
        def new_request = new Request(deliveryPlace, returnPlace, startDate, endDate, RequestStatus.PENDING, guest)
        requests.add(new_request)
    }

    int lengthOfRequests() {
        return requests.size()
    }
}
