import SwiftUI

struct AddExerciseView: View {
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @State public var name: String = ""
    @State public var networkFailed: Bool = false
    @State private var description: String = ""
    
    @State private var disabled: Bool = true
    @State private var error: String = ""
    
    var body: some View {
        NavigationView {
            VStack{
                VStack(alignment: .leading){
                    Spacer()
                    
                    VStack {
                        HStack {
                            Text("Nom")
                            Spacer()
                        }
                        
                        TextField("Nom de l'exercice...", text: self.$name)
                            .textFieldStyle(.roundedBorder)
                            .onChange(of: self.name, perform: { self.activeButton(name: $0) })
                    }.padding()
                    
                    Text(self.error)
                        .bold()
                        .foregroundColor(.red)
                        .padding(.horizontal)
                        .font(.system(size: 15))
                    
                    VStack {
                        HStack {
                            Text("Description")
                            Spacer()
                        }
                        
                        TextField("Description de l'exercice...", text: self.$description)
                            .textFieldStyle(.roundedBorder)
                        }.padding()
                    
                    if (self.description.count > 100) {
                        Text("100 caractères maximum")
                            .font(.system(size: 15))
                            .bold()
                            .padding(.horizontal)
                            .foregroundColor(.red)
                    }
                }
                .cornerRadius(20)
                .padding()
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
                
                if (self.networkFailed) {
                    Text("Une erreur est survenue, veuillez réessayer plus tard...")
                        .bold()
                        .foregroundColor(.red)
                        .multilineTextAlignment(.center)
                        .padding(.leading)
                }
                
                Button(action: {
                    Task {
                        self.disabled = true
                        await self.addExercise()
                        self.disabled = false
                    }
                }) {
                    Text("Ajouter").padding()
                }
                .disabled(self.disabled || self.description.count > 100)
                .cornerRadius(10)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .top)
                .padding()
            }
        }
    }
    
    func addExercise() async {
        let exerciseRemote: ExerciseModel = ExerciseModel(exerciseName: self.name, description: self.description)
        
        let worked: Bool = try await Network.addRemoteExercise(exercise: exerciseRemote)
        
        if (!worked) {
            self.networkFailed = true
            return
        }
        
        // Add local exercise
        let exercise = Exercise(context: self.element)
        exercise.exerciseName = exerciseRemote.exerciseName
        exercise.exerciseDescription = exerciseRemote.description
        try? self.element.save()
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func activeButton(name: String) {
        self.disabled = (self.name.isEmpty || self.checkExerciseExists(name: self.name)) ? true: false
    }
    
    func checkExerciseExists(name: String) -> Bool {
        for exercise in self.exercises {
            if (exercise.exerciseName == self.name) {
                self.error = "Cet exercice existe déjà"
                return true
            }
        }
        self.error = ""
        return false
    }
}

struct AddExerciseView_Previews: PreviewProvider {
    static var previews: some View {
        AddExerciseView()
    }
}

