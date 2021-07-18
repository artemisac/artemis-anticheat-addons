package ac.artemis.bungeealerts.messaging;

import ac.artemis.anticheat.api.alert.Alert;

public interface MessageSerializer<T> {
    byte[] serialize(final T alert);

    T deserialize(final byte[] alert);
}
