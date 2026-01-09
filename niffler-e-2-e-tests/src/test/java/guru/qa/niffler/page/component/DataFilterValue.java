package guru.qa.niffler.page.component;

public enum DataFilterValue {
    TODAY("TODAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    ALL("ALL");

    private final String displayName;

    DataFilterValue(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}