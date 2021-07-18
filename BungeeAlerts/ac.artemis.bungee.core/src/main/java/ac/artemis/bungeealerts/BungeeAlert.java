package ac.artemis.bungeealerts;

import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.alert.Severity;
import ac.artemis.anticheat.api.check.CheckInfo;
import ac.artemis.anticheat.api.check.type.Stage;
import ac.artemis.anticheat.api.check.type.Type;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class BungeeAlert {
    @SerializedName("server")
    private String server;

    @SerializedName("alert")
    private MessageAlert alert;

    public BungeeAlert() {
    }

    public BungeeAlert(String server, Alert alert) {
        this.server = server;

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

        this.setAlert(messageAlert);
    }

    @Data
    public static class MessageAlert implements Alert {
        @SerializedName("severity")
        private Severity severity;

        @SerializedName("uuid")
        private UUID uuid;

        @SerializedName("checkInfo")
        private MessageCheckInfo check;

        @SerializedName("count")
        private int count;

        @SerializedName("cancelled")
        private boolean cancelled;

        @SerializedName("minecraftMessage")
        private String minecraftMessage;

        @Override
        public int count() {
            return count;
        }

        @Override
        public String toMinecraftMessage() {
            return minecraftMessage;
        }
    }

    @Data
    public static class MessageCheckInfo implements CheckInfo {
        @SerializedName("type")
        private Type type;

        @SerializedName("var")
        private String var;

        @SerializedName("visualCategory")
        private String visualCategory;

        @SerializedName("visualName")
        private String visualName;

        @SerializedName("noDrop")
        private boolean noDrop;

        @SerializedName("stage")
        private Stage stage;

        @SerializedName("maxVb")
        private int maxVb;

        @SerializedName("delayBetweenAlerts")
        private int delayBetweenAlerts;

        @SerializedName("maxVl")
        private int maxVl;

        @SerializedName("enabled")
        private boolean enabled;

        @SerializedName("bannable")
        private boolean bannable;

        @SerializedName("setback")
        private boolean setback;

        @SerializedName("compatibleNMS")
        @Deprecated
        private boolean compatibleNMS;

        @Override
        public void save() {
            // Do nothing
        }
    }
}