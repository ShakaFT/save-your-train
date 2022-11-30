import SwiftUI

struct ActiveHistoryView: View {
    
    let history: HistoryModel
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: UIScreen.main.bounds.height * 0.05) {
                    VStack {
                        Text(Constants.getDate(timestamp: history.dateMs, format: .date)).font(.system(size: 30)).bold()
                        Text(Constants.getDate(timestamp: history.dateMs, format: .hour)).font(.system(size: 30)).bold()
                    }
                    
                    VStack (alignment: .leading ,spacing: UIScreen.main.bounds.height * 0.05) {
                        if (!history.repetition.isEmpty){
                            HStack {
                                Text("Nombre de répetitions").font(.system(size: 20)).bold()
                                Spacer()
                                Text(history.repetition).font(.system(size: 20)).foregroundColor(.gray)
                            }
                        }
                        
                        if (!history.execution.isEmpty){
                            HStack {
                                Text("Temps d'exécution").font(.system(size: 20)).bold()
                                Spacer()
                                Text("\(history.execution) s").font(.system(size: 20)).foregroundColor(.gray)
                            }
                        }
                        
                        if (!history.weight.isEmpty){
                            HStack {
                                Text("Poids").font(.system(size: 20)).bold()
                                Spacer()
                                Text("\(history.weight) kg").font(.system(size: 20)).foregroundColor(.gray)
                            }
                        }
                        
                        if (!history.rest.isEmpty){
                            HStack {
                                Text("Temps de repos").font(.system(size: 20)).bold()
                                Spacer()
                                Text("\(history.rest) s").font(.system(size: 20)).foregroundColor(.gray)
                            }
                        }
                        
                        if (!history.series.isEmpty){
                            HStack {
                                Text("Nombre de séries").font(.system(size: 20)).bold()
                                Spacer()
                                Text(history.series).font(.system(size: 20)).foregroundColor(.gray)
                            }
                        }
                    }
                    .padding()
                }
                
            }
            .navigationTitle(history.exerciseName)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct ActiveHistoryView_Previews: PreviewProvider {
    static var previews: some View {
        ActiveHistoryView(history: HistoryModel())
    }
}
