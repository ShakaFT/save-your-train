import Foundation

let constants: Constants = Constants()


func addRemoteExercise(exercise: ExerciseModel) {
    let url = URL(string: "\(constants.urlRestApi)/exercise/add")
    guard let requestUrl = url else { return }
    
    let payload: [String: Any] = [
        "email": constants.email,
        "exercise": [
            "name": exercise.name,
            "description": exercise.description
        ]
    ]
    
    var request = URLRequest(url: requestUrl)
    request.httpMethod = "POST"
    request.httpBody = try? JSONSerialization.data(withJSONObject: payload)
    
    let task = URLSession.shared.dataTask(with: request) { (data, response, _) in
        if let response = response as? HTTPURLResponse {
            if (response.statusCode != 200) {
                print("An error as occured with status \(response.statusCode)")
                return
            }
        }
        DispatchQueue.main.async {
            do {
                let _ = try JSONDecoder().decode(AddExerciseModel.self, from: data!)
            } catch let error {
                print("An error as occured during recovery of values from restApi : \(error)")
            }
        }
    }
    task.resume()
}
