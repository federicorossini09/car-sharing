package car.sharing.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.validation.FieldError

class ErrorMessageHelper {

    @Autowired
    MessageSource messageSource

    def getMessage(FieldError error) {
        Locale locale = Locale.getDefault()
        messageSource.getMessage(error, locale)
    }

}
