import SwiftUI

struct LoginView: View {

    @EnvironmentObject var userState: UserStateViewModel
    
    @State var email: String = ""
    @State var password: String = ""
    @State var error: String = ""
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: 0) {
                    VStack (spacing: UIScreen.main.bounds.height * 0.025){
                        Text("Connectez vous à votre compte").bold()
                        
                        NavigationLink(destination: SignUpView()) {
                            Text("Pas de compte ? Inscrivez-vous !")
                        }
                        .padding(.leading)
                        .foregroundColor(.green)
                        
                        if (!self.error.isEmpty) {
                            HStack {
                                Text(self.error)
                                    .font(.system(size: 15))
                                    .bold()
                                    .foregroundColor(.red)
                            }
                        }
                        
                        TextField("Email", text: self.$email)
                            .textFieldStyle(.roundedBorder)
                        
                        SecureField("Mot de passe", text: self.$password)
                            .textFieldStyle(.roundedBorder)
                        
                        
                    }.padding()
                    
                    VStack {
                        Button(action: {
                            Task {
                                let worked = await userState.signIn(
                                    email: self.email,
                                    password: self.password
                                )
                                
                                if (!worked) {
                                    self.error = "Email ou mot de passe incorrect !"
                                }
                            }
                        }){
                            Text("Connexion").padding()
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
