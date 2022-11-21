//
//  Save_Your_TrainApp.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 18/11/2022.
//

import SwiftUI

@main
struct Save_Your_TrainApp: App {
    @StateObject private var dataController = DataController()
    
    var body: some Scene {
        WindowGroup {
            MainView().environment(\.managedObjectContext, dataController.container.viewContext)
        }
    }
}
