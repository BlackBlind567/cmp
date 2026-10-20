# composeApp Module

`composeApp` is the main Kotlin Multiplatform module. It contains the shared Compose UI, domain/repository layer, API clients, session handling, resources, and platform-specific actual implementations for Android and iOS.

## Source Sets

```text
src/
|-- commonMain/      # Shared app code used by Android and iOS.
|-- androidMain/     # Android entry point, resources, and actual implementations.
|-- iosMain/         # iOS view controller and actual implementations.
|-- commonTest/      # Shared tests.
```

## commonMain Package Guide

```text
com/hybrid/internet/
|-- app/             # Shared App composable and app-level initialization helper.
|-- core/            # Infrastructure used across features.
|-- data/            # DTOs and remote API classes.
|-- di/              # Koin module and init function.
|-- domain/          # Repository contracts and implementations.
|-- navigation/      # Route declarations.
|-- presentation/    # Compose UI, screens, screen models, components, and theme.
```

## Request Flow

Most feature requests follow this path:

```text
Screen composable
    -> ScreenModel
    -> Repository interface
    -> Repository implementation
    -> Remote API class
    -> ApiClient HttpClient
    -> backend endpoint
```

Example:

```text
LoginScreen
    -> LoginScreenModel
    -> AuthRepository
    -> AuthRepositoryImpl
    -> AuthApi
    -> POST /api/customer/signin
```

## Core Infrastructure

- `core/base/BaseScreenModel.kt`
  - Base class for Voyager screen models.
  - Provides a coroutine scope and one-time UI event flow.

- `core/network/ApiClient.kt`
  - Builds the shared Ktor `HttpClient`.
  - Applies base URL, JSON configuration, bearer token auth, and logging.

- `core/network/BlindApiCall.kt`
  - Standardizes backend response handling.
  - Maps HTTP responses into `NetworkResult`.
  - Triggers session handling on unauthorized responses.

- `core/network/NetworkResult.kt`
  - Success/failure wrapper returned by repositories.

- `core/session/SessionManager.kt`
  - Clears local session data on unauthorized responses.
  - Emits app-wide unauthorized events.

- `core/storage/LocalStorage.kt`
  - Saves token and user data using multiplatform settings.

- `core/config/AppConfig.kt`
  - Holds active environment and backend base URL.

- `core/state`
  - Shared UI state and event types.

## Feature Structure

Feature UI lives in:

```text
presentation/features/<feature>
```

Typical files:

```text
<Feature>Screen.kt       # Voyager screen and navigation wrapper.
<Feature>Content.kt      # Compose UI content.
<Feature>ScreenModel.kt  # State management, actions, repository calls.
```

Feature data access normally lives in:

```text
data/remote/<Feature>Api.kt
domain/repository/<feature>/<Feature>Repository.kt
domain/repository/<feature>/<Feature>RepositoryImpl.kt
```

Register feature dependencies in:

```text
di/CoreModule.kt
```

## Platform Code

Android-specific files:

- `src/androidMain/kotlin/com/hybrid/internet/App.kt`
  - Android `Application`; initializes Koin.

- `src/androidMain/kotlin/com/hybrid/internet/MainActivity.kt`
  - Hosts the shared `App()` composable.

- `src/androidMain/kotlin/com/hybrid/internet/core/network/HttpEngine.android.kt`
  - Android Ktor engine.

- `src/androidMain/kotlin/com/hybrid/internet/core/image/ImagePicker.android.kt`
  - Android image picker implementation.

iOS-specific files:

- `src/iosMain/kotlin/com/hybrid/internet/MainViewController.kt`
  - Hosts the shared `App()` composable in a `ComposeUIViewController`.
  - Does not start Koin directly; the current native iOS host starts Koin from `iosApp/iosApp/iOSApp.swift`.

- `src/iosMain/kotlin/com/hybrid/internet/core/network/HttpEngine.ios.kt`
  - iOS Ktor engine.

- `src/iosMain/kotlin/com/hybrid/internet/core/image/ImagePicker.ios.kt`
  - iOS image picker implementation.

- `src/iosMain/kotlin/com/hybrid/internet/core/image/ImagePickerDelegate.kt`
  - Native delegate support for iOS image picking.

## Adding Or Updating A Screen

1. Create or update the screen under `presentation/features/<feature>`.
2. Keep Compose files focused on rendering and user actions.
3. Put state transitions and async work in the `ScreenModel`.
4. Call repositories from the `ScreenModel`.
5. Add request/response models under `data/model` when the backend contract changes.
6. Add or update the remote API under `data/remote`.
7. Add repository functions under `domain/repository`.
8. Register new dependencies in `di/CoreModule.kt`.
9. Wire navigation using Voyager from the calling screen.

## Build Tasks

From the repository root:

```powershell
.\gradlew.bat :composeApp:assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat :composeApp:bundleRelease
```
