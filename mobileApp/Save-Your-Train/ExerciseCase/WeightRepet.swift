import SwiftUI

struct WeightRepet: View {
    @Environment(\.managedObjectContext) var element
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    @State var nbSeries: Int = 1
    
    let repetition: String
    let series: String
    let name: String
    let weight: String
    
    var body: some View {
        NavigationView {
            ZStack {
                VStack(spacing: UIScreen.main.bounds.height * 0.25) {
                    VStack {
                        Text("Nombre de répétitions").font(.system(size: 30))
                        Text(self.repetition).font(.system(size: 70))
                        Text("Poids").font(.system(size: 30))
                        Text(self.weight).font(.system(size: 70))
                        Text(self.nbSeries == 1 ? "Série restante :" : "Séries restantes :").font(.system(size: 30))
                        Text(String(self.nbSeries)).font(.system(size: 40))
                    }
                    VStack {
                        Button(action: {
                            self.nbSeries == 1 ? self.stopExercise() : self.nextSeries()
                        }) {
                            Text(self.nbSeries == 1 ? "Terminer l'exercice" : "Série suivante").padding()
                        }
                        .cornerRadius(10)
                        .overlay(RoundedRectangle(cornerRadius: 20).stroke(.green, lineWidth: 1))
                        .foregroundColor(.green)
                        .padding()
                    }
                }
            }
            .onAppear(perform: {self.nbSeries = Int(self.series) ?? 1})
            .navigationTitle(self.name)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    func stopExercise() {
        let history = History(context: self.element)
        history.name = self.name
        history.series = self.series.isEmpty ? "1" : self.series
        history.repetition = self.repetition
        history.weight = self.weight
        history.dateMs = NSDate().timeIntervalSince1970
        try? self.element.save()
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func nextSeries() {
        self.nbSeries -= 1
    }
}

struct WeightRepet_Previews: PreviewProvider {
    static var previews: some View {
        WeightRepet(repetition: "", series: "", name: "", weight: "")
    }
}
