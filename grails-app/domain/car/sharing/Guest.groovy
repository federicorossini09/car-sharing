package car.sharing

class Guest {

    User user

    static constraints = {
    }

    Publication createFirstPublication(Car car) {

        def newHost = new Host(user: this.user)

        newHost.createPublication(car)
    }
}
