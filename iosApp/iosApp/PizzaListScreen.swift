import SwiftUI
import shared

extension PizzaListScreen {
    
    @MainActor
    class PizzaListViewModelWrapper: ObservableObject {
       private let pizzaListViewModel: PizzaListViewModel
        
        @Published var pizzaListState: PizzaListState
        
        var isOnlyFavourites: Bool = false
        
        init(isOnlyFavourites: Bool = false) {
            self.isOnlyFavourites = isOnlyFavourites
            let provider = ProvidePizzaListViewModel()
            pizzaListViewModel = provider.provideViewModel() as! PizzaListViewModel
            pizzaListState = pizzaListViewModel.pizzaListState.value
            
            startObserving()
        }
        
        func refreshData() {
            Task {
                if self.isOnlyFavourites {
                    print("Fetching favorite restaurants.")
                    pizzaListViewModel.fetchFavouriteRests()
                } else {
                    print("Fetching all restaurants.")
                    pizzaListViewModel.fetchData()
                }
            }
        }
        
        func startObserving() {
            Task {
                for await state in pizzaListViewModel.pizzaListState {
                    DispatchQueue.main.async {
                        self.pizzaListState = state
                    }
                }
            }
        }
        
        func insertFavouriteRest(restaurantItemModel: PizzaRestaurantItemModel) {
            pizzaListViewModel.insertFavouriteRestaurant(restaurantItemModel: restaurantItemModel)
        }
        
        func deleteFavouriteRest(id: Int64) {
            pizzaListViewModel.deleteFavouriteRestaurant(id: id)
        }
    }
}


struct PizzaListScreen: View {
    
    @ObservedObject private(set) var viewModel: PizzaListViewModelWrapper
    
    @State private var showSidebar = false
    @State private var shouldOpenAbout = false
    @State private var selectedPizzaItem: PizzaRestaurantItemModel?
    @State private var isRefreshing = false
        
    var body: some View {
        NavigationStack {
            List {
                switch viewModel.pizzaListState {
                case is PizzaListLoading:
                    Loader()
                        .listRowBackground(Color.clear)
                
                case let errorState as PizzaListError:
                    ErrorMessage(message: errorState.message)
                        .listRowBackground(Color.clear)
                
                case let successState as PizzaListSuccess:
                    ForEach(successState.items, id: \.self) { pizzaItem in
                        PizzaItemView(pizzaItem: pizzaItem,
                                      onFavouriteClick: { isFavorite in
                                          if isFavorite {
                                              viewModel.insertFavouriteRest(restaurantItemModel: pizzaItem)
                                          } else {
                                              viewModel.deleteFavouriteRest(id: Int64(pizzaItem.id))
                                          }
                                      },
                                      onItemClick: {
                                          selectedPizzaItem = pizzaItem
                                      })
                    }
                    .refreshable {
                        isRefreshing = true
                        viewModel.refreshData()
                        isRefreshing = false
                    }
                case let successState as PizzaListEmpty: EmptyFavouritesScreen()
                
                default:
                    EmptyView()
                        .listRowBackground(Color.clear)
                }
            }
            .listStyle(PlainListStyle())
            .navigationTitle(viewModel.isOnlyFavourites ? Strings().get(id: SharedResources.strings().favourites, args: []) : Strings().get(id: SharedResources.strings().restaurants, args: []))
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        showSidebar.toggle()
                    }) {
                        Image(systemName: "list.bullet")
                    }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: {
                        shouldOpenAbout.toggle()
                    }) {
                        Image(systemName: "info.circle")
                    }
                }
            }
            .sheet(isPresented: $showSidebar) {
                SidebarView()
            }
            .popover(isPresented: $shouldOpenAbout) {
                AboutScreen()
            }
            .navigationDestination(for: PizzaRestaurantItemModel.self) { pizzaItem in
                RestaurantDetailsScreen(viewModel: .init(), id: pizzaItem.id)
            }
            .onAppear {
                viewModel.refreshData()
            }
        }
        .navigationDestination(isPresented: Binding<Bool>(
            get: { selectedPizzaItem != nil },
            set: { if !$0 { selectedPizzaItem = nil } }
        )) {
            if let pizzaItem = selectedPizzaItem {
                RestaurantDetailsScreen(viewModel: .init(), id: pizzaItem.id)
            }
        }
    }
}

struct PizzaItemView: View {
    var pizzaItem: PizzaRestaurantItemModel
    var onFavouriteClick: (Bool) -> Void
    var onItemClick: () -> Void

    private let itemHeight: CGFloat = 200
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            AsyncImage(url: URL(string: pizzaItem.image)) { phase in
                if let image = phase.image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(height: itemHeight)
                        .clipped()
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                } else if phase.error != nil {
                    Text("Image Load Error")
                        .frame(height: itemHeight)
                        .frame(maxWidth: .infinity)
                        .background(Color.gray.opacity(0.2))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                } else {
                    ProgressView()
                        .frame(height: itemHeight)
                        .frame(maxWidth: .infinity)
                        .background(Color.gray.opacity(0.2))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }
            }
            .onTapGesture {
                onItemClick()
            }

            HStack {
                Text(pizzaItem.title)
                    .font(.title2)
                    .fontWeight(.bold)
                Spacer()
                Button(action: {
                    onFavouriteClick(!pizzaItem.isFavourite)
                }) {
                    Image(systemName: pizzaItem.isFavourite ? "heart.fill" : "heart")
                        .foregroundColor(pizzaItem.isFavourite ? .red : .gray)
                        .padding(10)
                        .background(Color.clear)
                        .contentShape(Rectangle())
                }
            }
            
            if !pizzaItem.messageDescription.isEmpty {
                Text(pizzaItem.messageDescription)
                    .lineLimit(2)
                    .truncationMode(.tail)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .padding(8) 
        .background(Color(UIColor.lightGray).opacity(0.1))
        .cornerRadius(12)
        .shadow(radius: 4)
        .padding(.horizontal, 8)
    }
}


struct SidebarView: View {
    var body: some View {
        NavigationStack {
            List {
                NavigationLink(destination: PizzaListScreen(viewModel: .init(isOnlyFavourites: true))) {
                    Label(Strings().get(id: SharedResources.strings().favourites, args: []), systemImage: "heart")
                }
                
                NavigationLink(destination: BasketScreen(
                    basketViewModel: .init(),
                       onBackClick: {
                           
                       }
                )) {
                    Label(Strings().get(id: SharedResources.strings().basket, args: []), systemImage: "cart")
                }

                NavigationLink(destination: Text(Strings().get(id: SharedResources.strings().recent_orders, args: []))) {
                    Label(Strings().get(id: SharedResources.strings().recent_orders, args: []), systemImage: "clock")
                }
                
                Divider()
                NavigationLink(destination: Text("Support")) {
                    Label("Support", systemImage: "person.crop.circle")
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationTitle("More")
        }
    }
}

struct Loader: View {
    var body: some View {
        ZStack {
            Color.clear
            ProgressView()
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        }
        .edgesIgnoringSafeArea(.all)
    }
}

struct ErrorMessage: View {
    var message: String
    
    var body: some View {
        Text(message)
            .font(.title)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }
}

struct EmptyFavouritesScreen: View {
    var body: some View {
        VStack {
            
            Spacer()
            
            VStack(spacing: 16) {
                Image(resource: \.empty_list)
                    .resizable()
                    .frame(width: 250, height: 250)

                Text("You don't have any favorite restaurants yet")
                    .font(.body)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
            }
            .padding(.bottom, 50)
            .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
