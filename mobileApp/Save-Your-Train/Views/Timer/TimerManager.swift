import Foundation
import SwiftUI

class TimerManager: ObservableObject {
    
    enum timerMode {
        case running
        case notStarted
        case stopped
    }
    
    @Published var mode: timerMode = .notStarted
    @Published var timeLeft: Double
    
    var timer = Timer()
    
    
    init(time: Double = 0.0) {
        self.timeLeft = time
    }
    
    public func start() {
        self.mode = .running
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { _ in
            self.updateTimer()
        }
    }
    
    @objc public func updateTimer() {
        if (self.timeLeft < 0.1) {
            self.mode = .stopped
            timer.invalidate()
        } else {
            self.timeLeft -= 0.1
        }
    }
}

