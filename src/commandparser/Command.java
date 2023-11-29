package commandparser;

public class Command {
    private String operation = "";

    public Command(String rawCommand) {
        if (!this.isExit()) {
            String[] components = rawCommand.split(" ");
            operation = components[0];
        }

    }

    private Boolean is(COMMAND_TYPES commandType) {
        return commandType.isEqual(this.operation);
    }

    public Boolean isLogin() {
        return this.is(COMMAND_TYPES.LOGIN);
    }

    public Boolean isSignup() {
        return this.is(COMMAND_TYPES.SIGNUP);
    }

    public Boolean isLogout() {
        return this.is(COMMAND_TYPES.LOGOUT);
    }

    public Boolean isChangePassword() {
        return this.is(COMMAND_TYPES.CHANGE_PASSWORD);
    }

    public Boolean isShowShippingAddress() {
        return this.is(COMMAND_TYPES.SHOW_SHIPPING_ADDRESSES);
    }

    public Boolean isAddShippingAddress() {
        return this.is(COMMAND_TYPES.ADD_SHIPPING_ADDRESS);
    }

    public Boolean isDeleteShippingAddress() {
        return this.is(COMMAND_TYPES.DELETE_SHIPPING_ADDRESS);
    }

    public Boolean isListProducts() {
        return this.is(COMMAND_TYPES.LIST_PRODUCTS);
    }

    public Boolean isSearchProducts() {
        return this.is(COMMAND_TYPES.SEARCH_PRODUCTS);
    }

    public Boolean isAdminLogin() {
        return this.is(COMMAND_TYPES.ADMIN_LOGIN);
    }

    public Boolean isAddProduct() {
        return this.is(COMMAND_TYPES.ADD_PRODUCT);
    }

    public Boolean isUpdateProduct() {
        return this.is(COMMAND_TYPES.UPDATE_PRODUCT);
    }

    public Boolean isDeleteProduct() {
        return this.is(COMMAND_TYPES.DELETE_PRODUCT);
    }

    public Boolean isShowProduct() {
        return this.is(COMMAND_TYPES.SHOW_PRODUCT);
    }

    public Boolean isAddToCart() {
        return this.is(COMMAND_TYPES.ADD_TO_CART);
    }

    public Boolean isShowCart() {
        return this.is(COMMAND_TYPES.SHOW_CART);
    }

    public Boolean isCheckout() {
        return this.is(COMMAND_TYPES.CHECKOUT);
    }

    public Boolean isGenerateReport() {
        return this.is(COMMAND_TYPES.GENERATE_REPORT);
    }

    public Boolean isHelp() {
        return this.is(COMMAND_TYPES.HELP);
    }

    public Boolean isExit() {
        return this.is(COMMAND_TYPES.EXIT);
    }
}
