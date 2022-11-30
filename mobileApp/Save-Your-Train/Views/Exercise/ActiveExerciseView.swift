import SwiftUI

struct ActiveExerciseView: View {
    
    let name: String
    let description: String
    
    @FetchRequest(
        entity: History.entity(),
        sortDescriptors: [NSSortDescriptor(keyPath: \History.dateMs, ascending: false)])
    
    var histories: FetchedResults<History>
    
    @ObservedObject public var exerciseCases = ExerciseCases()
    
    @State private var execution: String = ""
    @State private var rest: String = ""
    @State private var repetition: String = ""
    @State private var weight: String = ""
    @State private var series: String = ""
    
    @State private var disabled: Bool = true
    @State private var sheetAppear: Bool = false
    @State private var deleteExercise: Bool = false
    
    var body: some View {

        ZStack() {
            VStack(spacing: 0) {
                VStack() {
                    HStack{
                        Text("Description : ")
                        Text((self.description.isEmpty ? "Aucune description" : description))
                    }.padding()
                }
                .cornerRadius(20)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .padding()
                
                VStack(alignment: .leading, spacing: 0) {
                    
                    HStack {
                        Text("Execution")
                        TextField("60 sec (facultatif)", text: self.$execution)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: self.execution , perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Repos")
                        TextField("60 sec (facultatif)", text: self.$rest)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: self.rest , perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Répétitions")
                        TextField("10 (facultatif)", text: self.$repetition)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: self.repetition , perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Poids")
                        TextField("20 kg (facultatif)", text: self.$weight)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: self.weight , perform: {self.activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Séries")
                        TextField("4 (facultatif)", text: self.$series)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: self.series , perform: {self.activeButton(arg: $0)})
                    }.padding()
                     
                }.padding()
                HStack {
                    Spacer()
                    Spacer()
                    Spacer()
                    Button(action: {self.sheetAppear.toggle()}) {
                        Text("Lancer l'exercice").padding()
                    }
                    .disabled(self.disabled)
                    .cornerRadius(10)
                    .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                    .padding()
                    .sheet(isPresented: self.$sheetAppear) {
                            LaunchedExerciseView(rest: self.rest, execution: self.execution, repetition: self.repetition, weight: self.weight, series: self.series, name: self.name)
                    }
                    Spacer()
                    Button(action: {self.deleteExercise.toggle()}){
                        Image(systemName: "trash")
                            .font(.largeTitle)
                            .frame(width: 70, height: 70)
                            .foregroundColor(.red)
                    }
                }
            }
            DeleteExerciseView(name: self.name, show: $deleteExercise)
        }
        .navigationTitle(self.name)
        .onAppear(perform: initHistory)
        
    }
    
    func activeButton(arg: String) {
        let currentCase: ExercisesCaseModel = ExercisesCaseModel(
            exec: !self.execution.isEmpty,
            repet: !self.repetition.isEmpty,
            rest: !self.rest.isEmpty,
            weight: !self.weight.isEmpty
        )
        
        self.disabled = !exerciseCases.cases.contains(where: {$0 == currentCase})
    }
    
    func initHistory() {
        for history in self.histories {
            if (history.exerciseName == self.name) {
                self.execution = (history.execution) ?? ""
                self.repetition = (history.repetition) ?? ""
                self.rest = (history.rest) ?? ""
                self.weight = (history.weight) ?? ""
                self.series = (history.series) ?? ""
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
