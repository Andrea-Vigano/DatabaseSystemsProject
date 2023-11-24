package commandparser;

import java.util.Objects;

public enum COMMAND_TYPES {
    LOGIN("login"),
    SIGNUP("signup"),
    LOGOUT("logout"),
    LIST("list"),
    SEARCH("search"),
    EXIT("exit");

    private final String value;

    COMMAND_TYPES(String value) {
        this.value = value;
    }

    public Boolean isEqual(String commandType) {
        return Objects.equals(this.value, commandType);
    }
}
