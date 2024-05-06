package com.adrammedia.storenet.utils

import android.util.Patterns

sealed class RegisterValidation (){
    data object Success:RegisterValidation()
    data class Failed(val message:String): RegisterValidation()

}

data class RegisterFieldState(
    val fullName: RegisterValidation,
    val email:RegisterValidation,
    val password: RegisterValidation

)

fun validateFullName(fullName: String):RegisterValidation {
    if(fullName.isEmpty()) {
        return RegisterValidation.Failed("The full name is required")
    }
    if(fullName.length <= 4){
        return RegisterValidation.Failed("The full name must be more than 4 characters")
    }
    return RegisterValidation.Success
}

fun validateEmail(email: String):RegisterValidation {

    if(email.isEmpty()) {
        return RegisterValidation.Failed("The email is required")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("The email must be a valid email")
    }
    return RegisterValidation.Success
}


fun validatePassword(password: String):RegisterValidation {
    if(password.isEmpty()) {
        return RegisterValidation.Failed("The password is required")
    }
    if(password.length < 6){
        return RegisterValidation.Failed("The Password must be more than 5 characters")
    }
    return RegisterValidation.Success


    //in view model

    //        private val _registerValidation = Channel<RegisterFieldState>()
//         val registerValidation = _registerValidation.receiveAsFlow()
    //        val validateFullName = validateFullName(fullName)
//        val validateEmail = validateEmail(email)
//        val validatePassword = validatePassword(password)
//        val shouldRegister = validateFullName is RegisterValidation.Success &&
//                             validateEmail is RegisterValidation.Success
//                             validatePassword is RegisterValidation.Success
//        if (shouldRegister) {
//        } else {
//            val registerFieldState = RegisterFieldState(
//                validateFullName(fullName),
//                validateEmail(email),
//                validatePassword(password)
//            )
//            _registerValidation.send(registerFieldState)
//        }

    //Register activity
    //                    lifecycleScope.launch {
//                        registerLoginViewModel.registerValidation.collect {validation->
//                            if (validation.fullName is  RegisterValidation.Failed){
//                                withContext(Dispatchers.Main) {
//                                    binding.progressBar.visibility =View.GONE
//                                    binding.etRegiName.apply {
//                                        requestFocus()
//                                        error = validation.fullName.message
//                                    }
//                                }
//                            }
//                            if (validation.email is  RegisterValidation.Failed){
//                                withContext(Dispatchers.Main) {
//                                    binding.progressBar.visibility =View.GONE
//                                    binding.etRegiEmail.apply {
//                                        requestFocus()
//                                        error = validation.email.message
//                                    }
//
//                                }
//                            }
//                            if (validation.password is  RegisterValidation.Failed){
//                                withContext(Dispatchers.Main) {
//                                    binding.progressBar.visibility =View.GONE
//                                    binding.etRegiPassword.apply {
//                                        requestFocus()
//                                        error = validation.password.message
//                                    }
//                                }
//                            }
//                        }
//                    }
}

