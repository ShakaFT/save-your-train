import SwiftUI

struct ActiveExerciseView: View {
    
    @FetchRequest(
        entity: History.entity(),
        sortDescriptors: [NSSortDescriptor(keyPath: \History.dateMs, ascending: false)])
    var histories: FetchedResults<History>
    
    @ObservedObject public var exerciseCases = ExerciseCases()
    
    @State private var execution: String = ""
    @State private var repetition: String = ""
    @State private var rest: String = ""
    @State private var series: String = ""
    @State private var weight: String = ""
    
    @State private var deleteExercise: Bool = false
    @State private var disabled: Bool = true
    @State private var sheetAppear: Bool = false
    
    let name: String
    let description: String
    
    
    var body: some View {

        ZStack() {
            VStack(spacing: 0) {
                VStack() {
                    HStack{
                        Text("Description : ")
                        Text((self.description.isEmpty ? "Aucune description" : description))
                    }.padding()
                }
                .overlay(RoundedRectangle(cornerRadius: 10).stroke(.black, lineWidth: 1))
                .padding()
                
                VStack(alignment: .leading, spacing: 0) {
                    HStack {
                        Text("Execution")
                        Components.textField(text: self.$execution, placeholder:"60 sec (facultatif)")
                            .keyboardType(.numberPad)
                            .onChange(of: self.execution, perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Repos")
                        Components.textField(text: self.$rest, placeholder:"60 sec (facultatif)")
                            .keyboardType(.numberPad)
                            .onChange(of: self.rest, perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Répétitions")
                        Components.textField(text: self.$repetition, placeholder:"10 (facultatif)")
                            .keyboardType(.numberPad)
                            .onChange(of: self.repetition, perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Poids")
                        Components.textField(text: self.$weight, placeholder:"20 kg (facultatif)")
                            .keyboardType(.numberPad)
                            .onChange(of: self.weight , perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Séries")
                        Components.textField(text: self.$series, placeholder:"4 (facultatif)")
                            .keyboardType(.numberPad)
                            .onChange(of: self.series , perform: {self.activeButton(arg: $0)})
                    }.padding()
                     
                }.padding()
                HStack {
                    Spacer()
                    Spacer()
                    Spacer()
                    
                    Components.button(name: "Lancer l'exercice", action: {self.sheetAppear.toggle()})
                        .disabled(self.disabled)
                        .sheet(isPresented: self.$sheetAppear) {
                                LaunchedExerciseView(rest: self.rest, execution: self.execution, repetition: self.repetition, weight: self.weight, series: self.series, name: self.name)
                        }
                    
                    Spacer()
                    
                    Components.buttonImage(image: "trash", action: {self.deleteExercise.toggle()})
                        .foregroundColor(.red)
                }
            }
            DeleteExerciseView(show: self.$deleteExercise, name: self.name)
        }
        .navigationTitle(self.name)
        .onAppear(perform: self.initHistory)
        
    }
    
    public func activeButton(arg: String) {
        let currentCase: ExercisesCaseModel = ExercisesCaseModel(
            exec: !self.execution.isEmpty,
            repet: !self.repetition.isEmpty,
            rest: !self.rest.isEmpty,
            weight: !self.weight.isEmpty
        )
        
        self.disabled = !self.exerciseCases.cases.contains(where: {$0 == currentCase})
    }
    
    public func initHistory() {
        // retrieve last inputs
        for history in self.histories {
            if (history.exerciseName == self.name) {
                self.execution = (history.execution)!
                self.repetition = (history.repetition)!
                self.rest = (history.rest)!
                self.weight = (history.weight)!
                self.series = (history.series)!
                break
            }
        }
    }
}

struct ActiveExerciseView_Previews: PreviewProvider {
    static var previews: some View {
        ActiveExerciseView(name: "", description: "")
    }
}
