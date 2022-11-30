import SwiftUI

@main
struct Save_Your_TrainApp: App {
    
    @StateObject var userState: UserStateViewModel = UserStateViewModel()
    @StateObject var network: Network = Network()
    
    var body: some Scene {
        WindowGroup {
            NavigationView{
                ApplicationSwitcher()
            }
            .environmentObject(userState)
            .environmentObject(network)
        }
    }
}

struct ApplicationSwitcher: View {
    
    @EnvironmentObject var userState: UserStateViewModel
    @StateObject private var dataController = DataController()
    
    var body: some View {
        if (userState.isLoggedIn) {
            MainView().environment(\.managedObjectContext, dataController.container.viewContext)
        } else {
            LoginView()
                .navigationBarTitle("Se connecter")
                .navigationBarTitleDisplayMode(.inline)
        }
        
    }
}
