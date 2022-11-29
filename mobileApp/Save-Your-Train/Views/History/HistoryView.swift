import SwiftUI

struct HistoryView: View {
    @FetchRequest(entity: History.entity(), sortDescriptors: []) var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(self.histories) { (history: History) in
                            NavigationLink(destination: ActiveHistoryView(history: HistoryModel(dateMs: history.dateMs, exerciseName: history.exerciseName!, execution: history.execution ?? "", repetition: history.repetition ?? "", rest: history.rest ?? "", series: history.series ?? "1", weight: history.weight ?? "" ))) {
                                HStack {
                                    Text(history.exerciseName!)
                                    Spacer()
                                    Text(Constants.getDate(timestamp: history.dateMs))
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
