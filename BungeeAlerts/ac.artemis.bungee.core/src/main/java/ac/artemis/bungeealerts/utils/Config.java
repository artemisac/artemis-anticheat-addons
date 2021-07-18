package ac.artemis.bungeealerts.utils;

public interface Config {
    String getString(final String path);
    boolean getBoolean(final String path);
    int getInt(final String path);
}
