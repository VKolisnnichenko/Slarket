import SwiftUI
import shared


@MainActor
class RestaurantDetailsViewModelWrapper: ObservableObject {
    
    let restaurantDetailsViewModel: RestaurantDetailsViewModel
    
    @Published var restDetailsState: RestaurantDetailsState
    @Published var showBasket: Bool = false
    @Published var displayBasketDialog : Bool = false

    
    init() {
        let viewModelProider = ProvideRestaurantDetailsViewModel()
        restaurantDetailsViewModel = viewModelProider.provideViewModel() as! RestaurantDetailsViewModel
        restDetailsState = restaurantDetailsViewModel.restDetailsState.value
    
        startObserving()
    }
    
    func updateShowBasket() {
        if let successState = restDetailsState as? RestDetailsStateSuccess {
            DispatchQueue.main.async {
                self.showBasket = successState.restaurantDetailsModel.showBasket

           }
        }
    }
    
    func updateDisplayBasketDialog() {
        if let successState = restDetailsState as? RestDetailsStateSuccess {
            DispatchQueue.main.async {
                self.displayBasketDialog = successState.restaurantDetailsModel.displayBasketDialog
            }
        }
    }
    
    func fetchData(id : Int32) {
        restaurantDetailsViewModel.fetchRestDetails(restId: id)
    }
    
    func isShowBasket(){
        restaurantDetailsViewModel.isShowBasketFAB()
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.updateShowBasket()
        }
    }
    
    func addToBasket(){
        restaurantDetailsViewModel.addToBasket()
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.updateShowBasket()
            self.updateDisplayBasketDialog()
        }
    }
    
    func resetBasket(pizzaModel: PizzaModel){
        restaurantDetailsViewModel.resetBasketModel(pizzaModel: pizzaModel)
    }
    
    func replaceBasketItems(){
        restaurantDetailsViewModel.replaceBasketItem()
    }
    
    func insertFavouriteRest(){
        restaurantDetailsViewModel.addToFavourite()
    }
    
    func deleteFavouriteRest(){
        restaurantDetailsViewModel.removeFromFavourite()
    }
    
    func onDishSelected(isSelected : Bool){
        restaurantDetailsViewModel.updateSelectedDish(isSelected: isSelected)
    }
    
    func startObserving() {
        Task {
            for await state in restaurantDetailsViewModel.restDetailsState {
                DispatchQueue.main.async {
                    self.restDetailsState = state
                    self.updateShowBasket()
                    self.updateDisplayBasketDialog()
                }
            }
        }
    }
}

struct RestaurantDetailsScreen: View {
    @ObservedObject private var viewModel: RestaurantDetailsViewModelWrapper
    @Environment(\.presentationMode) private var presentationMode

    init(viewModel: RestaurantDetailsViewModelWrapper, id: Int32) {
        self.viewModel = viewModel
        viewModel.fetchData(id: id)
    }

    var body: some View {
        NavigationView {
            VStack {
                switch viewModel.restDetailsState {
                case is RestDetailsStateLoading:
                    LoadingView()
                case let errorState as RestDetailsStateError:
                    ErrorView(message: errorState.message)
                case let successState as RestDetailsStateSuccess:
                    RestaurantDetailsContent(details: successState.restaurantDetailsModel,
                                             showBasket: $viewModel.showBasket,
                                             displayBasketDialog: $viewModel.displayBasketDialog,
                                             onAddToBasketClick: {
                        viewModel.addToBasket()
                    }, replaceBasketItems: {
                        viewModel.replaceBasketItems()
                    }, updateFavoriteState: { isFavorite in
                        if(isFavorite){
                            viewModel.insertFavouriteRest()
                        }else{
                            viewModel.deleteFavouriteRest()
                        }
                        
                    },onDishSelected: viewModel.onDishSelected) { pizzaModel in
                        viewModel.resetBasket(pizzaModel: pizzaModel)
                    }
                default:
                    EmptyView()
                }
            }
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                    viewModel.isShowBasket()
                }
            }
        }
    }
}



