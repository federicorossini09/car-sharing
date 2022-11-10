package car.sharing

import example.grails.User
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Transactional
@Service(User)
interface UserDataService {

    User save(String username, String password, boolean enabled, boolean accountExpired, boolean accountLocked, boolean passwordExpired)

    void delete(Serializable id)

    int count()
}
