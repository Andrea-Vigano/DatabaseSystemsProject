package commandparser;

import java.util.Objects;

public enum COMMAND_TYPES {
    ADD_PRODUCT("add-product"),
    ADD_CATEGORY("add-category"),
    ADD_SHIPPING_ADDRESS("add-shipping-address"),
    ADD_TO_CART("add-to-cart"),
    ADD_PROMOTION("add-promotion"),
    ADMIN_LOGIN("admin-login"),
    CHANGE_PASSWORD("change-password"),
    CHECKOUT("checkout"),
    DELETE_PRODUCT("delete-product"),
    DELETE_CATEGORY("delete-category"),
    DELETE_PROMOTION("delete-promotion"),
    DELETE_SHIPPING_ADDRESS("delete-shipping-address"),
    EXIT("exit"),
    GENERATE_REPORT("generate-report"),
    HELP("help"),
    LIST_PRODUCTS("list-products"),
    LIST_CATEGORY("list-category"),
    LIST_REPORTS("list-report"),
    LOGIN("login"),
    LOGOUT("logout"),
    SEARCH_PRODUCTS("search-product"),
    SHOW_CART("show-cart"),
    SHOW_PRODUCT("show-product"),
    SHOW_CATEGORY("show-category"),
    SHOW_SHIPPING_ADDRESSES("show-shipping-addresses"),
    SIGNUP("signup"),
    UPDATE_PRODUCT("update-product");

    private final String value;

    COMMAND_TYPES(String value) {
        this.value = value;
    }

    public Boolean isEqual(String commandType) {
        return Objects.equals(this.value, commandType);
    }
}
