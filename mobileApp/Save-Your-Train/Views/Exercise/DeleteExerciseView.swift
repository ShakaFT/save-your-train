import SwiftUI

struct DeleteExerciseView: View {
    
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @Binding var show: Bool
    
    @State private var disabled: Bool = false
    
    var name: String
    
    var body: some View {
        ZStack {
            if show {
                Color.black.opacity(show ? 0.3 : 0).edgesIgnoringSafeArea(.all)

                VStack(alignment: .center, spacing: 0) {
                    Text("Voulez vous vraiment supprimer cet exercice ?")
                        .bold()
                        .font(Font.system(size: 20))
                        .padding()
                    HStack {
                        Button(action: {show = false}) {
                            Text("Annuler").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                        .padding()
                           
                        Button(action: {
                            Task {
                                self.disabled = true
                                await removeExercise(name: self.name)
                                self.disabled = false
                            }
                        }) {
                            Text("Supprimer").padding()
                        }
                        .cornerRadius(10)
                        .disabled(self.disabled)
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
    
    func removeExercise(name: String) async {
        let worked: Bool = await Network.deleteRemoteExercise(exerciseName: name)
        if (!worked) {
            return
        }
        
        for exercise in exercises {
            if (exercise.exerciseName == name) {
                self.element.delete(exercise)
                self.presentationMode.wrappedValue.dismiss()
            }
        }
        try? self.element.save()
    }
}
