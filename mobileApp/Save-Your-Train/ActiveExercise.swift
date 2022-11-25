//
//  ActiveExercise.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import SwiftUI

struct ActiveExercise: View {
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
                        Text((description.isEmpty ? "Aucune description" : description))
                    }.padding()
                }
                .cornerRadius(20)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .padding()
                
                VStack(alignment: .leading, spacing: 0) {
                    
                    HStack {
                        Text("Execution")
                        TextField("60 sec (facultatif)", text: $execution)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: execution , perform: {activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Repos")
                        TextField("60 sec (facultatif)", text: $rest)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: rest , perform: {activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Répétitions")
                        TextField("10 (facultatif)", text: $repetition)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: repetition , perform: {activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Poids")
                        TextField("20 kg (facultatif)", text: $weight)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: weight , perform: {activeButton(arg: $0)})
                    }.padding()
                    
                    HStack {
                        Text("Séries")
                        TextField("4 (facultatif)", text: $series)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.numberPad)
                            .onChange(of: series , perform: {activeButton(arg: $0)})
                    }.padding()
                     
                }
                .cornerRadius(20)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.black, lineWidth: 1))
                .padding()
                
                Button(action: {sheetAppear.toggle()}) {
                    Text("Lancer l'exercice").padding()
                }
                .disabled(self.disabled)
                .cornerRadius(10)
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                .padding()
                .sheet(isPresented: $sheetAppear) {
                    Repet(repetition: repetition, series: series, name: name)
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

struct ActiveExercise_Previews: PreviewProvider {
    static var previews: some View {
        ActiveExercise(name: "", description: "")
    }
}
