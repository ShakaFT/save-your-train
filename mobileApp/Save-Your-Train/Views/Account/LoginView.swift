import SwiftUI

struct LoginView: View {

    var userInfo: UserInfo
    @State var email: String = ""
    @State var password: String = ""
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: 0) {
                    Spacer()
                    VStack (spacing: UIScreen.main.bounds.height * 0.05){
                        TextField("Email", text: self.$email)
                            .textFieldStyle(.roundedBorder)
                        SecureField("Password", text: self.$password)
                            .textFieldStyle(.roundedBorder)
                    }.padding()
                    
                    VStack {
                        Button(action: {
                            Constants.email = self.email
                            userInfo.email = Constants.email
                        }){
                            Text("Se connecter").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                        .disabled(self.email.isEmpty || self.password.isEmpty)
                        .padding()
                    }
                    Spacer()
                }
            }
            .navigationBarTitle("Connexion")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView(userInfo: UserInfo())
    }
}
