package car.sharing

import grails.gorm.transactions.Transactional

@Transactional
class PublicationService {

    def getActivePublications() {
        Publication.findAllByStatus(PublicationStatus.ACTIVE)
    }

    def getById(id) {
        Publication.findById(id)
    }

}
