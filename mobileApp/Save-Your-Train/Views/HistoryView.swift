import SwiftUI

struct HistoryView: View {
    @FetchRequest(entity: History.entity(), sortDescriptors: []) var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(self.histories) { (history: History) in
                            NavigationLink(destination: ActiveHistoryView()) {
                                HStack {
                                    Text(history.name!)
                                    Spacer()
                                    Text(self.getDate(timestamp: history.dateMs))
                                }
                            }
                        }.onDelete(perform: self.removeHistory)
                    }
                VStack{
                    Spacer()
                    HStack {
                        Button(action: self.getPreviousHistories ) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }
                        Button(action: self.getNextHistories ) {
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
            self.element.delete(self.histories[offset])
        }
        try? self.element.save()
    }
    
    func getDate(timestamp: Double) -> String {
        let date: Date = Date(timeIntervalSince1970: timestamp)
        let dateFormatter: DateFormatter = DateFormatter()
        
        dateFormatter.timeZone = TimeZone(abbreviation: TimeZone.current.identifier)
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "HH:mm dd-MM-yyyy"
        
        return dateFormatter.string(from: date)
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
