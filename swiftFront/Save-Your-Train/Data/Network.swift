import Foundation
import CoreData

@MainActor
class Network: ObservableObject {
    
    @Published var exercises: [ExerciseModel] = []
    @Published var histories: [HistoryModel] = []
    @Published var userData: AccountModel = AccountModel()
    
    public func addRemoteExercise(exercise: ExerciseModel) async -> Bool {
        let payload: [String: Any] = [
            "email": UserStateViewModel.email,
            "exercise": [
                "exerciseName": exercise.exerciseName,
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
    
    public func deleteRemoteExercise(exerciseName: String) async -> Bool {
        let payload: [String: Any] = [
            "email": UserStateViewModel.email,
            "exerciseName": exerciseName
        ]
        
        do {
            _ = try await Network.callAPI(endpoint: "/exercise/delete", method: "POST", payload: payload)
        } catch {
            return false
        }
        
        return true
    }
    
    public func addRemoteHistory(history: HistoryModel) async -> Bool {
        let payload: [String: Any] = [
            "email": UserStateViewModel.email,
            "exercise": [
                "dateMs": history.dateMs,
                "exerciseName": history.exerciseName,
                "execution": history.execution,
                "repetition": history.repetition,
                "rest": history.rest,
                "series": history.series,
                "weight": history.weight
            ]
        ]
        
        do {
            _ = try await Network.callAPI(endpoint: "/history/add", method: "POST", payload: payload)
        } catch {
            return false
        }
        
        return true
    }
    
    public func deleteRemoteHistory(timestamp: Double) async -> Bool {
        let payload: [String: Any] = [
            "email": UserStateViewModel.email,
            "dateMs": timestamp
        ]
        
        do {
            _ = try await Network.callAPI(endpoint: "/history/delete", method: "POST", payload: payload)
        } catch {
            return false
        }
        
        return true
    }
    
    public func signIn(account: AccountModel) async -> Bool {
        let payload: [String: Any] = [
            "email": account.email,
            "password": account.password
        ]
        
        do {
            let data: Data = try await Network.callAPI(endpoint: "/account/sign_in", method: "POST", payload: payload)
            let signInData: SignInModel = try JSONDecoder().decode(SignInModel.self, from: data)
            
            if (!signInData.userSignIn) { return false } // email or password incorrect
            
            self.userData = signInData.userData
            self.exercises = signInData.exercises
            self.histories = signInData.history
            
        } catch {
            return false
        }
        
        return true
    }
    
    public func signUp(account: AccountModel) async -> Bool {
        let payload: [String: Any] = [
            "email": account.email,
            "password": account.password,
            "firstName": account.firstName,
            "lastName": account.lastName
        ]
        
        do {
            _ = try await Network.callAPI(endpoint: "/account/add", method: "POST", payload: payload)
        } catch {
            return false
        }
        
        return true
    }
    
    
    private static func callAPI(endpoint: String, method: String = "GET", payload: [String: Any] = [:]) async throws -> Data {
        // URL
        let url = URL(string: "\(Constants.urlRestApi)\(endpoint)")
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
