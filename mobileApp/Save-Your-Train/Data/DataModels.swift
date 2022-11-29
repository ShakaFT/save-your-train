import Foundation

// Base Models

struct ExerciseModel: Decodable, Encodable {
    let name: String // id
    let description: String
}

struct HistoryModel: Decodable, Encodable {
    var dateMs: Double = 0.0 // id
    var exerciseName: String = "" // exerciseName
    var execution: String = ""
    var repetition: String = ""
    var rest: String = ""
    var series: String = ""
    var weight: String = ""
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
