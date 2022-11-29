import SwiftUI

struct LaunchedExerciseView: View {
    @Environment(\.managedObjectContext) var element
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    @State var disabled: Bool = false
    @State var nbSeries: Int = 1
    
    var rest : String
    var execution : String
    var repetition : String
    var weight : String
    var series : String
    let name: String
    
    @ObservedObject var displayRest : DisplayRest = DisplayRest()
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: UIScreen.main.bounds.height * 0.25) {
                    VStack {
                        if(!repetition.isEmpty && !displayRest.toggled) {
                            Text("Nombre de répétitions").font(.system(size: 30))
                            Text(self.repetition).font(.system(size: 50))
                        }
                        
                        if(!weight.isEmpty && !displayRest.toggled) {
                            Text("Poids").font(.system(size: 30))
                            Text(self.weight).font(.system(size: 50))
                        }
                        
                        if(!rest.isEmpty && displayRest.toggled) {
                            Text("Temps de repos").font(.system(size: 30))
                            TimerView(time: Double(self.rest)!)
                        }
                        
                        if(!execution.isEmpty && !displayRest.toggled) {
                            Text("Temps d'execution").font(.system(size: 30))
                            TimerView(time: Double(self.execution)!)
                        }
                        
                        Text(self.nbSeries == 1 ? "Série restante :" : "Séries restantes :").font(.system(size: 30))
                        Text(String(self.nbSeries)).font(.system(size: 40))
                    }
                    
                    VStack {
                        Button(action: {
                            if self.nbSeries > 1 {
                                self.nextSeries()
                            } else {
                                Task {
                                    await self.stopExercise()
                                }
                            }
                        }) {
                            Text(self.getButtonName()).padding()
                        }
                        .cornerRadius(10)
                        .disabled(self.disabled)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.green, lineWidth: 1))
                        .foregroundColor(.green)
                        .padding()
                    }
                }
            }
            .onAppear(perform: {self.nbSeries = Int(self.series) ?? 1})
            .navigationTitle(self.name)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func stopExercise() async {
        let historyRemote: HistoryModel = HistoryModel(dateMs: NSDate().timeIntervalSince1970, exerciseName: self.name, execution: self.execution, repetition: self.repetition, rest: self.rest, series: self.series, weight: self.weight)
        self.disabled = true
        let worked: Bool = try await Network.addRemoteHistory(history: historyRemote)
        if (!worked) {
            self.disabled = false
            return
        }
        
        // Add local history
        let history = History(context: self.element)
        history.exerciseName = historyRemote.exerciseName
        history.series = historyRemote.series
        history.repetition = historyRemote.repetition
        history.weight = historyRemote.weight
        history.rest = historyRemote.rest
        history.execution = historyRemote.execution
        history.dateMs = historyRemote.dateMs
        try? self.element.save()
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func nextSeries() {
        if (!rest.isEmpty && !displayRest.toggled) {
            displayRest.toggled = true
        } else {
            displayRest.toggled = false
            self.nbSeries -= 1
        }
    }
    
    func getButtonName() -> String {
    
        if(!displayRest.toggled && !rest.isEmpty && self.nbSeries != 1 ) {
            return "Repos"
        } else if (!displayRest.toggled && self.nbSeries == 1) {
            return "Terminer l'exercice"
        } else {
            return "Série suivante"
        }
    }
}

class DisplayRest: ObservableObject {
    
    @Published var toggled : Bool = false
    
}


struct LaunchedExerciseView_Previews: PreviewProvider {
    static var previews: some View {
        LaunchedExerciseView(rest: "", execution: "", repetition: "", weight: "", series: "", name: "")
    }
}
