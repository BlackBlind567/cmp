# Hybrid Internet CMP App

This repository contains a Kotlin Multiplatform Compose application for Android and iOS. Most product code lives in the shared `composeApp` module, with small Android and iOS entry points that host the shared Compose UI.

The app is structured around customer-facing internet service workflows: login and OTP verification, dashboard/home data, invoices, plans, tickets, service requests, support, profile, and password changes.

## High-Level Architecture

The project follows a practical layered architecture inside `composeApp/src/commonMain`:

```text
Platform entry point
    -> shared App composable
    -> Voyager navigation screen
    -> feature Screen + Content
    -> ScreenModel
    -> Repository interface
    -> Repository implementation
    -> Remote API class
    -> Ktor HttpClient
    -> backend API
```

Core responsibilities:

- `presentation`: Compose UI, feature screens, reusable components, screen state handling.
- `domain`: Repository contracts and implementations used by screen models.
- `data`: API request/response models and remote API classes.
- `core`: shared infrastructure such as networking, session handling, storage, dispatchers, validation, and base state classes.
- `di`: Koin dependency graph.
- `navigation`: shared route definitions.
- platform source sets: Android/iOS-specific hosts and platform implementations.

## Project Layout

```text
.
|-- build.gradle.kts                 # Root Gradle plugin setup.
|-- settings.gradle.kts              # Project name, repositories, included modules.
|-- gradle/libs.versions.toml        # Central dependency and plugin versions.
|-- composeApp/
|   |-- README.md                    # Shared module architecture and file guide.
|   |-- build.gradle.kts             # Multiplatform, Android, iOS, Compose, and dependency setup.
|   |-- composeApp.podspec           # CocoaPods spec for the shared framework.
|   |-- src/
|       |-- commonMain/              # Shared Kotlin and Compose code.
|       |-- androidMain/             # Android entry point and Android-specific actual implementations.
|       |-- iosMain/                 # iOS entry point and iOS-specific actual implementations.
|       |-- commonTest/              # Shared tests.
|-- iosApp/                          # Native iOS host app and Xcode project.
|-- .github/workflows/               # CI build, PR check, and release workflows.
```

## Runtime Entry Points

### Android

- `composeApp/src/androidMain/kotlin/com/hybrid/internet/App.kt`
  - Android `Application` class.
  - Starts Koin through `com.hybrid.internet.di.initKoin`.
  - Passes Android context into Koin.

- `composeApp/src/androidMain/kotlin/com/hybrid/internet/MainActivity.kt`
  - Android activity.
  - Calls `setContent { App() }` to render the shared Compose app.

- `composeApp/src/androidMain/AndroidManifest.xml`
  - Declares the Android app, launcher activity, and application configuration.

### iOS

- `composeApp/src/iosMain/kotlin/com/hybrid/internet/MainViewController.kt`
  - Creates a `ComposeUIViewController`.
  - Hosts the shared `App()` composable for iOS.

- `iosApp/iosApp/iOSApp.swift`
  - Native SwiftUI application entry point.
  - Starts the shared Koin graph from Swift before rendering `ContentView`.

- `iosApp/iosApp/ContentView.swift`
  - Embeds the Kotlin/Compose view controller into the iOS app.

Note: the shared `MainViewController.kt` currently has `initKoin()` commented out because the native iOS app initializes Koin from `iOSApp.swift`.

## Shared App Startup

- `composeApp/src/commonMain/kotlin/com/hybrid/internet/app/App.kt`
  - Main shared Compose root.
  - Applies `AppTheme`.
  - Starts Voyager navigation with `SplashScreen`.
  - Listens to global `UiEvent.Unauthorized` events and replaces the back stack with `LoginScreen`.

- `composeApp/src/commonMain/kotlin/com/hybrid/internet/di/KoinHelper.kt`
  - Shared Koin bootstrap function.
  - Loads `coreModule`.

- `composeApp/src/commonMain/kotlin/com/hybrid/internet/di/CoreModule.kt`
  - Registers core services, API classes, repositories, and screen models.

There is also `composeApp/src/commonMain/kotlin/com/hybrid/internet/app/AppInitializer.kt`, which defines another `initKoin()` helper in the `app` package. The active Android initialization path uses `di/KoinHelper.kt`.

## Dependency Injection

Koin is the dependency injection container. The central graph is defined in:

```text
composeApp/src/commonMain/kotlin/com/hybrid/internet/di/CoreModule.kt
```

It registers:

- singleton infrastructure: `UiEventBus`, `TokenProvider`, `ApiClient`, `HttpClient`, `AppDispatchers`, `LocalStorage`, `SessionManager`.
- remote APIs: `AuthApi`, `DashboardApi`, `PlanApi`, `TicketTrackingApi`, `TicketApi`, `ServiceRequestApi`, `SupportApi`.
- repositories: auth, dashboard, plan, service request, support, ticket, ticket details, change password.
- screen models: splash, login, OTP, home, profile, plans, tracking, ticket, support, service request, change password.

