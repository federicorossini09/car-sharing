package car.sharing

import car.sharing.exceptions.HostNotFoundException
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import java.time.LocalDate

@Secured('isAuthenticated()')
class PublicationController {

    HostService hostService

    PublicationService publicationService

    def myPublications() {

        try {
            [myPublications: hostService.getLoggedUserPublications()]
        } catch (HostNotFoundException e) {
            [error: e, myPublications: []]
        }
    }

    def newPublication() {
        [brands: ['Chevrolet', 'Ford', 'Renault', 'Toyota', 'Volkswagen']]
    }

    def createPublication(params) {
        try {
            def carParams = parseCarParams(params)
            def publication = hostService.createPublication(carParams)
            redirect(action: 'publicationPrice', params: [publicationId: publication.id])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            redirect(action: 'newPublication')
        }
    }

    def parseCarParams(params) {
        params.vtvExpirationDate = params.vtvExpirationDate ? LocalDate.parse(params.vtvExpirationDate) : null
        params
    }

    def publicationPrice(params) {
        [publication: publicationService.getById(params.publicationId)]
    }

    def setPublicationPrice(params) {
        try {
            hostService.setPublicationPrice(Long.valueOf(params.id), new BigDecimal(params.finalValue))
            redirect(action: 'viewPublication', params: [publicationId: params.id, isHost: true])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            redirect(action: 'publicationPrice', params: [publicationId: params.id])
        }
    }

    def viewPublication(params) {
        [publication: publicationService.getById(params.publicationId), isHost: params.isHost]
    }

    def publish(params) {
        hostService.publish(Long.valueOf(params.id))
        redirect(action: 'myPublications')
    }
}
