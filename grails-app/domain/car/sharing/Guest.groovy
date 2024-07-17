package car.sharing

class Guest {

    User user
    static hasMany = [requests: Request]


    static constraints = {
    }

    Publication createFirstPublication(Car car) {

        def newHost = new Host(user: this.user)

        newHost.createPublication(car)
    }

    void addRequest(Publication publication, String deliveryPlace, String returnPlace, String startDate, String endDate) {
        def newRequest = new Request(publication, deliveryPlace, returnPlace, startDate, endDate, this)
        requests.add(newRequest)
    }
}
