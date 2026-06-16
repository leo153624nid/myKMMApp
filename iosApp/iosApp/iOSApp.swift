import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        #if DEBUG
            KoinInitializerKt.doInitKoin(isDebug: true, extraModules: [])
        #else
            KoinInitializerKt.doInitKoin(isDebug: false, extraModules: [])
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}