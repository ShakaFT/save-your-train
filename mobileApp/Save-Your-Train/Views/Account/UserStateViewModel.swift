import Foundation
import SwiftUI

enum UserStateError: Error{
    case signInError, signOutError
}

@MainActor
class UserStateViewModel: ObservableObject {
    
    @AppStorage("isLoggedIn") var isLoggedIn = false
    
    @AppStorage("email") public static var email: String = ""
    @AppStorage("firstName") public static var firstName: String = "Test"
    @AppStorage("lastName") public static var lastName: String = "Hello"
    
    func signIn(email: String, password: String) async -> Result<Bool, UserStateError>  {
        UserStateViewModel.email = email
        isLoggedIn = true
        return .success(true)
    }
    
    func signUp(firstName: String, lastName: String, email: String, password: String) async -> Result<Bool, UserStateError>  {
        UserStateViewModel.email = email
        UserStateViewModel.firstName = firstName
        UserStateViewModel.lastName = lastName
        isLoggedIn = true
        return .success(true)
    }
    
    func signOut() -> Result<Bool, UserStateError>  {
        UserStateViewModel.email = ""
        isLoggedIn = false
        return .success(true)
    }
}
