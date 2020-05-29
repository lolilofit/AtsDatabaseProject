package javafx_application.repository.tables;

import java.util.Map;

public interface Table {
    static String getRepresentativeString() {
        throw new UnsupportedOperationException();
    }

    String getMainString();

    static Map<String, Class> getTableParams() {
        throw new UnsupportedOperationException();
    }
}
