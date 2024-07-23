package car.sharing

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.userdetails.GrailsUser

@Secured('isAuthenticated()')
class HomeController {

    PublicationService publicationService

    def index() {
        [activePublications: publicationService.getActivePublications()]
    }

}
