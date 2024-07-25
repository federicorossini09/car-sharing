package car.sharing

class Host {

    User user
    List publications = []
    static hasMany = [publications: Publication]

    static constraints = {
    }

    Publication createPublication(Car car) {

        def newPublication = new Publication(host: this, car: car)

        this.publications.add(newPublication)

        newPublication
    }
}
