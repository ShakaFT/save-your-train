import SwiftUI

struct DeleteExerciseView: View {
    
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @EnvironmentObject var network: Network
    
    @Binding var show: Bool
    
    @State private var disabled: Bool = false
    
    var name: String
    
    var body: some View {
        ZStack {
            if self.show {
                Color.black.opacity(0.3).edgesIgnoringSafeArea(.all)

                VStack(alignment: .center, spacing: 0) {
                    Text("Voulez vous vraiment supprimer cet exercice ?")
                        .bold()
                        .font(Font.system(size: 20))
                        .padding()
                    
                    HStack {
                        Components.button(name: "Annuler", action: {self.show = false})
                        Components.button(name: "Supprimer", color: .red, action: {
                            Task {
                                self.disabled = true
                                await removeExercise(name: self.name)
                                self.disabled = false
                            }
                        }).disabled(self.disabled)
                    }
                }
                .frame(maxWidth: 300)
                .border(Color.black, width: 1)
                .background(.white)
            }
        }
    }
    
    func removeExercise(name: String) async {
        let worked: Bool = await self.network.deleteRemoteExercise(exerciseName: name)
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
