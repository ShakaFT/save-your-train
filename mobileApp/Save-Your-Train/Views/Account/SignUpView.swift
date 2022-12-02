import SwiftUI

struct SignUpView: View {
    
    @EnvironmentObject var network: Network
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
                        Components.error(text: self.error)
                    }
                    VStack {
                        Components.label(text: "Nom")
                        Components.textField(text: self.$lastName)
                    }
                    
                    VStack {
                        Components.label(text: "Prénom")
                        Components.textField(text: self.$firstName)
                    }
                    
                    VStack {
                        Components.label(text: "Email")
                        Components.textField(text: self.$email)
                            .keyboardType(.emailAddress)
                            .disableAutocorrection(true)
                            .autocapitalization(.none)
                            .onChange(of: email, perform: {
                                self.error = ""
                                let _ = $0
                            })
                    }
                    
                    VStack {
                        Components.label(text: "Mot de passe")
                        Components.textField(text: self.$password, password: true)
                        
                        if (1...7 ~= self.password.count) {
                            Components.error(text: "Le mot de passe doit contenir 8 caractères minimum")
                        }
                    }
                    
                    VStack {
                        Components.label(text: "Confirmer le mot de passe")
                        Components.textField(text: self.$confirmPassword, password: true)
                        
                        if (self.password != self.confirmPassword && !self.confirmPassword.isEmpty) {
                            Components.error(text: "Les 2 mots de passe doivent être identiques")
                        }
                    }
                }.padding()
                
                VStack {
                    Components.button(name: "Inscription", action: {
                        Task {
                            let success = await userState.signUp(
                                network: self.network, 
                                firstName: self.firstName,
                                lastName: self.lastName,
                                email: self.email,
                                password: self.password
                            )
                            if (!success) {
                                self.error = "Cet email existe déjà"
                            }
                        }
                    }).disabled(disableButton())
                    Spacer()
                }
            }
        }
        .navigationBarTitle("Créer un compte")
        .navigationBarTitleDisplayMode(.inline)
    }
    
    public func disableButton() -> Bool {
        return self.email.range(of: Constants.regexEmail, options: .regularExpression) == nil
            || 0...7 ~= self.password.count
            || self.lastName.isEmpty
            || self.firstName.isEmpty
            || self.confirmPassword != self.password
    }
}
