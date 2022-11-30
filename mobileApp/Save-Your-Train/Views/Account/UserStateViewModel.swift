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
    
    func signIn(email: String, password: String) async -> Bool  {
        UserStateViewModel.email = email
        isLoggedIn = true
        return true
    }
    
    func signUp(firstName: String, lastName: String, email: String, password: String) async -> Bool {
        let account: AccountModel = AccountModel(email: email, password: password, firstName: firstName, lastName: lastName)
        let worked: Bool = await Network.signUp(account: account)
        
        if (!worked) {
            return false
        }
        
        UserStateViewModel.email = email
        UserStateViewModel.firstName = firstName
        UserStateViewModel.lastName = lastName
        isLoggedIn = true
        return true
    }
    
    func signOut() -> Bool {
        UserStateViewModel.email = ""
        isLoggedIn = false
        return true
    }
}