When adding a new feature, the new API, repository, and screen model should be registered here.

## Networking

Networking is built with Ktor.

Important files:

- `core/network/ApiClient.kt`
  - Creates the shared `HttpClient`.
  - Applies the base URL from `AppConfig.BASE_URL`.
  - Installs JSON serialization with `ignoreUnknownKeys`.
  - Installs bearer token auth through `TokenProvider`.
  - Enables Ktor logging through `AppLogger`.

- `core/network/HttpEngine.kt`
  - Expected declaration for platform-specific HTTP engines.

- `androidMain/core/network/HttpEngine.android.kt`
  - Android engine implementation.

- `iosMain/core/network/HttpEngine.ios.kt`
  - iOS engine implementation.

- `core/network/BlindApiCall.kt`
  - Shared API call wrapper.
  - Converts HTTP responses into `NetworkResult`.
  - Handles success, validation errors, unauthorized responses, forbidden responses, server errors, and network exceptions.
  - Calls `SessionManager` when a `401 Unauthorized` response is returned.

- `core/network/NetworkResult.kt`
  - Sealed result type used by repositories and screen models.

- `core/network/HttpError.kt`
  - Shared error model.

- `core/network/RetryPolicy.kt`
  - Retry helper used by network calls.

## Configuration

- `core/config/AppConfig.kt`
  - Holds the selected environment.
  - Resolves the backend base URL.

- `core/config/Environment.kt`
  - Defines available environments.

Current environments point to `https://erp.hybridisp.com`.

## Session And Storage

- `core/storage/LocalStorage.kt`
  - Stores token and logged-in user data using `multiplatform-settings`.
  - Provides `clear()` for logout/session expiry.

- `core/network/TokenProvider.kt`
  - Reads the stored token for Ktor bearer auth.

- `core/session/SessionManager.kt`
  - Handles unauthorized errors.
  - Clears local storage.
  - Emits `UiEvent.Unauthorized`.

- `core/state/UiEvent.kt`
  - Defines UI-level events, including unauthorized navigation and snackbar events.

Unauthorized flow:

```text
API returns 401
    -> blindApiCall creates HttpError.Unauthorized
    -> SessionManager clears LocalStorage
    -> UiEventBus emits UiEvent.Unauthorized
    -> App.kt replaces navigation stack with LoginScreen
```

## Presentation Layer

Shared UI lives under:

```text
composeApp/src/commonMain/kotlin/com/hybrid/internet/presentation
```

Main areas:

- `components`
  - Reusable UI building blocks such as `AppButton`, `AppTextField`, `AppScaffold`, `DropdownSelector`, `OtpInput`, `PasswordField`, loading, error, and top app bar components.

- `features`
  - Feature-specific screens, content composables, cards, lists, and screen models.

- `theme`
  - App colors, color scheme, and Compose theme setup.

Feature naming generally follows this pattern:

```text
FeatureScreen.kt       # Voyager screen and navigation-level wrapper.
FeatureContent.kt      # Stateless or mostly UI-focused Compose content.
FeatureScreenModel.kt  # State, events, repository calls, and UI actions.
```

Examples:

- `presentation/features/login/LoginScreen.kt`
- `presentation/features/login/LoginContent.kt`
- `presentation/features/login/LoginScreenModel.kt`

## Domain And Data Layers

The domain layer defines the repository API consumed by screen models:

```text
domain/repository/<feature>/<FeatureRepository>.kt
domain/repository/<feature>/<FeatureRepositoryImpl>.kt
```

The implementation normally calls one or more remote API classes and wraps responses through `blindApiCall`.

The data layer contains:

- `data/model/request`
  - Request DTOs sent to the backend.

- `data/model/response`
  - Response DTOs decoded from backend responses.

- `data/model/supportmodel`
  - Support-specific local models.

- `data/remote`
  - Ktor API wrappers for backend endpoints.

Example login flow:

```text
LoginScreen
    -> LoginScreenModel.login()
    -> AuthRepository.login()
    -> AuthRepositoryImpl.login()
    -> AuthApi.login()
    -> Ktor HttpClient POST /api/customer/signin
    -> NetworkResult<LoginResponse>
    -> token/user saved in LocalStorage
```

## Feature Map

