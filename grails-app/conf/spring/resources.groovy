import car.sharing.UserPasswordEncoderListener
import car.sharing.util.ErrorMessageHelper

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    errorMessageHelper(ErrorMessageHelper)
}
