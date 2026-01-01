package guru.qa.niffler.model;

public class NewUser {
    String name;
    String password;
    String submitPassword;

    public NewUser(String name, String password, String submitPassword) {
        this.name = name;
        this.password = password;
        this.submitPassword = submitPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSubmitPassword() {
        return submitPassword;
    }
}