//
//  DataController.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import Foundation
import CoreData

class DataController: ObservableObject {
    let container = NSPersistentContainer(name: "Data")
    
    init(){
        self.container.loadPersistentStores {
            description, error in
            if let error = error {
                print("Error when loading data : \(error)")
            }
        }
    }
}
