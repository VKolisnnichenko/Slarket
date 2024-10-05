import SwiftUI
import shared

struct ContentView: View {
    
    @State private var shouldOpenAbout = false
    @State private var showSidebar = false
    
    var body: some View {
        NavigationStack {
            PizzaListScreen(viewModel: .init())
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
