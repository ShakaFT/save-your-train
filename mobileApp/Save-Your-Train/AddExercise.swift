//
//  AddExercise.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import SwiftUI

struct AddExercise: View {
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @State public var name: String = ""
    @State private var description: String = ""
    @State private var disabled: Bool = true
    @State private var error: String = ""
    
    var body: some View {
        NavigationView {
            VStack{
                VStack(alignment: .leading){
                    HStack {
                        Text("Nom")
                        TextField("Nom de l'exercice...", text: $name)
                            .textFieldStyle(.roundedBorder)
                            .onChange(of: name, perform: {activeButton(name: $0)})
                    }.padding()
                    
                    Text(error)
                        .foregroundColor(.red)
                        .padding(.horizontal)
                        .font(.system(size: 14).bold())
                    
                    HStack {
                        Text("Description")
                        TextField("Description de l'exercice...", text: $description)
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
                
                Button(action: addExercise) {
                    Text("Ajouter").padding()
                }
                .disabled(disabled)
                .cornerRadius(10)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .top)
                .padding()
            }
        }
    }
    
    func addExercise() {
        let exercise = Exercise(context: element)
        exercise.exerciseName = name
        exercise.exerciseDescription = description
        try? element.save()
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func activeButton(name: String) {
        if (name.isEmpty || checkExerciseExists(name: name)) {
            self.disabled = true
        } else {
            self.disabled = false
        }
    }
    
    func checkExerciseExists(name: String) -> Bool {
        for exercise in exercises {
            if (exercise.exerciseName == name) {
                self.error = "Cet exercice existe déjà"
                return true
            }
        }
        self.error = ""
        return false
    }
}

struct AddExercise_Previews: PreviewProvider {
    static var previews: some View {
        AddExercise()
    }
}

