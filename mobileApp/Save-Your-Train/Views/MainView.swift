import SwiftUI

struct MainView: View {
    var body: some View {
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