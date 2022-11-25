import Foundation
import CoreData

class DataController: ObservableObject {
    let container = NSPersistentContainer(name: "Data")
    
    init(){
        self.container.loadPersistentStores {
            _, error in
            if let error = error {
                print("Error when loading data : \(error)")
            }
        }
    }
}
