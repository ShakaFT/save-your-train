//
//  Repet.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 24/11/2022.
//

import SwiftUI


struct Repet: View {
    
    @Environment(\.managedObjectContext) var element
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    let repetition: String
    let series: String
    let name: String
    @State var nbSeries: Int = 1
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: UIScreen.main.bounds.height * 0.25) {
                    VStack {
                        Text("Nombre de répétition").font(.system(size: 30))
                        Text(repetition).font(.system(size: 70))
                        Text(nbSeries == 1 ? "Série restante :" : "Séries restantes :").font(.system(size: 30))
                        Text(String(nbSeries)).font(.system(size: 40))
                    }
                    VStack {
                        Button(action: {(self.nbSeries == 1 ? self.stopExercise() : self.nextSeries())}) {
                            Text(nbSeries == 1 ? "Terminer l'exercice" : "Série suivante").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.green, lineWidth: 1))
                        .foregroundColor(.green)
                        .padding()
                    }
                }
                
            }
            .onAppear(perform: {nbSeries = Int(series) ?? 1})
            .navigationTitle(name)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func stopExercise() {
        let history = History(context: element)
        //history.name = name
        //history.series = series.isEmpty ? "1" : series
        //history.repetition = repetition
        //history.dateMs = NSDate().timeIntervalSince1970
        try? element.save()
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func nextSeries() {
        nbSeries -= 1
    }
}

struct Repet_Previews: PreviewProvider {
    static var previews: some View {
        Repet(repetition: "", series: "", name: "")
    }
}
