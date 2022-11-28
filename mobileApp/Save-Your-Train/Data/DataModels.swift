import Foundation

// Base Models

struct ExerciseModel: Decodable, Encodable {
    let name: String
    let description: String
}

// Network Models

struct AddExerciseModel: Decodable {
    let exercises: [ExerciseModel]
}

// Config

struct ExercisesCaseModel: Codable {
    var exec: Bool = false
    var repet: Bool = false
    var rest: Bool = false
    var weight: Bool = false
    
    static func == (first: ExercisesCaseModel, second: ExercisesCaseModel) -> Bool {
        return first.exec == second.exec && first.repet == second.repet && first.rest == second.rest && first.weight == second.weight
    }
}
