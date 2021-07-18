package ac.artemis.bungeealerts.messaging;

import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.alert.Severity;
import ac.artemis.anticheat.api.check.CheckInfo;
import ac.artemis.anticheat.api.check.type.Stage;
import ac.artemis.anticheat.api.check.type.Type;
import ac.artemis.bungeealerts.BungeeAlert;
import ac.artemis.bungeealerts.BungeeProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class SimpleBase64Serializer implements MessageSerializer<BungeeAlert> {
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    @Override
    @SneakyThrows
    public byte[] serialize(BungeeAlert bungeeAlert) {
        final Alert alert = bungeeAlert.getAlert();
        final CheckInfo checkInfo = alert.getCheck();
        final BungeeAlert.MessageCheckInfo messageCheckInfo = new BungeeAlert.MessageCheckInfo();
        messageCheckInfo.setBannable(checkInfo.isBannable());
        messageCheckInfo.setEnabled(checkInfo.isEnabled());
        messageCheckInfo.setMaxVb(checkInfo.getMaxVb());
        messageCheckInfo.setMaxVl(checkInfo.getMaxVl());
        messageCheckInfo.setDelayBetweenAlerts(checkInfo.getDelayBetweenAlerts());
        messageCheckInfo.setNoDrop(checkInfo.isNoDrop());
        messageCheckInfo.setType(checkInfo.getType());
        messageCheckInfo.setStage(checkInfo.getStage());
        messageCheckInfo.setVar(checkInfo.getVar());
        messageCheckInfo.setVisualCategory(checkInfo.getVisualCategory());
        messageCheckInfo.setVisualName(checkInfo.getVisualName());
        messageCheckInfo.setCompatibleNMS(true);

        final BungeeAlert.MessageAlert messageAlert = new BungeeAlert.MessageAlert();
        messageAlert.setMinecraftMessage(alert.toMinecraftMessage());
        messageAlert.setCheck(messageCheckInfo);
        messageAlert.setCount(alert.count());
        messageAlert.setCancelled(alert.isCancelled());
        messageAlert.setSeverity(alert.getSeverity());
        messageAlert.setUuid(alert.getUuid());

        bungeeAlert.setAlert(messageAlert);

        final String json = gson.toJson(bungeeAlert, BungeeAlert.class);
        final byte[] base64 = Base64.getEncoder().encode(json.getBytes(StandardCharsets.UTF_8));

        return base64;
    }

    @Override
    public BungeeAlert deserialize(byte[] alert) {
        final String decoded = new String(Base64.getDecoder().decode(alert));
        return gson.fromJson(decoded, BungeeAlert.class);
    }


}
