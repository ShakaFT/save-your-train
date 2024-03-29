import SwiftUI

struct ProfileView: View {
    
    @FetchRequest(entity: Exercise.entity(), sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @FetchRequest(entity: History.entity(), sortDescriptors: []) var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    
    @EnvironmentObject var userState: UserStateViewModel
    
    
    var body: some View {
        NavigationView {
            List {
                HStack {
                    Text("Nom").bold()
                    Divider()
                    Text("\(UserStateViewModel.firstName) \(UserStateViewModel.lastName)")
                }
                
                HStack {
                    Text("Email").bold()
                    Divider()
                    Text(UserStateViewModel.email)
                }
                
                HStack {
                    Spacer()
                    Button(action: {
                        userState.signOut()
                        self.removeAllData()
                    }) {
                        Text("Se déconnecter").foregroundColor(.red)
                    }
                    Spacer()
                }
            }
            .navigationTitle("Votre profil")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func removeAllData() {
        // remove all exercises
        for exercise: Exercise in self.exercises {
            self.element.delete(exercise)
        }
        
        // remove all history
        for history: History in self.histories {
            self.element.delete(history)
        }
        
        try? self.element.save()
    }
}
