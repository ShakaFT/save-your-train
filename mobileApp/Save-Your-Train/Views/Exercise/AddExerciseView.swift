import SwiftUI

struct AddExerciseView: View {
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @EnvironmentObject var network: Network
    
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
                        Components.label(text: "Nom")
                        Components.textField(text: self.$name, placeholder: "Nom de l'exercice...")
                            .onChange(of: self.name, perform: { self.activeButton(name: $0) })
                    }.padding()
                    
                    Components.error(text: self.error).foregroundColor(.red)
                        .padding(.horizontal)
                    
                    VStack {
                        Components.label(text: "Description")
                        Components.textField(text: self.$description, placeholder: "Description de l'exercice...")
                    }.padding()
                    
                    if (self.description.count > 100) {
                        Components.error(text: "100 caractères maximum")
                            .padding(.horizontal)
                    }
                }
                .cornerRadius(20)
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
                .padding()
                
                if (self.networkFailed) {
                    Components.error(text: "Une erreur est survenue, veuillez réessayer plus tard...")
                        .multilineTextAlignment(.center)
                        .padding(.leading)
                }
                
                Components.button(name: "Ajouter", action:  {
                    Task {
                        self.disabled = true
                        await self.addExercise()
                        self.disabled = false
                    }
                })
                .disabled(self.disabled || self.description.count > 100)
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .top)
            }
        }
    }
    
    func addExercise() async {
        let exerciseRemote: ExerciseModel = ExerciseModel(exerciseName: self.name, description: self.description)
        
        let worked: Bool = await self.network.addRemoteExercise(exercise: exerciseRemote)
        
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
