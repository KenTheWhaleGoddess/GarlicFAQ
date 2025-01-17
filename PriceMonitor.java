import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PriceMonitor {
    final ScheduledExecutorService executorService;

    static ChromeDriver driver;
    static WebDriverWait webDriverWait;

    // source URL and path for Summary data
    final static String source = "https://www.garlicwatch.com/";
    final static String summaryUrl = source + "api/summary";
    final static String marketCapUrl = source + "api/stats";

    //Price variables, printed in getPriceDescription()
    static String priceinusd;
    static int usdchange;

    static String priceinbtc;
    static int btcchange;

    static String volumegrlc;
    static int volumechange;

    static String volumeinusd;
    static int changeinvolumeusd;

    static String volumeinbtc;
    static int changeinvolumebtc;

    static String marketcapinbtc;
    static String marketcapinusd;
    static String supply;


    public PriceMonitor() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--window-size=2560,1440");

        driver = new ChromeDriver(chromeOptions);

        webDriverWait = new WebDriverWait(driver, 5);

        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                updateprice();
                updatemarketcap();
                writeImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }


    static void updateprice() throws IOException {
        HttpURLConnection url = (HttpURLConnection) new URL(summaryUrl).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
        String in = br.readLine();

        JSONObject obj = new JSONObject(in);
        priceinusd = obj.getString("last");
        usdchange = obj.getInt("last_change");

        priceinbtc = obj.getString("last_btc");
        btcchange = obj.getInt("last_btc_change");

        volumegrlc = obj.getString("volume24h");
        volumechange = obj.getInt("volume_change");

        volumeinusd = obj.getString("volume24h_usd");
        changeinvolumeusd = obj.getInt("volume_usd_change");

        volumeinbtc = obj.getString("volume24h_btc");
        changeinvolumebtc = obj.getInt("volume_btc_change");

    }
    private static void writeImage() throws IOException {
        driver.get(source);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div[2]/div/div/div[1]/div[2]/canvas")));

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("D:\\grlcfaq\\src\\main\\java\\garlicwatch.png"));
        BufferedImage imgscr = ImageIO.read(new File("D:\\grlcfaq\\src\\main\\java\\garlicwatch.png"));
        ImageIO.write(imgscr.getSubimage(16, 176, (1515 - 16), (662 - 176)), "png", new File("D:\\grlcfaq\\src\\main\\java\\chart.png"));


        driver.close();
    }

    private static void updatemarketcap() throws IOException {
        HttpURLConnection url = (HttpURLConnection) new URL(marketCapUrl).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
        String in = br.readLine();

        JSONObject obj = new JSONObject(in.replace("[", "").replace("]", ""));
        marketcapinbtc = obj.getString("mark_cap_btc");
        marketcapinusd = obj.getString("mark_cap_usd");
        supply = obj.getString("supply_form");
    }

    public String getPriceDescription() {
        return "**Average price (24hr)**\nUSD: $" + priceinusd + " (" + usdchange + "%)\nBTC: " + priceinbtc + " (" + btcchange + "%)" +
                "\n\n\n" +
                "**Volume (24hr)**\nGRLC: " + volumegrlc + " :garlic: (" + volumechange + "%)\nUSD: $" + volumeinusd + " (" + changeinvolumeusd + "%)\nBTC: " + volumeinbtc + " (" + changeinvolumebtc + "%)" +
                "\n\n\n**Market cap**\nGRLC: " + supply + " :garlic:\nUSD: $" + marketcapinusd + "\nBTC: " + marketcapinbtc;
    }

    public static int getusdchange(){
        return usdchange;
    }
}
