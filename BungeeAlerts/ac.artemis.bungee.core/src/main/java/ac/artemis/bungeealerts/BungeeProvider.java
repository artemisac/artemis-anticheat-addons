package ac.artemis.bungeealerts;

import ac.artemis.bungeealerts.provider.AlertProvider;
import ac.artemis.bungeealerts.utils.Config;

public interface BungeeProvider {
    AlertProvider provider();
    Config getConfig(final String name);
    String getName();
}
