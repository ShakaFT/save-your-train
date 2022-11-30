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
                VStack(spacing: 0) {
                    Spacer()
                    VStack (spacing: UIScreen.main.bounds.height * 0.05){
                        if(!repetition.isEmpty && !displayRest.toggled) {
                            HStack {
                                Text(self.repetition).font(.system(size: 30)).bold()
                                Text(Int(self.repetition) == 1 ? "répétition" : "répétitions").font(.system(size: 25))
                            }
                        }
                        
                        if(!rest.isEmpty && displayRest.toggled) {
                            Text("Temps de repos").font(.system(size: 25))
                            TimerView(time: Double(self.rest)!)
                        }
                        
                        if(!execution.isEmpty && !displayRest.toggled) {
                            Text("Temps d'execution").font(.system(size: 25))
                            TimerView(time: Double(self.execution)!)
                        }
                        
                        if(!weight.isEmpty && !displayRest.toggled) {
                            HStack {
                                Text(self.weight).font(.system(size: 30)).bold()
                                Text("kg").font(.system(size: 25))                            }
                        }
                        
                        HStack {
                            Text(String(self.nbSeries)).font(.system(size: 30)).bold()
                            Text(self.nbSeries == 1 ? "série restante" : "séries restantes").font(.system(size: 25))
                        }
                        
                    }
                    Spacer()
                    VStack {
                        Button(action: {
                            if self.nbSeries > 1 {
                                self.nextSeries()
                            } else {
                                Task {
                                    self.disabled = true
                                    await self.stopExercise()
                                    self.disabled = false
                                }
                            }
                        }) {
                            Text(self.getButtonName()).padding()
                        }
                        .cornerRadius(10)
                        .disabled(self.disabled)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                        .foregroundColor(.blue)
                        .padding()
                    }
                    Spacer()
                }
            }
            .onAppear(perform: {self.nbSeries = Int(self.series) ?? 1})
            .navigationTitle(self.name)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func stopExercise() async {
        let historyRemote: HistoryModel = HistoryModel(dateMs: NSDate().timeIntervalSince1970, exerciseName: self.name, execution: self.execution, repetition: self.repetition, rest: self.rest, series: self.series, weight: self.weight)
        let worked: Bool = try await Network.addRemoteHistory(history: historyRemote)
        if (!worked) {
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
