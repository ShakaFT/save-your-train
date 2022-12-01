import SwiftUI

struct HistoryView: View {
    @FetchRequest(entity: History.entity(),
                  sortDescriptors: [NSSortDescriptor(keyPath: \History.dateMs, ascending: false)]) var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    
    @State public var historyArray: Array<History> = []
    @State public var currentPage: Int = 1
    
    var body: some View {
        NavigationView {
            ZStack {
                    List {
                        ForEach(self.getHistoriesToDisplay()) { (history: History) in
                            NavigationLink(destination: ActiveHistoryView(history: HistoryModel(dateMs: history.dateMs, exerciseName: history.exerciseName!, execution: history.execution ?? "", repetition: history.repetition ?? "", rest: history.rest ?? "", series: history.series ?? "1", weight: history.weight ?? "" ))) {
                                HStack {
                                    Text(history.exerciseName!)
                                    Spacer()
                                    Text(Utils.getDate(timestamp: history.dateMs))
                                }
                            }
                        }
                    }
                VStack{
                    Spacer()
                    HStack {
                        Button(action: self.previousPage ) {
                            Image(systemName: "arrow.backward.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }.disabled(self.currentPage == 1)
                        
                        Button(action: self.nextPage ) {
                            Image(systemName: "arrow.right.circle")
                                .font(.largeTitle)
                                .frame(width: 70, height: 70)
                        }.disabled(self.currentPage*10>=self.historyArray.count)
                    }
                    .padding()
                }
            }
            .navigationTitle("Historique")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear(perform: {
                self.historyArray = Array(self.histories)
            })
        }
    }
    
    func getHistoriesToDisplay() -> ArraySlice<FetchedResults<History>.Element> {
        return self.historyArray[(currentPage-1)*10..<self.getMaxPage()]
    }
    
    func nextPage() {
        self.currentPage += 1
    }
    
    func previousPage() {
        self.currentPage -= 1
    }
    
    private func getMaxPage() -> Int {
        let result: Int = currentPage * Constants.nbItems
        if (result > self.historyArray.count) {
            return self.historyArray.count
        }
        return result
    }
}

struct HistoryView_Previews: PreviewProvider {
    static var previews: some View {
        HistoryView()
    }
}
