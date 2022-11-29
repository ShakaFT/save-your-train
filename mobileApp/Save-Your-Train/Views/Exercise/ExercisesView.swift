import SwiftUI

struct ExercisesView: View {
    
    @FetchRequest(entity: Exercise.entity(),
                  sortDescriptors: [NSSortDescriptor(keyPath: \Exercise.exerciseName, ascending: true)]) var exercises: FetchedResults<Exercise>
    @Environment(\.managedObjectContext) var element
    
    @State public var exercisesArray: Array<Exercise> = []
    @State public var currentPage: Int = 1
    
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(self.getExercisesToDisplay()) { (exercise: Exercise) in
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
                        Button(action: self.previousPage) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }.disabled(self.currentPage == 1)
                        
                        Button(action: self.nextPage) {
                            Image(systemName: "arrow.right.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }.disabled(self.currentPage*10>=self.exercisesArray.count)
                        
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
            .onAppear(perform: self.setExercisesArray)
        }
    }
    
    func getExercisesToDisplay() -> ArraySlice<FetchedResults<Exercise>.Element> {
        return self.exercisesArray[(currentPage-1)*10..<self.getMaxPage()]
    }
    
    func nextPage() {
        self.currentPage += 1
    }
    
    func previousPage() {
        self.currentPage -= 1
    }
    
    func removeExercise(at offsets: IndexSet) {
        for offset in offsets {
            // print(self.exercises[offset].exerciseName)
            self.element.delete(self.exercises[offset])
        }
        try? self.element.save()
        self.setExercisesArray()
    }
    
    public func setExercisesArray() {
        self.exercisesArray = Array(self.exercises)
    }
    
    private func getMaxPage() -> Int {
        let result: Int = currentPage * Constants.nbItems
        if (result > self.exercisesArray.count) {
            return self.exercisesArray.count
        }
        return result
    }
}

struct ExercisesView_Previews: PreviewProvider {
    static var previews: some View {
        ExercisesView()
    }
}
