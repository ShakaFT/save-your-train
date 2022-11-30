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
                    if (!self.error.isEmpty) {
                        Text(self.error)
                            .font(.system(size: 15))
                            .bold()
                            .foregroundColor(.red)
                    }
                    VStack {
                        HStack {
                            Text("Nom").font(.system(size: 15)).bold()
                            Spacer()
                        }
                        
                        TextField("", text: self.$lastName)
                            .textFieldStyle(.roundedBorder)
                    }
                    
                    VStack {
                        HStack {
                            Text("Prénom").font(.system(size: 15)).bold()
                            Spacer()
                        }
                        
                        TextField("", text: self.$firstName)
                            .textFieldStyle(.roundedBorder)
                    }
                    
                    VStack {
                        HStack {
                            Text("Email").font(.system(size: 15)).bold()
                            Spacer()
                        }
                        
                        TextField("", text: self.$email)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.emailAddress)
                            .disableAutocorrection(true)
                            .autocapitalization(.none)
                            .onChange(of: email, perform: {
                                self.error = ""
                                let _ = $0
                            })
                    }
                    
                    VStack {
                        HStack {
                            Text("Mot de passe").font(.system(size: 15)).bold()
                            Spacer()
                        }
                        
                        SecureField("", text: self.$password)
                            .textFieldStyle(.roundedBorder)
                    }
                    
                    VStack {
                        HStack {
                            Text("Confirmer le mot de passe").font(.system(size: 15)).bold()
                            Spacer()
                        }
                        
                        SecureField("", text: self.$confirmPassword)
                            .textFieldStyle(.roundedBorder)
                    }
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
                        Text("Inscription").padding()
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
