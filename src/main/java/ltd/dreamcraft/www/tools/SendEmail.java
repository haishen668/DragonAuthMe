package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.Manager.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {
    public static void send(final String toAdress, final String code) {
        (new BukkitRunnable() {
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.put("mail.transport.protocol", "smtp");
                    properties.put("mail.smtp.host", ConfigManager.getEmailAddressIP());
                    properties.put("mail.smtp.port", ConfigManager.getEmailAddressPort());
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.ssl.enable", String.valueOf(ConfigManager.getEmailAddressSSL()));
                    properties.put("mail.debug", "false");
                    Session session = Session.getInstance(properties);
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(ConfigManager.getContentEmailAddress()));
                    message.setRecipients(RecipientType.TO, new InternetAddress[]{new InternetAddress(toAdress)});
                    message.setSubject(ConfigManager.getContentEmailTitle());
                    message.setText(ConfigManager.getContentEmailText().replaceAll("%Email_Code%", code));
                    Transport transport = session.getTransport();
                    transport.connect(ConfigManager.getEmailAuthAccount(), ConfigManager.getEmailAuthPassword());
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception var5) {
                    var5.printStackTrace();
                }

            }
        }).runTaskAsynchronously(DragonAuthMe.in());
    }
}
