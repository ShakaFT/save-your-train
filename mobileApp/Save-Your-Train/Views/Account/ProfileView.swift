import SwiftUI

struct ProfileView: View {
    
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
                        _ = userState.signOut()
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
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView()
    }
}
