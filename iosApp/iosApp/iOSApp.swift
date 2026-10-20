import SwiftUI
import UIKit
import ComposeApp // Replace with your actual shared module name

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    init() {
        // This calls the Kotlin function you just created
        KoinHelperKt.doInitKoin(appDeclaration: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
