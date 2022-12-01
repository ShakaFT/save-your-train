import Foundation

class Utils {
    
    enum format: String {
        case fullDate = "HH:mm dd/MM/yyyy"
        case hour = "HH:mm"
        case date = ""
    }
    
    public static func getDate(timestamp: Double, format: format = .fullDate) -> String {
        let date: Date = Date(timeIntervalSince1970: timestamp)
        let dateFormatter: DateFormatter = DateFormatter()
        
        dateFormatter.timeZone = TimeZone(abbreviation: TimeZone.current.identifier)
        dateFormatter.locale = NSLocale.current
        if(format == .date) {
            dateFormatter.dateStyle = .long
        } else {
            dateFormatter.dateFormat = format.rawValue
        }
        
        return dateFormatter.string(from: date)
    }
}
