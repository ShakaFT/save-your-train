//
//  HistoryView.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import SwiftUI

struct HistoryView: View {
    @FetchRequest(sortDescriptors: []) var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(histories) { history in
                            NavigationLink(destination: ActiveHistory()) {
                                HStack {
                                    Text(history.name ?? "")
                                    Text(getDate(timestamp: history.dateMs))
                                }
                            }
                        }.onDelete(perform: removeHistory)
                    }
                VStack{
                    Spacer()
                    HStack {
                        Button(action: getPreviousHistories ) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Button(action: getNextHistories ) {
                            Image(systemName: "arrow.right.circle")
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
    
    func removeHistory(at offsets: IndexSet) {
     for offset in offsets {
        let history = histories[offset]
            element.delete(history)
        }
     try? element.save()
    }
    
    func getDate(timestamp: Double) -> String {
        let date = Date(timeIntervalSince1970: timestamp)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: TimeZone.current.identifier) //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "dd-MM-yyyy HH:mm" //Specify your format that you want
        let strDate = dateFormatter.string(from: date)
        return strDate
    }
    
    func getPreviousHistories() {
        
    }
    
    func getNextHistories() {
        
    }
}

struct HistoryView_Previews: PreviewProvider {
    static var previews: some View {
        HistoryView()
    }
}
