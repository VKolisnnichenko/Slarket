import Foundation
import shared
import SwiftUI

extension Image {
    init(resource: KeyPath<SharedResources.images, shared.ImageResource>) {
        self.init(uiImage: SharedResources.images()[keyPath: resource].toUIImage()!)
    }
}
