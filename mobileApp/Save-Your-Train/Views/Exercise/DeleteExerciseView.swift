import SwiftUI

struct DeleteExerciseView: View {
    
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    var name: String
    
    @Binding var show: Bool
    
    var body: some View {
        ZStack {
            if show {
                Color.black.opacity(show ? 0.3 : 0).edgesIgnoringSafeArea(.all)

                VStack(alignment: .center, spacing: 0) {
                    Text("Voulez vous vraiment supprimer cet exercice ?")
                        .font(Font.system(size: 20))
                        .padding()
                    HStack {
                        Button(action: {show = false}) {
                            Text("Annuler").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                        .padding()
                           
                        Button(action: {removeExercise(name: self.name)}) {
                            Text("Supprimer").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.red, lineWidth: 1))
                        .foregroundColor(.red)
                        .padding()
                    }
                }
                .frame(maxWidth: 300)
                .border(Color.black, width: 1)
                .background(.white)
            }
        }
    }
    
    func removeExercise(name: String) {
        for exercise in exercises {
            if (exercise.exerciseName == name) {
                self.element.delete(exercise)
                self.presentationMode.wrappedValue.dismiss()
            }
        }
        try? self.element.save()
    }
}
