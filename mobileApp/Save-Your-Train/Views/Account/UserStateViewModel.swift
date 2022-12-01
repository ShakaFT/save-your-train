import Foundation
import SwiftUI

@MainActor
class UserStateViewModel: ObservableObject {
    
    @AppStorage("isLoggedIn") var isLoggedIn = false
    
    @AppStorage("email") public static var email: String = ""
    @AppStorage("firstName") public static var firstName: String = "Test"
    @AppStorage("lastName") public static var lastName: String = "Hello"
    
    func signIn(network: Network, email: String, password: String) async -> Bool  {
        let account: AccountModel = AccountModel(email: email, password: password)
        let worked: Bool = await network.signIn(account: account)
        
        if (!worked) {
            return false
        }
        
        
        UserStateViewModel.email = network.userData.email
        UserStateViewModel.firstName = network.userData.firstName
        UserStateViewModel.lastName = network.userData.lastName
        self.isLoggedIn = true
        return true
    }
    
    func signUp(network: Network, firstName: String, lastName: String, email: String, password: String) async -> Bool {
        let account: AccountModel = AccountModel(email: email, password: password, firstName: firstName, lastName: lastName)
        let worked: Bool = await network.signUp(account: account)
        
        if (!worked) {
            return false
        }
        
        UserStateViewModel.email = email
        UserStateViewModel.firstName = firstName
        UserStateViewModel.lastName = lastName
        isLoggedIn = true
        return true
    }
    
    func signOut() {
        UserStateViewModel.email = ""
        UserStateViewModel.firstName = ""
        UserStateViewModel.lastName = ""
        isLoggedIn = false
    }
}