| Feature | UI package | Screen model | Repository/API |
| --- | --- | --- | --- |
| Splash | `presentation/features/splash` | `SplashScreenModel` | session/storage driven |
| Login | `presentation/features/login` | `LoginScreenModel` | `AuthRepository`, `AuthApi` |
| OTP | `presentation/features/otp` | `OtpScreenModel` | `AuthRepository`, `AuthApi` |
| Home/Dashboard | `presentation/features/home`, `dashboard` | `HomeScreenModel` | `DashboardRepository`, `DashboardApi` |
| Profile | `presentation/features/profile` | `ProfileScreenModel` | local user/session data and auth repository |
| Plans | `presentation/features/plans` | `CustomerPlansScreenModel` | `PlanRepository`, `PlanApi` |
| Plan Tracking | `presentation/features/statusTracking` | `PlanTrackingScreenModel` | `PlanRepository`, `TicketTrackingApi` usage through repository layer |
| Tickets | `presentation/features/ticket`, `ticketHistory` | `TicketScreenModel`, `TicketHistoryScreenModel` | `TicketRepository`, `TicketDetailsRepository`, `TicketApi` |
| Service Requests | `presentation/features/serviceRequest` | `ServiceRequestScreenModel`, `RaiseServiceRequestScreenModel` | `ServiceRequestRepository`, `ServiceRequestApi` |
| Support | `presentation/features/support` | `SupportScreenModel` | `SupportRepository`, `SupportApi` |
| Change Password | `presentation/features/changePassword` | `ChangePasswordScreenModel` | `ChangePasswordRepository`, `AuthApi` |

## Platform-Specific Code

Shared code uses `expect/actual` where platform behavior differs.

Current platform-specific areas:

- `Platform.kt`, `Platform.android.kt`, `Platform.ios.kt`
  - Platform name/info.

- `core/network/HttpEngine.kt`
  - Android and iOS Ktor engine selection.

- `core/image/ImagePicker.kt`
  - Shared image picker contract.

- `androidMain/core/image/ImagePicker.android.kt`
  - Android image picker implementation.

- `iosMain/core/image/ImagePicker.ios.kt`
  - iOS image picker implementation.

- `iosMain/core/image/ImagePickerDelegate.kt`
  - iOS delegate support for image picking.

## Resources

Shared Compose resources:

```text
composeApp/src/commonMain/composeResources
```

Android resources:

```text
composeApp/src/androidMain/res
```

The Compose resource class is configured in `composeApp/build.gradle.kts`:

```kotlin
compose.resources {
    publicResClass = true
    packageOfResClass = "com.hybrid.internet.composeapp.generated.resources"
    generateResClass = auto
}
```

## Build And Run

### Android Debug Build

Windows:

```powershell
.\gradlew.bat :composeApp:assembleDebug
```

macOS/Linux:

```shell
./gradlew :composeApp:assembleDebug
```

### Android Tests

Windows:

```powershell
.\gradlew.bat testDebugUnitTest
```

macOS/Linux:

```shell
./gradlew testDebugUnitTest
```

### Android Release Bundle

Windows:

```powershell
.\gradlew.bat :composeApp:bundleRelease
```

macOS/Linux:

```shell
./gradlew :composeApp:bundleRelease
```

### iOS

Open `iosApp` in Xcode and run the `iosApp` scheme. The iOS app hosts the shared Compose framework generated from `composeApp`.

## CI/CD

GitHub Actions workflows live in `.github/workflows`:

- `build.yml`
  - Runs on pushes to `feature/*` and `develop`.
  - Executes `./gradlew testDebugUnitTest`.

- `pr_check.yml`
  - Runs on pull requests targeting `develop`.
  - Executes `./gradlew testDebugUnitTest`.

- `release.yml`
  - Runs on pushes to `main`.
  - Executes `./gradlew bundleRelease`.

## Adding A New Feature

Use the existing feature pattern:

1. Add request/response DTOs in `data/model/request` and `data/model/response` if the backend contract needs new models.
2. Add a remote API class or extend an existing one in `data/remote`.
3. Add a repository interface and implementation in `domain/repository/<feature>`.
4. Wrap remote calls with `blindApiCall`.
5. Add a `ScreenModel` under `presentation/features/<feature>`.
6. Add screen/content composables under the same feature folder.
7. Register the API, repository, and screen model in `di/CoreModule.kt`.
8. Add navigation from the relevant screen using Voyager.
9. Add or update shared components only when multiple features need the same UI behavior.

## Development Notes

- Keep business flow in `ScreenModel` classes, not inside composables.
- Keep backend endpoint definitions inside `data/remote`.
- Keep persistence access behind `LocalStorage` or repository abstractions.
- Use `NetworkResult` for repository results so UI code handles success and failure consistently.
- Use `UiEvent` for one-time UI events such as snackbars and forced login navigation.
- Update `AppConfig` when changing backend environment behavior.
- Avoid committing secrets, keystore passwords, generated build output, APKs, or local IDE files.
