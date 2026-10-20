import FirebaseCore
import FirebaseMessaging
import UIKit
import UserNotifications

final class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        NSLog("HybridFCM[iOS] didFinishLaunching launchOptions=\(String(describing: launchOptions))")

        configureFirebaseIfAvailable()
        configureNotifications(application)

        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        let apnsToken = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        NSLog("HybridFCM[iOS] APNs token=\(apnsToken)")
        Messaging.messaging().apnsToken = deviceToken
        fetchAndLogFcmToken(reason: "APNs token registered")
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        NSLog("HybridFCM[iOS] APNs registration failed error=\(error.localizedDescription)")
    }

    private func configureFirebaseIfAvailable() {
        guard FirebaseApp.app() == nil else {
            NSLog("HybridFCM[iOS] Firebase already configured")
            return
        }

        guard Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil else {
            NSLog("HybridFCM[iOS] GoogleService-Info.plist missing; FCM disabled for iOS")
            return
        }

        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        NSLog("HybridFCM[iOS] Firebase configured")
        fetchAndLogFcmToken(reason: "Firebase configured")
    }

    private func configureNotifications(_ application: UIApplication) {
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            NSLog("HybridFCM[iOS] notification permission granted=\(granted), error=\(String(describing: error))")
            DispatchQueue.main.async {
                application.registerForRemoteNotifications()
            }
        }
    }

    private func fetchAndLogFcmToken(reason: String) {
        guard FirebaseApp.app() != nil else {
            NSLog("HybridFCM[iOS] skip FCM token fetch; Firebase not configured. reason=\(reason)")
            return
        }

        Messaging.messaging().token { token, error in
            if let error {
                NSLog("HybridFCM[iOS] FCM token fetch failed reason=\(reason), error=\(error.localizedDescription)")
                return
            }

            NSLog("HybridFCM[iOS] FCM token reason=\(reason), token=\(token ?? "nil")")
            print("IOS_FCM_TOKEN=\(token ?? "nil")")
            if let token {
                PushTokenStoreKt.savePushToken(token: token)
            }
        }
    }
}

extension AppDelegate: MessagingDelegate {
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        NSLog("HybridFCM[iOS] FCM token refreshed token=\(fcmToken ?? "nil")")
        print("IOS_FCM_TOKEN_REFRESHED=\(fcmToken ?? "nil")")
        if let fcmToken {
            PushTokenStoreKt.savePushToken(token: fcmToken)
        }
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        let userInfo = notification.request.content.userInfo
        NSLog("HybridFCM[iOS] notification foreground userInfo=\(userInfo)")
        completionHandler([.banner, .sound, .badge])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        NSLog("HybridFCM[iOS] notification tapped action=\(response.actionIdentifier), userInfo=\(userInfo)")
        completionHandler()
    }
}
