package commandparser;

import java.util.Objects;

public enum COMMAND_TYPES {
    LOGIN("login"),
    SIGNUP("signup"),
    LOGOUT("logout"),
    CHANGE_PASSWORD("change-password"),
    SHOW_SHIPPING_ADDRESSES("show-shipping-addresses"),
    ADD_SHIPPING_ADDRESS("add-shipping-address"),
    DELETE_SHIPPING_ADDRESS("delete-shipping-address"),
    LIST_PRODUCTS("list-products"),
    SEARCH_PRODUCTS("search-product"),
    ADMIN_LOGIN("admin-login"),
    ADD_PRODUCT("add-product"),
    DELETE_PRODUCT("delete-product"),
    UPDATE_PRODUCT("update-product"),
    SHOW_PRODUCT("show-product"),
    ADD_TO_CART("add-to-cart"),
    SHOW_CART("show-cart"),
    CHECKOUT("checkout"),
    GENERATE_REPORT("generate-report"),
    HELP("help"),
    EXIT("exit");

    private final String value;

    COMMAND_TYPES(String value) {
        this.value = value;
    }

    public Boolean isEqual(String commandType) {
        return Objects.equals(this.value, commandType);
    }
}
