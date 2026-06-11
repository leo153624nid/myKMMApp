import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        #if DEBUG
            KoinInitializerKt.doInitKoin(isDebug: true)
        #else
            KoinInitializerKt.doInitKoin(isDebug: false)
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}