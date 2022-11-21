//
//  ExerciseView.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import SwiftUI

struct ExerciseView: View {
    var body: some View {
        
        NavigationView {
            ZStack {
                    List {
                        Text("A")
                        Text("B")
                        Text("C")
                        Text("A")
                        Text("B")
                        Text("C")
                        Text("A")
                        Text("B")
                        Text("C")
                        Text("A")
                    }
                VStack{
                    Spacer()
                    HStack {
                        Button(action: getPreviousExercises ) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Button(action: getNextExercises ) {
                            Image(systemName: "arrow.right.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Spacer()
                        Button(action: addExercise ) {
                            Image(systemName: "plus.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                    }
                    .padding()
                }
            }
        }
    }
    
    func addExercise() {
        
    }
    
    func getPreviousExercises() {
        
    }
    
    func getNextExercises() {
        
    }
}

struct ExerciseView_Previews: PreviewProvider {
    static var previews: some View {
        ExerciseView()
    }
}
