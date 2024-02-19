package com.creamydark.googleonetaplogin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.creamydark.googleonetaplogin.ui.theme.GoogleOneTapLoginTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleOneTapLoginTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: GoogleSignInViewModel = viewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GoogleSignIn(viewModel)

                }
            }
        }
    }
}

@Composable
fun GoogleSignIn(viewModel: GoogleSignInViewModel) {

    val context = LocalContext.current

    val googleSignInClient: GoogleSignInClient by remember {
        mutableStateOf(
            GoogleSignIn.getClient(
                context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("774052194850-834g2cdv8inbaq6h8suv3cmr56947e7d.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            )
        )
    }

    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                // Successful sign-in, send the account.idToken to your ViewModel
                // for Firebase authentication
                val idToken = account.idToken
                // Call the ViewModel method to sign in with Google
                idToken?.let { viewModel.signInWithGoogle(it) }
            }
        } catch (e: ApiException) {
            // Handle sign-in failure
            e.printStackTrace()
            Log.d("signInWithGoogle", "signInWithGoogle:${e.localizedMessage}")

        }
    }




    Box(Modifier.fillMaxSize()) {
        Button(modifier = Modifier.align(Center), onClick = {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }) {
            Text( text = "Google Sign In")
        }
    }
}