struct LoadingView: View {
    var body: some View {
        ProgressView()
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct ErrorView: View {
    var message: String
    
    var body: some View {
        Text(message)
            .foregroundColor(.red)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .multilineTextAlignment(.center)
    }
}

struct DetailsPizzaItemView: View {
    var pizza: PizzaModel
    var onItemClick: () -> Void
    
    var body: some View {
        Button(action: onItemClick) {
            HStack {
                AsyncImage(url: URL(string: pizza.pizzaImageUrl)) { phase in
                    if let image = phase.image {
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 100, height: 100)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    } else if phase.error != nil {
                        Color.gray
                            .frame(width: 100, height: 100)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    } else {
                        ProgressView()
                            .frame(width: 100, height: 100)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
                
                VStack(alignment: .leading, spacing: 4) {
                    Text(pizza.pizzaName)
                        .font(.headline)
                    
                    Text("$\(pizza.pizzaPrice)")
                        .font(.subheadline)
                        .foregroundColor(.orange)
                    Text(pizza.desc)
                        .font(.caption)
                .multilineTextAlignment(.leading)
                        .lineLimit(2)
                }
                .padding(.leading, 8)
                
                Spacer()
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(8)
            .padding(.horizontal)
        }
    }
}
struct FloatingActionButton: View {
    var action: () -> Void
    
    var body: some View {
        VStack {
            Spacer()
            HStack {
                Spacer()
                Button(action: action) {
                    Image(systemName: "cart.fill")
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.orange)
                        .clipShape(Circle())
                        .shadow(radius: 5)
                }
                .frame(width: 56, height: 56)
                .padding(.trailing, 20)
                .padding(.bottom, 20)
            }
        }
    }
}


struct PizzaBottomSheetView: View {
    @State var basketItemModel : BasketItemModel

    var pizza: PizzaModel
    var onDishSelected: (Bool) -> Void
    var onAddToBasketClick: () -> Void

        
    var body: some View {
        VStack(spacing: 24) {
    
            AsyncImage(url: URL(string: pizza.pizzaImageUrl)) { phase in
                if let image = phase.image {
                    image
                        .resizable()
                        .frame(height: 200)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                } else if phase.error != nil {
                    Color.gray.frame(height: 200)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                } else {
                    ProgressView()
                        .frame(height: 200)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                }
            }

            HStack {
                Text(basketItemModel.name)
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Text("$\(basketItemModel.sum)")
                    .font(.title2)
                    .foregroundColor(.orange)
                    .padding(.trailing, 16)
            }

            Text("Ingredients: \(pizza.desc)")
                .font(.body)
                .foregroundColor(.secondary)
                .frame(maxWidth: .infinity, alignment: .leading)


            CutleryAndSaucesView(addCutlery: $basketItemModel.isCutlery
                                 , addSauces: $basketItemModel.isSauces,onDishSelected: onDishSelected)

            TextField(Strings().get(id: SharedResources.strings().feel_free_to_write_your_wishes, args: []), text: $basketItemModel.userWishes)
                .padding()
                .background(Color.white)
                .cornerRadius(8)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                )

            Button(action: onAddToBasketClick) {
                Text(Strings().get(id: SharedResources.strings().add_to_basket, args: []))
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.orange)
                    .cornerRadius(10)
            }
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 50)
    }
}


struct CutleryAndSaucesView: View {
    @Binding var addCutlery: Bool
    @Binding var addSauces: Bool
    
    var onDishSelected: (Bool) -> Void

    var body: some View {
        VStack(spacing: 16) {
            AddonItemView(isChecked: $addCutlery, text: "I want to add cutlery", onDishSelected: { bool in
                onDishSelected(bool)
            })
            AddonItemView(isChecked: $addSauces, text: "I want to add sauces", onDishSelected: { bool in
                onDishSelected(bool)
            })
        }
    }
}

struct AddonItemView: View {
    @Binding var isChecked: Bool
    @State private var isOn = false

