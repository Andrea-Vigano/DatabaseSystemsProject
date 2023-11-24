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

    public Boolean isList() {
        return this.is(COMMAND_TYPES.LIST);
    }

    public Boolean isSearch() {
        return this.is(COMMAND_TYPES.SEARCH);
    }

    public Boolean isAdminLogin() {
        return this.is(COMMAND_TYPES.ADMIN_LOGIN);
    }

    public Boolean isAddProduct() {
        return this.is(COMMAND_TYPES.ADD_PRODUCT);
    }

    public Boolean isExit() {
        return this.is(COMMAND_TYPES.EXIT);
    }
}
