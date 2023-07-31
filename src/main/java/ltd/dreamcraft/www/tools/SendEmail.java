package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.Manager.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.bukkit.Bukkit.getConsoleSender;

public class SendEmail {
    public static void send(final String toAddress, final String code) {
        (new BukkitRunnable() {
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.put("mail.transport.protocol", "smtp");
                    properties.put("mail.smtp.host", ConfigManager.getEmailAddressIP());
                    properties.put("mail.smtp.port", ConfigManager.getEmailAddressPort());
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.ssl.enable", String.valueOf(ConfigManager.getEmailAddressSSL()));
                    //javaEmail Debug
                    properties.put("mail.debug", "false");
                    Session session = Session.getInstance(properties);
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(ConfigManager.getContentEmailAddress()));
                    message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toAddress)});
                    message.setSubject(ConfigManager.getContentEmailTitle());

                    // 读取HTML文件内容
                    String htmlContent = new String(Files.readAllBytes(Paths.get(ConfigManager.getHtmlFilePath())));

                    // 设置验证码
                    htmlContent = htmlContent.replace("%Email_Code%", code);

                    // 设置 HTML 内容
                    message.setContent(htmlContent, "text/html; charset=UTF-8");

                    Transport transport = session.getTransport();
                    transport.connect(ConfigManager.getEmailAuthAccount(), ConfigManager.getEmailAuthPassword());
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception var5) {
                    getConsoleSender().sendMessage(Lang.error("SMTP服务器无法连接，或者邮件无法发送"));
                    var5.printStackTrace();
                }
            }
        }).runTaskAsynchronously(DragonAuthMe.in());
    }
}

