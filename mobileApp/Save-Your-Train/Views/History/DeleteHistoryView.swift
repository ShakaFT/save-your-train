import SwiftUI

struct DeleteHistoryView: View {
    
    @FetchRequest(sortDescriptors: []) var histories: FetchedResults<History>
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) var element
    
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
                        Button(action: {show = false}) {
                            Text("Annuler").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue, lineWidth: 1))
                        .padding()
                           
                        Button(action: {
                            Task {
                                self.disabled = true
                                await removeHistory(dateMs: self.dateMs)
                                self.disabled = false
                            }
                        }) {
                            Text("Supprimer").padding()
                        }
                        .cornerRadius(10)
                        .disabled(self.disabled)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.red, lineWidth: 1))
                        .foregroundColor(.red)
                        .padding()
                    }
                }
                .frame(maxWidth: 300)
                .border(Color.black, width: 1)
                .background(.white)
            }
        }
    }
    
    func removeHistory(dateMs: Double) async {
        let worked: Bool = await Network.deleteRemoteHistory(timestamp: dateMs)
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
