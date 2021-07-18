package ac.artemis.bungeealerts.provider;

import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.bungeealerts.BungeeAlert;

public interface AlertProvider {
    void receiveAlert(final BungeeAlert alert);
}
