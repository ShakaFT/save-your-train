import SwiftUI

struct ActiveHistoryView: View {
    
    @State var deleteHistory: Bool = false
    
    let history: HistoryModel
    
    
    var body: some View {
        ZStack {
            VStack(spacing: UIScreen.main.bounds.height * 0.05) {
                VStack {
                    Text(Utils.getDate(timestamp: self.history.dateMs, format: .date))
                        .bold()
                        .font(.system(size: 30))
                    
                    Text(Utils.getDate(timestamp: self.history.dateMs, format: .hour))
                        .bold()
                        .font(.system(size: 30))
                }
                
                VStack (alignment: .leading ,spacing: UIScreen.main.bounds.height * 0.05) {
                    if (!self.history.repetition.isEmpty) {
                        HStack {
                            Text("Nombre de répetitions")
                                .bold()
                                .font(.system(size: 20))
                            
                            Spacer()
                            
                            Text(self.history.repetition)
                                .font(.system(size: 20))
                                .foregroundColor(.gray)
                        }
                    }
                    
                    if (!self.history.execution.isEmpty) {
                        HStack {
                            Text("Temps d'exécution")
                                .bold()
                                .font(.system(size: 20))
                            
                            Spacer()
                            
                            Text("\(self.history.execution) s")
                                .font(.system(size: 20))
                                .foregroundColor(.gray)
                        }
                    }
                    
                    if (!self.history.weight.isEmpty) {
                        HStack {
                            Text("Poids")
                                .bold()
                                .font(.system(size: 20))
                            
                            Spacer()
                            
                            Text("\(self.history.weight) kg")
                                .font(.system(size: 20))
                                .foregroundColor(.gray)
                        }
                    }
                    
                    if (!self.history.rest.isEmpty) {
                        HStack {
                            Text("Temps de repos")
                                .bold()
                                .font(.system(size: 20))
                            
                            Spacer()
                            
                            Text("\(self.history.rest) s")
                                .font(.system(size: 20))
                                .foregroundColor(.gray)
                        }
                    }
                    
                    if (!self.history.series.isEmpty) {
                        HStack {
                            Text("Nombre de séries")
                                .bold()
                                .font(.system(size: 20))
                            
                            Spacer()
                            
                            Text(self.history.series)
                                .font(.system(size: 20))
                                .foregroundColor(.gray)
                        }
                    }
                }
                .padding()
                
                Components.buttonImage(image: "trash", action: {self.deleteHistory.toggle()})
                    .foregroundColor(.red)
            }
            
            DeleteHistoryPopup(show: self.$deleteHistory, dateMs: self.history.dateMs)
        }
        .navigationTitle(history.exerciseName)
        .navigationBarTitleDisplayMode(.inline)
    }
}
