# Implementation Plan - Google Sign-In with Firebase

The goal is to implement Google Sign-In using Firebase Authentication and the modern Android Credential Manager API.

## Proposed Changes

### Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/gradle/libs.versions.toml)
- Add versions for `credentials` and `googleid`.
- Add libraries: `androidx-credentials`, `androidx-credentials-play-services-auth`, and `googleid`.

#### [MODIFY] [build.gradle.kts](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/build.gradle.kts)
- Add the new dependencies to the `dependencies` block.

### Dependency Injection

#### [MODIFY] [AuthModule.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/di/AuthModule.kt)
- Add a `@Provides` method to provide an instance of `FirebaseAuth`.

### Data Layer

#### [MODIFY] [AuthRepository.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/domain/repository/AuthRepository.kt)
- (Optional) Add a method to get the current user's display name or email if needed for the UI.

#### [MODIFY] [AuthRepositoryImpl.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/repository/AuthRepositoryImpl.kt)
- Inject `FirebaseAuth`.
- Implement `isAuthenticated` as a `StateFlow` that reacts to `FirebaseAuth` state changes.
- Implement `signInWithGoogle` using `GoogleAuthProvider.getCredential`.
- Implement `signOut` using `Firebase.auth.signOut()`.

### Presentation Layer

#### [MODIFY] [AuthScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/auth/AuthScreen.kt)
- Inject `Context`.
- Implement the `Credential Manager` flow:
    1.  Create a `GetGoogleIdOption`.
    2.  Build a `GetCredentialRequest`.
    3.  Call `credentialManager.getCredential`.
    4.  Pass the resulting `idToken` to `AuthViewModel`.

#### [MODIFY] [AuthViewModel.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/auth/AuthViewModel.kt)
- Update `onSignInResult` to handle the sign-in result from the repository and potentially expose loading or error states.

## Verification Plan

### Manual Verification
1.  Launch the app and reach the **Auth Screen**.
2.  Tap **Sign in with Google**.
3.  Ensure the Google account picker appears.
4.  Select an account.
5.  Verify successful redirection to the **Dashboard**.
6.  Verify that the user remains logged in after restarting the app.
7.  Verify that clicking "Sign Out" in the Profile works correctly.
