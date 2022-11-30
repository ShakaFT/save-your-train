import SwiftUI

struct SignUpView: View {
    
    @EnvironmentObject var userState: UserStateViewModel
    
    @State var firstName: String = ""
    @State var lastName: String = ""
    @State var email: String = ""
    @State var password: String = ""
    @State var confirmPassword: String = ""
    @State var error: String = ""
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                Spacer()
                VStack (spacing: UIScreen.main.bounds.height * 0.025){
                    TextField("Nom", text: self.$lastName)
                        .textFieldStyle(.roundedBorder)
                    
                    TextField("Prénom", text: self.$firstName)
                        .textFieldStyle(.roundedBorder)
                    
                    TextField("Email", text: self.$email)
                        .textFieldStyle(.roundedBorder)
                        .onChange(of: email, perform: {
                            self.error = ""
                            let _ = $0
                        })
                    
                    if (!self.error.isEmpty) {
                        HStack {
                            Text(self.error)
                                .font(.system(size: 15))
                                .bold()
                                .foregroundColor(.red)
                            Spacer()
                        }
                    }
                    SecureField("Mot de passe", text: self.$password)
                        .textFieldStyle(.roundedBorder)
                    
                    SecureField("Confirmer le mot de passe", text: self.$confirmPassword)
                        .textFieldStyle(.roundedBorder)
                }.padding()
                
                VStack {
                    Button(action: {
                        Task {
                            let success = await userState.signUp(
                                firstName: self.firstName,
                                lastName: self.lastName,
                                email: self.email,
                                password: self.password
                            )
                            if (!success) {
                                self.error = "Cet email existe déjà"
                            }
                        }
                    }){
                        Text("Se connecter").padding()
                    }
                    .cornerRadius(10)
                    .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                    .disabled(self.email.isEmpty || self.password.isEmpty || self.lastName.isEmpty || self.firstName.isEmpty || self.confirmPassword.isEmpty)
                    .padding()
                }
                Spacer()
            }
        }
        .navigationBarTitle("Créer un compte")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}
