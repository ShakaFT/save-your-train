//
//  ContentView.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 18/11/2022.
//

import SwiftUI

struct MainView: View {
    var body: some View {
        TabView {
            ExerciseView()
                .tabItem {
                    Label("Exercice", systemImage: "square.and.pencil")
                }
            HistoryView()
                .tabItem {
                    Label("Historique", systemImage: "chart.bar.xaxis")
                }
            ProfileView()
                .tabItem {
                    Label("Profil", systemImage: "person.crop.circle.fill")
                }
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
