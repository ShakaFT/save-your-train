import SwiftUI

struct LoginView: View {

    @EnvironmentObject var userState: UserStateViewModel
    @EnvironmentObject var network: Network
    
    @Environment(\.managedObjectContext) var element
    
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
                        }
                        
                        VStack {
                            HStack {
                                Text("Mot de passe").font(.system(size: 15)).bold()
                                Spacer()
                            }
                            SecureField("", text: self.$password)
                                .textFieldStyle(.roundedBorder)
                        }
                    }.padding()
                    
                    VStack {
                        Components.button(name: "Connexion", action: {
                            Task {
                                let worked = await userState.signIn(
                                    network: self.network,
                                    email: self.email,
                                    password: self.password
                                )
                                
                                if (!worked) {
                                    self.error = "Email ou mot de passe incorrect !"
                                }
                                
                                self.fillLoginData()
                            }
                        }).disabled(disableButton())
                    }
                    Spacer()
                }
            }
        }
    }

    public func disableButton() -> Bool {
        return self.email.range(of: Constants.regexEmail, options: .regularExpression) == nil || self.password.isEmpty
    }
    
    public func fillLoginData() {
        
        for exercise: ExerciseModel in self.network.exercises {
            let exerciseData = Exercise(context: self.element)
            exerciseData.exerciseName = exercise.exerciseName
            exerciseData.exerciseDescription = exercise.description
        }
        
        for history: HistoryModel in self.network.histories {
            let historyData: History = History(context: self.element)
            historyData.exerciseName = history.exerciseName
            historyData.dateMs = history.dateMs
            historyData.execution = history.execution
            historyData.repetition = history.repetition
            historyData.rest = history.rest
            historyData.series = history.series
            historyData.weight = history.weight
        }
        
        try? self.element.save()
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
