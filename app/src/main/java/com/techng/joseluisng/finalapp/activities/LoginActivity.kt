package com.techng.joseluisng.finalapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.techng.joseluisng.finalapp.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val RC_GOOGLE_SIGN_IN = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(isValidEmail(email) && isValidPassword(password)){
                loginByEmail(email, password)
            }else{
                toast("Por favor llene todos los datos y confirme que la contraseña es correcta.")
            }
        }

        buttonCreateAcount.setOnClickListener {
            //Usando el intent de la clase kotlin Extensiones.kt algo similar a la librería anko
           goToActivity<SignUpActivity>()
            //Animación cuando pasa de un pantalla a otra
           overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        textViewForgotPassword.setOnClickListener {
            //Usando el intent de la clase Kotlin Extensiones.kt algo similar a la librería anko
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        buttonLogInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "El correo no es valido."
        }

        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "El password esta vacio o debe contener minimo 4 caracteres"
        }

    }

    //FUNCIÓN PARA OBTENER EL API DE LAS CUENTAS DE GOOGLE
    private fun getGoogleApiClient(): GoogleApiClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    //FUNCIÓN PARA HACER LOGÓN CON GOOGLE Y FIREBASE
    private fun loginByGoogleAcountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if(mGoogleApiClient.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            }
            goToActivity<MainActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    //FUNCION PARA ACCEDER POR MEDIO DEL EMAIL QUE SE CREO LA CUENTA
    private fun loginByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                if (mAuth.currentUser!!.isEmailVerified){
                    goToActivity<MainActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                } else {
                    toast("Necesitar cofirmar tu email en el correo que te enviamos.")
                }
            }else{
                toast("Ocurrio un error, intenta otra vez.")
            }
        }

    }

    //FUNCIÓN SOBRE ESCRITA QUE RECIBE EL RESULTADO DE CUANDO SE HACE LOGÍN CON CUENTA GOOGLE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess){
                val account = result.signInAccount
                loginByGoogleAcountIntoFirebase(account!!)
            }
        }
    }

    //FUNCIÓN POR SI HAY UN FALLO EN LA CONEXIÓN CON GOOGLE Y FIREBASE
    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("La conección fallo!!")
    }



    /* private fun isValidEmailAndPassword(email: String, password: String): Boolean{
         return !email.isNullOrEmpty() && !password.isNullOrEmpty()
     }*/
}
