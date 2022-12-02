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
                        }
                    }
                VStack{
                    Spacer()
                    HStack {
                        Components.buttonImage(image: "arrow.backward.circle", action: self.previousPage)
                            .disabled(self.currentPage == 1)
                        
                        Components.buttonImage(image: "arrow.right.circle", action: self.nextPage)
                            .disabled(self.currentPage*10>=self.exercisesArray.count)
                        
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
            .onAppear(perform: {
                self.exercisesArray = Array(self.exercises)
            })
        }
    }
    
    public func getExercisesToDisplay() -> ArraySlice<FetchedResults<Exercise>.Element> {
        return self.exercisesArray[(currentPage-1)*10..<self.getMaxPage()]
    }
    
    public func nextPage() {
        self.currentPage += 1
    }
    
    public func previousPage() {
        self.currentPage -= 1
    }
    
    private func getMaxPage() -> Int {
        let result: Int = currentPage * Constants.nbItems
        return result > self.exercisesArray.count ? self.exercisesArray.count : result
    }
}

struct ExercisesView_Previews: PreviewProvider {
    static var previews: some View {
        ExercisesView()
    }
}
