import Foundation

class ExerciseCases: ObservableObject  {
    
    @Published var cases: [ExercisesCaseModel] = []
        
    init() {
        loadJson()
    }
    
    func loadJson()  {
        guard let url = Bundle.main.url(forResource: "exerciseCases", withExtension: "json")
            else {
                // Json file not found
                return
            }
        
        let data = try? Data(contentsOf: url)
        self.cases = try! JSONDecoder().decode([ExercisesCaseModel].self, from: data!)
    }
}
