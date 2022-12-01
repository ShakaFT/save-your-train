import Foundation
import SwiftUI

class Components {
    
    public static func button(name: String, color: Color = .blue, action: @escaping () -> Void ) -> some View {
        return Button(action: action) {
            Text(name).padding()
        }
        .cornerRadius(10)
        .foregroundColor(color == .blue ? nil: color)
        .overlay(RoundedRectangle(cornerRadius: 10).stroke(color, lineWidth: 1))
        .padding()
    }
}
