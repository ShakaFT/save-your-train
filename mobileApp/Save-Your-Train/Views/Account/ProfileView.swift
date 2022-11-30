import SwiftUI

struct ProfileView: View {
    var body: some View {
        NavigationView {
            List {
                HStack {
                    Text("Nom").bold()
                    Divider()
                    Text("\(Constants.firstName) \(Constants.lastName)")
                }
                
                HStack {
                    Text("Email").bold()
                    Divider()
                    Text(Constants.email)
                }
                
                HStack {
                    Spacer()
                    Button(action: {}) {
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
