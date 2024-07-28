package car.sharing

import grails.gorm.transactions.Transactional

@Transactional
class RequestService {

    def getById(id) {
        Request.findById(id)
    }

}
