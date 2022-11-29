import Foundation

// Base Models

struct ExerciseModel: Decodable, Encodable {
    let name: String // id
    let description: String
}

struct HistoryModel: Decodable, Encodable {
    let dateMs: Double // id
    let exerciseName: String // exerciseName
    let execution: String
    let repetition: String
    let rest: String
    let series: String
    let weight: String
}

// Network Models



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
