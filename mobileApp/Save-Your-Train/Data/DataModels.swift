import Foundation

// Base Models

struct AccountModel: Decodable, Encodable {
    var email: String = ""
    var password: String = ""
    var firstName: String = ""
    var lastName: String = ""
}

struct ExerciseModel: Decodable, Encodable {
    let exerciseName: String // id
    let description: String
}

struct HistoryModel: Decodable, Encodable {
    var dateMs: Double = 0.0 // id
    var exerciseName: String = ""
    var execution: String = ""
    var repetition: String = ""
    var rest: String = ""
    var series: String = ""
    var weight: String = ""
}

// Network Models

struct SignInModel: Codable {
    let userSignIn: Bool
    let exercises: [ExerciseModel]
    let history: [HistoryModel]
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
