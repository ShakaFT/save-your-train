//
//  Constant.swift
//  Save-Your-Train
//
//  Created by Hugo Martin on 21/11/2022.
//

import Foundation
import SwiftUI

class Constants {
    @AppStorage("email") public var email: String = "test@gmail.com"
    
    public let urlRestApi: String = "https://save-your-train.ew.r.appspot.com"
}
