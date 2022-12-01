import SwiftUI

struct DeleteHistoryView: View {
    
    @FetchRequest(sortDescriptors: []) var histories: FetchedResults<History>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
    @EnvironmentObject var network: Network
    
    @Binding var show: Bool
    
    @State private var disabled: Bool = false
    
    var dateMs: Double
    
    var body: some View {
        ZStack {
            if show {
                Color.black.opacity(show ? 0.3 : 0).edgesIgnoringSafeArea(.all)

                VStack(alignment: .center, spacing: 0) {
                    Text("Voulez vous vraiment supprimer cet historique ?")
                        .bold()
                        .font(Font.system(size: 20))
                        .padding()
                    HStack {
                        Components.button(name: "Annuler", action: {show = false})
                        Components.button(name: "Supprimer", color: .red, action: {
                            Task {
                                self.disabled = true
                                await removeHistory(dateMs: self.dateMs)
                                self.disabled = false
                            }
                        }).disabled(self.disabled)
                    }
                }
                .frame(maxWidth: 300)
                .border(Color.black, width: 1)
                .background(.white)
            }
        }
    }
    
    func removeHistory(dateMs: Double) async {
        let worked: Bool = await network.deleteRemoteHistory(timestamp: dateMs)
        if (!worked) {
            return
        }
        
        for history in histories {
            if (history.dateMs == dateMs) {
                self.element.delete(history)
                self.presentationMode.wrappedValue.dismiss()
            }
        }
        try? self.element.save()
    }
}
