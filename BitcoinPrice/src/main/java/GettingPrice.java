import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GettingPrice {

    public static final String USD_BIT_URL = "https://api.coinbase.com/v2/prices/spot?currency=USD";

    public static void main(String[] args) {
        while (true) {
            try {
                String htmlPage = getHTMLbyUrl(USD_BIT_URL);
                Double price = null;
                Pattern pattern = Pattern.compile("\"([\\d\\.]+?)\"");

                Matcher matcher = pattern.matcher(htmlPage);
                while (matcher.find()) {
                    price = Double.parseDouble(matcher.group(1));
                }
                System.out.println(price);

                if (price < 42_000) {
                    if (SystemTray.isSupported()) {
                        SystemTray tray = SystemTray.getSystemTray();

                        java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/tray.gif");
                        TrayIcon trayIcon = new TrayIcon(image);
                        tray.add(trayIcon);
                        trayIcon.displayMessage("Bitcoin", "1 Bitcoin = " + price + " USD",
                                TrayIcon.MessageType.INFO);
                    }
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String getHTMLbyUrl(String url) throws IOException {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        StringBuilder sb = new StringBuilder("");
        while (in.ready()) {
            sb.append(in.readLine() + '\n');
        }
        return sb.toString();
    }

}
