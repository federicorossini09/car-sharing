package car.sharing


import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import javax.transaction.Transactional

@Transactional
@Secured('permitAll')
class RegistrationController {

    static allowedMethods = [index: 'GET', registration: 'POST']

    def index() { }

    def registration() {

        if(!params.password.equals(params.repassword)) {
            flash.message = "Password and Re-Password not match"
            redirect action: "index"
        } else {
            try {
                def user = User.findByUsername(params.username)?: new User(username: params.username, password: params.password).save()
                redirect controller: "login", action: "auth"
            } catch (ValidationException e) {
                flash.message = "Register Failed"
                redirect action: "index"
            }
        }

    }
}
