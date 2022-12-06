import SwiftUI

struct LaunchedExercisePopup: View {
    @Environment(\.managedObjectContext) var element
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    @EnvironmentObject var network: Network
    
    @ObservedObject var displayRest : DisplayRest = DisplayRest()
    
    @Binding var show: Bool
    
    @State var disabled: Bool = false
    @State var nbSeries: Int = 1
    
    var rest : String
    var execution : String
    var repetition : String
    var weight : String
    var series : String
    let name: String
    
    
    var body: some View {
            ZStack {
                if self.show {
                    Color.black.opacity(0.3).edgesIgnoringSafeArea(.all)
                    
                    VStack(spacing: 0) {
                        HStack {
                            Components.buttonImage(image: "arrow.backward", action: {self.show.toggle()})
                                .foregroundColor(.black)
                            Spacer()
                        }
                        
                        Spacer()
                        
                        VStack (spacing: UIScreen.main.bounds.height * 0.05){
                            if (!self.repetition.isEmpty && !self.displayRest.toggled) {
                                HStack {
                                    Text(self.repetition)
                                        .bold()
                                        .font(.system(size: 30))
                                    Text(Int(self.repetition) == 1 ? "répétition" : "répétitions").font(.system(size: 25))
                                }
                            }
                            
                            if (!self.rest.isEmpty && self.displayRest.toggled) {
                                Text("Temps de repos").font(.system(size: 25))
                                TimerView(time: Double(self.rest)!)
                            }
                            
                            if (!self.execution.isEmpty && !self.displayRest.toggled) {
                                Text("Temps d'execution").font(.system(size: 25))
                                TimerView(time: Double(self.execution)!)
                            }
                            
                            if (!self.weight.isEmpty && !self.displayRest.toggled) {
                                HStack {
                                    Text(self.weight)
                                        .bold()
                                        .font(.system(size: 30))
                                    Text("kg").font(.system(size: 25))                            }
                            }
                            
                            HStack {
                                Text(String(self.nbSeries))
                                    .bold()
                                    .font(.system(size: 30))
                                Text(self.nbSeries == 1 ? "série restante" : "séries restantes").font(.system(size: 25))
                            }
                        }
                        Spacer()
                        
                        VStack {
                            Components.button(name: self.getButtonName(), action: {
                                if (self.nbSeries > 1) {
                                    self.nextSeries()
                                    return
                                }
                                Task {
                                    self.disabled = true
                                    await self.stopExercise()
                                    self.disabled = false
                                }
                            }).disabled(self.disabled)
                        }
                        Spacer()
                    }
                    .frame(maxWidth: UIScreen.main.bounds.width * 0.80, maxHeight: UIScreen.main.bounds.height * 0.80)
                    .border(Color.black, width: 1)
                    .background(.white)
                }
            }
            .onAppear(perform: {self.nbSeries = Int(self.series) ?? 1})
            .navigationTitle(self.name)
            .navigationBarTitleDisplayMode(.inline)
    }
    
    public func stopExercise() async {
        let historyRemote: HistoryModel = HistoryModel(
            dateMs: NSDate().timeIntervalSince1970,
            exerciseName: self.name,
            execution: self.execution,
            repetition: self.repetition,
            rest: self.rest,
            series: self.series,
            weight: self.weight
        )
        let worked: Bool = await self.network.addRemoteHistory(history: historyRemote)
        
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
    
    public func nextSeries() {
        if (!rest.isEmpty && !displayRest.toggled) {
            self.displayRest.toggled = true
        } else {
            self.displayRest.toggled = false
            self.nbSeries -= 1
        }
    }
    
    public func getButtonName() -> String {
        if(!self.displayRest.toggled && !self.rest.isEmpty && self.nbSeries != 1 ) {
            return "Repos"
        } else if (!displayRest.toggled && self.nbSeries == 1) {
            return "Terminer l'exercice"
        }
        return "Série suivante"
    }
}

class DisplayRest: ObservableObject {
    @Published var toggled : Bool = false
}

