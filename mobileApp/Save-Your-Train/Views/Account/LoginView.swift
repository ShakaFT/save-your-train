import SwiftUI

struct LoginView: View {

    @EnvironmentObject var userState: UserStateViewModel
    
    @State var email: String = ""
    @State var password: String = ""
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: 0) {
                    Spacer()
                    VStack (spacing: UIScreen.main.bounds.height * 0.025){
                        TextField("Email", text: self.$email)
                            .textFieldStyle(.roundedBorder)
                        SecureField("Mot de passe", text: self.$password)
                            .textFieldStyle(.roundedBorder)
                    }.padding()
                    
                    VStack {
                        HStack {
                            NavigationLink(destination: SignUpView()) {
                                Text("Cliquez ici pour créer un compte")
                            }.padding(.leading)
                            Spacer()
                        }
                
                        Button(action: {
                            Task {
                                await userState.signIn(
                                    email: self.email,
                                    password: self.password
                                )
                            }
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
        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
