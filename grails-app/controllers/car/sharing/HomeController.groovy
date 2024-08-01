package car.sharing


import grails.plugin.springsecurity.annotation.Secured

@Secured('isAuthenticated()')
class HomeController extends AbstractController {

    PublicationService publicationService

    def index() {
        [activePublications: publicationService.getActivePublications()]
    }

}
