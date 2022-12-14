package car.sharing

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.userdetails.GrailsUser

@Secured('isAuthenticated()')
class HomeController {

    SpringSecurityService springSecurityService

    def index() {
        return [username: loggedUsername()]
    }

    String loggedUsername() {
        if ( springSecurityService.principal == null ) {
            return null
        }
        if ( springSecurityService.principal instanceof String ) {
            return springSecurityService.principal
        }
        if ( springSecurityService.principal instanceof GrailsUser ) {
            return ((GrailsUser) springSecurityService.principal).username
        }
        null
    }
}