    var text: String
    var onDishSelected: (Bool) -> Void

    var body: some View {
        HStack {
            Text(text)
                .font(.body)
                .lineLimit(1)
                .layoutPriority(1)
            Text("(1$)")
                .font(.headline)
                .foregroundColor(.blue)
            
            Toggle("", isOn: $isOn)
                .onChange(of: isOn) { newValue in
                    isChecked = newValue
                    onDishSelected(newValue)
                }
                .toggleStyle(SwitchToggleStyle(tint: .orange))
                .layoutPriority(0)
        }
    }
}


struct RestaurantDetailsContent: View {
    var details: RestaurantDetailsModel
    @Binding var showBasket: Bool
    @Binding var displayBasketDialog: Bool
    
    
    var onAddToBasketClick: () -> Void
    var replaceBasketItems: () -> Void
    var updateFavoriteState: (Bool) -> Void
    var onDishSelected: (Bool) -> Void
    var onResetBasketClick: (PizzaModel) -> Void

    @State private var selectedPizza: PizzaModel? = nil
    @State private var showSheet = false
    @State private var showBasketScreen = false

    var body: some View {
        ZStack {
            ScrollView {
                VStack(alignment: .leading) {
                    AsyncImage(url: URL(string: details.headerImageUrl)) { phase in
                        if let image = phase.image {
                            image
                                .resizable()
                                .frame(height: 220)
                                .clipped()
                        } else if phase.error != nil {
                            Color.gray
                                .frame(height: 220)
                        } else {
                            ProgressView()
                                .frame(height: 220)
                        }
                    }
                    
                    HStack {
                        VStack(alignment: .leading) {
                            Text(details.restName)
                                .font(.title)
                                .fontWeight(.bold)
                                .padding(.top, 8)
                            Text(details.restDescription)
                                .font(.body)
                                .padding(.bottom, 10)
                        }
                        Spacer()
                        Button(action: {
                            updateFavoriteState(!details.isFavourite)
                        }) {
                            Image(systemName: details.isFavourite ? "heart.fill" : "heart")
                                .foregroundColor(details.isFavourite ? .red : .gray)
                                .font(.title)
                        }
                    }
                    .padding(.horizontal, 16)
                    
                    ForEach(details.pizzaList, id: \.self) { pizza in
                        DetailsPizzaItemView(pizza: pizza, onItemClick: {
                            selectedPizza = pizza
                            onResetBasketClick(pizza)
                            showSheet.toggle()
                        })
                    }
                    .sheet(isPresented: Binding(
                        get: { showSheet },
                        set: { showSheet = $0 }
                    )) {
                        if let pizza = selectedPizza{
                            PizzaBottomSheetView(basketItemModel : details.basketItemModel,pizza: pizza,onDishSelected: onDishSelected) {
                                details.basketItemModel.dishImage = pizza.pizzaImageUrl
                                showSheet = false
                                onAddToBasketClick()
                          }
                        }
                    }
                }
            }

            if showBasket {
                FloatingActionButton {
                   showBasketScreen = true
                }
            }
        }
    
        .navigationDestination(isPresented: $showBasketScreen) {
               BasketScreen(
                basketViewModel: .init(),
                   onBackClick: {
                       showBasket = false
                   }
               )
           }
    
        .alert(isPresented: $displayBasketDialog) {
            Alert(
                title: Text(Strings().get(id: SharedResources.strings().replace_items_in_basket, args: [])),
                message: Text(Strings().get(id: SharedResources.strings().you_already_have_items_in_your_basket, args: [])),
                primaryButton: .destructive(Text("Yes"), action: {
                    replaceBasketItems()
                    displayBasketDialog = false
                }),
                secondaryButton: .cancel(Text("No"), action: {
                    displayBasketDialog = false
                })
            )
        }
    }
}
