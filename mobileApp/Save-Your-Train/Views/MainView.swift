import SwiftUI

struct MainView: View {
    
    @ObservedObject var userInfo: UserInfo = UserInfo()
    
    var body: some View {
        if (userInfo.email.isEmpty) {
            LoginView(userInfo: userInfo)
        } else {
            Button(action: {
                Constants.email = ""
                userInfo.email = Constants.email
            }){
                Text("Log out")
            }
            
            TabView {
                ExercisesView()
                    .tabItem {
                        Label("Exercices", systemImage: getExerciseIcon())
                    }
                HistoryView()
                    .tabItem {
                        Label("Historique", systemImage: "chart.bar.xaxis")
                    }
                ProfileView()
                    .tabItem {
                        Label("Profil", systemImage: "person.crop.circle.fill")
                    }
            }
        }
    }
    
    func getExerciseIcon() -> String {
        if(Double(UIDevice.current.systemVersion)! < 16.0 ){
            return "square.and.pencil"
        }
        return "dumbbell.fill"
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
