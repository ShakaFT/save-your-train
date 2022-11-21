//
//  ExercisesView.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import SwiftUI

struct ExercisesView: View {
    @FetchRequest(sortDescriptors: []) var exercises: FetchedResults<Exercise>
    
    @Environment(\.managedObjectContext) var element
    
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(exercises) { Exercise in
                            HStack {
                                Text(Exercise.exerciseName ?? "")
                                Text(Exercise.exerciseDescription ?? "").foregroundColor(.gray)
                            }
                        }.onDelete(perform: removeExercise)
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
                        NavigationLink(destination: AddExercise()) {
                            Image(systemName: "plus.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("Exercices")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func getPreviousExercises() {
        
    }
    
    func getNextExercises() {
        
    }
    
    func removeExercise(at offsets: IndexSet) {
     for offset in offsets {
        let exercise = exercises[offset]
            element.delete(exercise)
        }
     try? element.save()
    }
}

struct ExercisesView_Previews: PreviewProvider {
    static var previews: some View {
        ExercisesView()
    }
}
