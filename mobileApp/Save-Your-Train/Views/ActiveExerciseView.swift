import SwiftUI

struct ActiveExerciseView: View {
    
    let name: String
    let description: String
    
    @State private var execution: String = ""
    @State private var rest: String = ""
    @State private var repetition: String = ""
    @State private var weight: String = ""
    @State private var series: String = ""
    
    @State private var disabled: Bool = true
    @State private var sheetAppear: Bool = false
    
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
                     
                }
                .cornerRadius(20)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .padding()
                
                Button(action: {self.sheetAppear.toggle()}) {
                    Text("Lancer l'exercice").padding()
                }
                .disabled(self.disabled)
                .cornerRadius(10)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                .padding()
                .sheet(isPresented: self.$sheetAppear) {
                    if(self.execution.isEmpty && self.rest.isEmpty && !self.repetition.isEmpty && self.weight.isEmpty) {
                        Repet(repetition: self.repetition, series: self.series, name: self.name)
                    } else if (self.execution.isEmpty && self.rest.isEmpty && !self.repetition.isEmpty && !self.weight.isEmpty) {
                        WeightRepet(repetition: self.repetition, series: self.series, name: self.name, weight: self.weight)
                    }
                }
                
            }
        }.navigationTitle(self.name)
    }
    
    func activeButton(arg: String) {
        if (self.execution.isEmpty && self.rest.isEmpty && self.repetition.isEmpty && self.weight.isEmpty && self.series.isEmpty ) {
            self.disabled = true
        } else if (!self.execution.isEmpty && !self.repetition.isEmpty) {
            self.disabled = true
        }
        else if (self.execution.isEmpty && !self.rest.isEmpty && self.repetition.isEmpty && self.weight.isEmpty) {
            self.disabled = true
        }
        else if (self.execution.isEmpty && self.rest.isEmpty && self.repetition.isEmpty && !self.weight.isEmpty) {
            self.disabled = true
        }
        else if (self.execution.isEmpty && self.rest.isEmpty && self.repetition.isEmpty && self.weight.isEmpty && !self.series.isEmpty) {
            self.disabled = true
        }
        else if (self.execution.isEmpty && self.rest.isEmpty && self.repetition.isEmpty && !self.weight.isEmpty) {
            self.disabled = true
        }
        else if (self.execution.isEmpty && !self.rest.isEmpty && self.repetition.isEmpty && !self.weight.isEmpty) {
            self.disabled = true
        }
        else {
            self.disabled = false
        }
    }
}

struct ActiveExerciseView_Previews: PreviewProvider {
    static var previews: some View {
        ActiveExerciseView(name: "", description: "")
    }
}
