import SwiftUI

struct ExercisesView: View {
    
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    @Environment(\.managedObjectContext) var element
    
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(self.exercises) { (exercise: Exercise) in
                            NavigationLink(destination: ActiveExerciseView(name: exercise.exerciseName!, description: exercise.exerciseDescription!)){
                                HStack {
                                    Text(exercise.exerciseName!)
                                    Text(exercise.exerciseDescription ?? "Aucune description").foregroundColor(.gray)
                                }
                            }
                        }.onDelete(perform: self.removeExercise)
                    }
                VStack{
                    Spacer()
                    HStack {
                        Button(action: self.getPreviousExercises) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Button(action: self.getNextExercises ) {
                            Image(systemName: "arrow.right.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Spacer()
                        NavigationLink(destination: AddExerciseView()) {
                            Image(systemName: "plus.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("Exercices")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func getPreviousExercises() {
        
    }
    
    func getNextExercises() {
        
    }
    
    func removeExercise(at offsets: IndexSet) {
        for offset in offsets {
            self.element.delete(self.exercises[offset])
        }
        try? self.element.save()
    }
}

struct ExercisesView_Previews: PreviewProvider {
    static var previews: some View {
        ExercisesView()
    }
}
