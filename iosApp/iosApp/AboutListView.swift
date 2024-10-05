
import shared
import SwiftUI

struct AboutListView: View {
    var body: some View {
        VStack(spacing: 24) {
            Image(resource: \.app_info)
                .resizable()
                .scaledToFit()
                .frame(width: 200, height: 200)
                .clipShape(Circle())
            
            Text (Strings().get(id: SharedResources.strings().app_info_screen_text, args: []))
                .font(.system(size: 18))
                .multilineTextAlignment(.center)
                .padding(.horizontal, 16)
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

