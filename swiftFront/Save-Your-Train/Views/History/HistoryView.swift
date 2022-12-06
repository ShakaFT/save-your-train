import SwiftUI

struct HistoryView: View {
    @FetchRequest(entity: History.entity(),
                  sortDescriptors: [NSSortDescriptor(keyPath: \History.dateMs, ascending: false)])
    var histories: FetchedResults<History>
    
    @Environment(\.managedObjectContext) var element
    
    @State public var historyArray: Array<History> = []
    @State public var currentPage: Int = 1
    
    
    var body: some View {
        NavigationView {
            ZStack {
                List {
                    ForEach(self.getHistoriesToDisplay()) { (history: History) in
                        NavigationLink(destination: ActiveHistoryView(history: getHistory(history: history))) {
                            
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
                        Components.buttonImage(image: "arrow.backward.circle", action: self.previousPage)
                            .disabled(self.currentPage == 1)
                        
                        Components.buttonImage(image: "arrow.right.circle", action: self.nextPage)
                            .disabled(self.currentPage*10 >= self.historyArray.count)
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
    
    public func getHistoriesToDisplay() -> ArraySlice<FetchedResults<History>.Element> {
        return self.historyArray[(currentPage-1)*10..<self.getMaxPage()]
    }
    
    public func nextPage() {
        self.currentPage += 1
    }
    
    public func previousPage() {
        self.currentPage -= 1
    }
    
    public func getHistory(history: History) -> HistoryModel {
        return HistoryModel(
            dateMs: history.dateMs,
            exerciseName: history.exerciseName!,
            execution: history.execution!,
            repetition: history.repetition!,
            rest: history.rest!,
            series: history.series!,
            weight: history.weight!
        )
    }
    
    private func getMaxPage() -> Int {
        let result: Int = currentPage * Constants.nbItems
        return result > self.historyArray.count ? self.historyArray.count : result
    }
}
