package com.example.pennypenguin.presentation.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.security.MessageDigest
import java.util.UUID
import android.content.pm.PackageManager
import android.os.Build

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    
    // Legacy Google Sign-In setup
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("391534480315-t5kv2lsafhqo2mnmlq8t2gbccbc1dca5.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    
    val legacySignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            viewModel.onSignInResult(account.idToken)
        } catch (e: Exception) {
            Log.e("AuthScreen", "Legacy Sign-in failed", e)
            viewModel.setError("Sign-in failed: ${e.localizedMessage ?: e.message}")
        }
    }

    val errorState by viewModel.error.collectAsState()
    val isLoadingViewModel by viewModel.isLoading.collectAsState()
    var isProcessingLocal by remember { mutableStateOf(false) }
    val isLoading = isLoadingViewModel || isProcessingLocal
    
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isXiaomiDevice by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isXiaomiDevice = Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true) || 
                         Build.MANUFACTURER.equals("POCO", ignoreCase = true)
    }

    LaunchedEffect(errorState) {
        errorState?.let {
            errorMessage = it
            showErrorDialog = true
            viewModel.clearError()
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Sign In Error") },
            text = { 
                Column {
                    Text(errorMessage)
                    if (isXiaomiDevice && errorMessage.contains("timed out", ignoreCase = true)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Xiaomi/POCO devices may block the Google sign-in window. Please ensure 'Display pop-up windows while running in the background' is allowed in app settings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                if (isXiaomiDevice) {
                    TextButton(onClick = {
                        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
                        intent.putExtra("extra_pkgname", context.packageName)
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Fallback to general settings
                            val fallbackIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            fallbackIntent.data = Uri.fromParts("package", context.packageName, null)
                            context.startActivity(fallbackIntent)
                        }
                        showErrorDialog = false
                    }) {
                        Text("Open Permissions")
                    }
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🐧",
                fontSize = 120.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PennyPenguin",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Your cute companion for financial waddling.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    isProcessingLocal = true
                    Log.d("AuthScreen", "Sign in button clicked")
                    
                    // Diagnostic: Log App Signature
                    try {
                        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                        } else {
                            @Suppress("DEPRECATION")
                            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                        }
                        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            packageInfo.signingInfo?.apkContentsSigners
                        } else {
                            @Suppress("DEPRECATION")
                            packageInfo.signatures
                        }
                        signatures?.forEach { signature ->
                            val md = MessageDigest.getInstance("SHA-1")
                            val digest = md.digest(signature.toByteArray())
                            val sha1 = digest.joinToString(":") { "%02X".format(it) }
                            Log.d("AuthScreen", "Runtime SHA-1: $sha1")
                        }
                    } catch (e: Exception) {
                        Log.e("AuthScreen", "Error logging SHA-1", e)
                    }

                    // Diagnostic: Check Google Play Services
                    val googleApiAvailability = GoogleApiAvailability.getInstance()
                    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
                    if (resultCode != ConnectionResult.SUCCESS) {
                        Log.e("AuthScreen", "Google Play Services not available: $resultCode")
                        viewModel.setError("Google Play Services is not available (Code: $resultCode). Please update it.")
                        isProcessingLocal = false
                        return@Button
                    }

                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId("391534480315-t5kv2lsafhqo2mnmlq8t2gbccbc1dca5.apps.googleusercontent.com")
                        .setAutoSelectEnabled(false)
                        .setNonce(UUID.randomUUID().toString()) // Added nonce for stability
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    scope.launch {
                        try {
                            Log.d("AuthScreen", "Calling getCredential with shortened timeout...")
                            
                            val result = withTimeoutOrNull(5000) { // Shortened to 5s
                                credentialManager.getCredential(
                                    context = context,
                                    request = request
                                )
                            }

                            if (result == null) {
                                Log.w("AuthScreen", "CredentialManager timed out, trying legacy fallback...")
                                legacySignInLauncher.launch(googleSignInClient.signInIntent)
                                return@launch
                            }

                            Log.d("AuthScreen", "getCredential result: ${result.credential.type}")
                            val credential = result.credential
                            if (credential is GoogleIdTokenCredential) {
                                viewModel.onSignInResult(credential.idToken)
                            } else {
                                Log.w("AuthScreen", "Unexpected credential type, trying legacy fallback...")
                                legacySignInLauncher.launch(googleSignInClient.signInIntent)
                            }
                        } catch (e: GetCredentialCancellationException) {
                            Log.d("AuthScreen", "Sign in cancelled")
                        } catch (e: NoCredentialException) {
                            Log.e("AuthScreen", "No credentials available, trying legacy fallback...", e)
                            legacySignInLauncher.launch(googleSignInClient.signInIntent)
                        } catch (e: Throwable) {
                            Log.e("AuthScreen", "Error during sign in, trying legacy fallback...", e)
                            legacySignInLauncher.launch(googleSignInClient.signInIntent)
                        } finally {
                            isProcessingLocal = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Sign in with Google")
                }
            }
        }
    }
}
