import SwiftUI

@main
struct Save_Your_TrainApp: App {
    @StateObject private var dataController = DataController()
    
    var body: some Scene {
        WindowGroup {
            MainView().environment(\.managedObjectContext, dataController.container.viewContext)
        }
    }
}
