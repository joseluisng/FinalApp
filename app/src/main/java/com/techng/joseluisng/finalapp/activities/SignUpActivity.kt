package com.techng.joseluisng.finalapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.Intent
import com.techng.joseluisng.finalapp.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonGoLogin.setOnClickListener {
            //Usando el intent desde la clase kotlin Extensiones.kt
            goToActivity<LoginActivity> {
                //Flag para limpiar la pantalla que se queda atras, o el cache, es decir para eliminar este activity una vez que pase a la siguiente pantalla
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            //Hacer una animación para que pase a la otra activity
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonSignUp.setOnClickListener {
            val email = editTextEmail_signup.text.toString()
            val password = editTextPassword_signup.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)){
                signUpByEmail(email, password)
            }else{
                toast("Por favor llene todos los datos y confirme que la contraseña es correcta.")
            }
        }

        editTextEmail_signup.validate {
            editTextEmail_signup.error = if (isValidEmail(it)) null else "El correo no es valido."
        }

        editTextPassword_signup.validate {
            editTextPassword_signup.error = if (isValidPassword(it)) null else "El password esta vacio o debe contener minimo 4 caracteres."
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(editTextConfirmPassword.text.toString(), it)) null else "Confirmar Password no coincide el campo password."
        }

    }

    private fun signUpByEmail(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                    toast("Un Correo se ha enviado a la cuenta Email agregada, para confirmar tu cuenta.")

                    toast( "Se ha enviado un Email a tu correo, para confirmar tu cuenta.")
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("Ha ocurrido un error, intenta de nuevo.")
            }

        }
    }

    /*private fun isValidEmailAndPassword(email: String, password: String): Boolean{
        return !email.isNullOrEmpty() && !password.isNullOrEmpty() &&
                password == editTextConfirmPassword.text.toString()
    }*/
}
