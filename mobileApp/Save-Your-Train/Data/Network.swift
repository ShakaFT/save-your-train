import Foundation

let constants: Constants = Constants()

class Network {
    
    public static func addRemoteExercise(exercise: ExerciseModel) async -> Bool {
        
        let payload: [String: Any] = [
            "email": constants.email,
            "exercise": [
                "name": exercise.name,
                "description": exercise.description
            ]
        ]
        
        do {
            _ = try await Network.callAPI(endpoint: "/exercise/add", method: "POST", payload: payload)
        } catch {
            return false
        }
        
        return true
    }
    
    private static func callAPI(endpoint: String, method: String = "GET", payload: [String: Any] = [:]) async throws -> Data {
        // URL
        let url = URL(string: "\(constants.urlRestApi)\(endpoint)")
        guard let urlRequest = url else { throw NSError(domain: "", code: 0) }
        
        // Request
        var request = URLRequest(url: urlRequest)
        request.httpMethod = method
        if (method != "GET" && method != "DELETE") {
            request.httpBody = try? JSONSerialization.data(withJSONObject: payload)
        }
        
        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            
            // Check status code
            if ((response as? HTTPURLResponse)?.statusCode != 200) {
                throw NSError(domain: "", code: 0)
            }
            return data

        } catch {
            throw NSError(domain: "", code: 0)
        }
    }
}
