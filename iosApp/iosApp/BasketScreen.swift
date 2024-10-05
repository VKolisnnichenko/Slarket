import SwiftUI
import shared


class BasketViewModelWrapper: ObservableObject {
    @Published var basketState: BasketState

    private let basketViewModel: BasketViewModel

    init() {
        let viewModelProider = ProvideBasketViewModel()
        basketViewModel = viewModelProider.provideViewModel() as! BasketViewModel
        basketState = basketViewModel.basketState.value

        startObserving()
    }

    @MainActor
    func fetchBasketItems() {
        basketViewModel.fetchBasketItems()
    }
    
    @MainActor
    func removeItem(by id: Int64) {
        basketViewModel.deleteBasketItem(id: id)
    }

    
    func startObserving() {
        Task {
            for await state in basketViewModel.basketState {
                DispatchQueue.main.async {
                    self.basketState = state
                }
            }
        }
    }
}

struct BasketScreen: View {
    @ObservedObject var basketViewModel: BasketViewModelWrapper
    var onBackClick: () -> Void
    
    init(basketViewModel: BasketViewModelWrapper, onBackClick: @escaping () -> Void) {
        self.basketViewModel = basketViewModel
        self.onBackClick = onBackClick
        basketViewModel.fetchBasketItems()
    }
    
    var body: some View {
        Group {
            switch basketViewModel.basketState {
            case is BasketStateLoading:
                BasketLoadingView()
            case let errorState as BasketStateError:
                BasketErrorView(message: errorState.message)
            case let successState as BasketStateSuccess:
                BasketSuccessScreen(basketPresentationModel: successState.basketPresentationModel,
                                    onDeleteClick: { itemId in
                                        basketViewModel.removeItem(by: itemId)
                                    },
                                    onBackClick: onBackClick)
            case is BasketStateEmpty:
                EmptyBasketScreen(onBackClick: onBackClick)
            default:
                EmptyView()
            }
        }.toolbar {
            ToolbarItem(placement: .principal) {
                Text(Strings().get(id: SharedResources.strings().basket, args: []))
                    .font(.headline)
                    .foregroundColor(.black)
            }
        }
    }
}

struct BasketSuccessScreen: View {
    
    @State private var basketItems: [BasketItemModel]
    var onDeleteClick: (Int64) -> Void
    var onBackClick: () -> Void
    var onContinueClick: () -> Void = {}
    

    init(
        basketPresentationModel: BasketPresentationModel,
        onDeleteClick: @escaping (Int64) -> Void,
        onBackClick: @escaping () -> Void,
        onContinueClick: @escaping () -> Void = {}
    ) {
        self._basketItems = State(initialValue: basketPresentationModel.basketItems)
        self.onDeleteClick = onDeleteClick
        self.onBackClick = onBackClick
        self.onContinueClick = onContinueClick
    }


    var body: some View {
        VStack {
            List {
                Section(header: TitleTextView(restName: {
                    if let firstBasketItem = basketItems.first,
                       let pizzaTitle = mockDataPizzaList.first(where: { $0.id == firstBasketItem.restId })?.title {
                        return pizzaTitle
                    } else {
                        return "Unknown Restaurant"
                    }
                }()).padding(.vertical, 8)) {
                    ForEach(Array(basketItems.enumerated()), id: \.offset) { index, item in
                        BasketItemCardView(basketItem: item)
                    }
                    .onDelete { indexSet in
                                         indexSet.forEach { index in
                                             let item = basketItems[index]
                                             deleteItem(item)
                                             onDeleteClick(Int64(item.id))
                                         }
                                     }
                }
            }
            .listStyle(PlainListStyle())

            VStack {
                HStack {
                    Text("Total:")
                        .font(.headline)
                    Spacer()
                    Text("$\(basketItems.reduce(0) { $0 + $1.sum })") // Calculate total sum
                        .font(.headline)
                        .foregroundColor(.orange)
                }
                .padding()

                Button(action: onContinueClick) {
                    Text("Continue")
                        .font(.body)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.orange)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                .padding()
            }
        }
    }
    

    private func deleteItem(_ item: BasketItemModel) {
        basketItems.removeAll { $0.id == item.id }
    }

}



struct TitleTextView: View {
    let restName: String

    var body: some View {
        Text("Selected dishes in the ")
            .foregroundColor(.black) +
        Text(restName)
            .font(.headline)
            .foregroundColor(.black)
            .bold() +
        Text(" restaurant")
            .foregroundColor(.black)
    }
}



struct BasketItemCardView: View {
    let basketItem: BasketItemModel
    @State private var expanded = false

    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                AsyncImage(url: URL(string: basketItem.dishImage)) { image in
                    image.resizable()
                        .frame(width: 64, height: 64)
                        .cornerRadius(8)
                } placeholder: {
                    ProgressView()
                }
                VStack(alignment: .leading) {
                    Text(basketItem.name)
                        .font(.headline)
                    Text("$\(basketItem.sum)")
                        .foregroundColor(.orange)
                }
                Spacer()
                Image(systemName: expanded ? "chevron.up" : "chevron.down")
            }
            if expanded {
                Text("Cutlery: \(basketItem.isCutlery ? "Yes" : "No")")
                Text("Sauces: \(basketItem.isSauces ? "Yes" : "No")")
                if !basketItem.userWishes.isEmpty {
                    Text("User Wishes: \(basketItem.userWishes)")
                }
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .cornerRadius(12)
        .contentShape(Rectangle()) // Make the entire area tappable
        .onTapGesture {
            expanded.toggle() 
        }
    }
}

struct EmptyBasketScreen: View {
    var onBackClick: () -> Void

    var body: some View {
        VStack {
            Spacer()
            VStack {
                Image(resource: \.empty_list)
                    .resizable()
                    .frame(width: 250, height: 250)
                
                Text(Strings().get(id: SharedResources.strings().empty_basket, args: []))
                    .multilineTextAlignment(.center)
                    .padding()
            }.padding(.bottom, 150)
            Spacer()
        }
        .padding()
    }
}

struct BasketErrorView: View {
    let message: String

    var body: some View {
        VStack {
            Spacer()
            Text(message)
                .foregroundColor(.red)
            Spacer()
        }
    }
}

struct BasketLoadingView: View {
    var body: some View {
        VStack {
            Spacer()
            ProgressView()
                .scaleEffect(2)
            Spacer()
        }
    }
}

