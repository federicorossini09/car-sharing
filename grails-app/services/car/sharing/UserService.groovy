package car.sharing

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser

@Transactional
class UserService {

    SpringSecurityService springSecurityService

    String getLoggedUsername() {
        if (springSecurityService.principal == null) {
            return null
        }
        if (springSecurityService.principal instanceof String) {
            return springSecurityService.principal
        }
        if (springSecurityService.principal instanceof GrailsUser) {
            return ((GrailsUser) springSecurityService.principal).username
        }
        null
    }

    User getLoggedUser() {

        String username = null

        if (springSecurityService.principal == null) {
            return null
        }
        if (springSecurityService.principal instanceof String) {
            username = springSecurityService.principal
        }
        if (springSecurityService.principal instanceof GrailsUser) {
            username = ((GrailsUser) springSecurityService.principal).username
        }

        User.findByUsername(username) ?: null

    }
}
