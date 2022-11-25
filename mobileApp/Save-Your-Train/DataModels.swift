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
