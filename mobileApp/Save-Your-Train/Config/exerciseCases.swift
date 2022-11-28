import Foundation

class ExerciseCases: ObservableObject  {
    @Published var cases: [ExercisesCaseModel] = []
        
    init() {
        loadJson()
    }
    
    func loadJson()  {
        guard let url = Bundle.main.url(forResource: "exerciseCases.json", withExtension: nil)
            else {
                print("Json file not found")
                return
            }
        
        let data = try? Data(contentsOf: url)
        self.cases = try! JSONDecoder().decode([ExercisesCaseModel].self, from: data!)
    }
}
