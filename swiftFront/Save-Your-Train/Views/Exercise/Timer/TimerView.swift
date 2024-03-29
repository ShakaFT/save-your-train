import SwiftUI

struct TimerView: View {
    @ObservedObject var timerManager : TimerManager
    var time: Double
    
    init(time: Double) {
        self.time = time
        self.timerManager = TimerManager(time: self.time)
    }
    
    var body: some View {
        VStack {
            Text(String(format: "%.1f", self.timerManager.timeLeft)).font(.system(size: 50))
            if (self.timerManager.mode == .notStarted) {
                Button(action: self.timerManager.start) {
                        Text("Démarrer")
                            .padding(.vertical, 20)
                            .padding(.horizontal, 90)
                }
                .cornerRadius(10)
                .foregroundColor(.green)
                .overlay(RoundedRectangle(cornerRadius: 10).stroke(.green, lineWidth: 1))
            }
        }
    }
}

struct Timer_Previews: PreviewProvider {
    static var previews: some View {
        TimerView(time: 0.0)
    }
}
