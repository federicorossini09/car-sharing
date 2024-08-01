package car.sharing

import car.sharing.exceptions.HostNotFoundException
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import java.time.LocalDateTime

@Secured('isAuthenticated()')
class PublicationController extends AbstractController {

    HostService hostService

    PublicationService publicationService

    UserService userService

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
        params.vtvExpirationDate = params.vtvExpirationDate ? LocalDateTime.parse(params.vtvExpirationDate + "T00:00:00") : null
        params
    }

    def publicationPrice(params) {
        [publication: publicationService.getById(params.publicationId)]
    }

    def setPublicationPrice(params) {
        try {
            hostService.setPublicationPrice(Long.valueOf(params.id), new BigDecimal(params.finalValue))
            redirect(action: 'viewPublication', params: [id: params.id])
        } catch (ValidationException e) {
            flash.errors = e.errors
            flash.values = params
            redirect(action: 'publicationPrice', params: [publicationId: params.id])
        }
    }

    def viewPublication(params) {
        def publicationId = Long.valueOf(params.id)
        [publication: publicationService.getById(publicationId), isHost: hostService.isLoggedHostPublication(publicationId)]
    }

    def publish(params) {
        hostService.publish(Long.valueOf(params.id))
        flash.successMessage = 'La Publicación fue activada'
        redirect(action: 'viewPublication', params: [id: params.id])
    }

    def unpublish(params) {
        hostService.unpublish(Long.valueOf(params.id))
        flash.successMessage = 'La Publicación fue desactivada'
        redirect(action: 'viewPublication', params: [id: params.id])
    }
}
