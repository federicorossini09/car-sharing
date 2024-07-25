package car.sharing

import grails.gorm.transactions.Transactional

@Transactional
class GuestService {

    UserService userService

    Guest getLoggedGuest() {
        def user = userService.getLoggedUser()
        Guest.findByUser(user)
    }
}
