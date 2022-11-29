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
                    HStack {
                        Text("Nom")
                        TextField("Nom de l'exercice...", text: self.$name)
                            .textFieldStyle(.roundedBorder)
                            .onChange(of: self.name, perform: { self.activeButton(name: $0) })
                    }.padding()
                    
                    Text(self.error)
                        .foregroundColor(.red)
                        .padding(.horizontal)
                        .font(.system(size: 14).bold())
                    
                    HStack {
                        Text("Description")
                        TextField("Description de l'exercice...", text: self.$description)
                            .textFieldStyle(.roundedBorder)
                    }.padding()
                    
                    HStack(alignment: .firstTextBaseline){
                        Text("Image")
                        Text("...")
                    }.padding()
                }
                .cornerRadius(20)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .padding()
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
                
                if (self.networkFailed) {
                    Text("Une erreur est survenue, veuillez réessayer plus tard...")
                        .bold()
                        .foregroundColor(.red)
                        .multilineTextAlignment(.center)
                }
                
                Button(action: {
                    Task {
                        await self.addExercise()
                    }
                }) {
                    Text("Ajouter").padding()
                }
                .disabled(self.disabled)
                .cornerRadius(10)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .top)
                .padding()
            }
        }
    }
    
    func addExercise() async {
        let exerciseRemote: ExerciseModel = ExerciseModel(name: self.name, description: self.description)
        
        self.disabled = true
        let worked: Bool = try await Network.addRemoteExercise(exercise: exerciseRemote)
        
        if (!worked) {
            self.networkFailed = true
            self.disabled = false
            return
        }
        
        // Add local exercise
        let exercise = Exercise(context: self.element)
        exercise.exerciseName = exerciseRemote.name
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

