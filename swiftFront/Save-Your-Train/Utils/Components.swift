import Foundation
import SwiftUI

class Components {
    
    public static func button(name: String, color: Color = .blue, action: @escaping () -> Void) -> some View {
        return Button(action: action) {
            Text(name).padding()
        }
        .cornerRadius(10)
        .foregroundColor(color == .blue ? nil: color)
        .overlay(RoundedRectangle(cornerRadius: 10).stroke(color, lineWidth: 1))
        .padding()
    }
    
    public static func buttonImage(image: String, action: @escaping () -> Void) -> some View {
        return Button(action: action) {
            Image(systemName: image)
                .font(.largeTitle)
                .frame(width: 70, height: 70)
        }
    }
    
    public static func error(text: String) -> some View {
        return Text(text)
            .bold()
            .font(.system(size: 15))
            .foregroundColor(.red)
    }
    
    public static func label(text: String) -> some View {
        return HStack {
            Text(text)
                .bold()
                .font(.system(size: 15))
            Spacer()
        }
    }
    
    public static func textField(text: Binding<String>, placeholder: String = "", password: Bool = false) -> some View {
        let view: AnyView = password ? AnyView(SecureField(placeholder, text: text)): AnyView(TextField(placeholder, text: text))
        return view.textFieldStyle(.roundedBorder)
    }
}
