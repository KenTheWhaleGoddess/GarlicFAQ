import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class main extends ListenerAdapter {
    static JDA jda;
    static String prefix = "!";
    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault("your token here").enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES).setActivity(Activity.watching("!help")).addEventListeners(new main()).build();
    } // make sure your intents are enabled on discord developer if the bot is not working. Intents are enabled just in case this data is needed.

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(prefix)) {
            return;
        }

        String message = event.getMessage().getContentRaw().toLowerCase().substring(prefix.length());

        EmbedBuilder eb = new EmbedBuilder();

        switch (message) {
            case "help":
                eb.setTitle("Help");
                eb.setDescription("I am a meaningful and well architected FAQ Bot hosted on some person’s computer.\n" +
                        "\n" +
                        "Some core commands include: **!wallet**, **!garlicoin**, **!node**, **!howtobuy**, **!howtosell**, **!howtomine**\n" +
                        "\n" +
                        "[Read more](https://garlic.wiki/)");
                break;
            case "wallet":
            case "wallets":
                eb.setTitle("We have 3 main wallets, 2 offline and 1 online");
                eb.setDescription("[Garlicoin Core](https://garlicoin.io/downloads/) -- Recommended for most users. Requires 8-10 GB of space for the entire blockchain. for more info, check **!core**, **!coresync**\n" +
                        "[Garlium](https://xske.github.io/garlium/) for more info, check **!garlium**\n" +
                        "[Web wallet](https://grlc.eu/!w/) for more info, check **!webwallet**\n" +
                        "Paper wallet -- entirely offline for more info, check **!paperwallet**\n" +
                        "Mobile Wallet coming soon (Q2 2021)!\n\n[Read more](https://garlic.wiki/)");
                break;
            case "whatis":
            case "garlicoin":
                eb.setDescription("Garlicoin is hot out of the oven and ready to serve you with its buttery goodness.\n" +
                        "Forked from LTC, this decentralized cryptocurrency with memes backing its value will always be there for you.\n" +
                        "This is the coin you never thought you needed, and you probably don’t.\n\n[Read more](https://garlic.wiki/index.php/What_Is_Garlicoin)");
                break;
            case "node":
            case "garlicoinnode":
            case "fullnode":
                eb.setTitle("Why host a full node?");
                eb.setDescription("Build a snazzy Garlicoin application like a wallet or a faucet\n" +
                        "Bragging rights\n\n[Read more](https://garlic.wiki/index.php/How_To_Host_A_Garlicoin_Full_Node)");
                break;
            case "howtobuy":
            case "buy":
                eb.setTitle("So you wanna buy some garlic?");
                eb.setDescription("Buy LTC/DOGE at your favoured exchange\n" +
                        "Deposit those coins on [Frei](https://freiexchange.com/)\n" +
                        "Sell them on [Frei](https://freiexchange.com/) for BTC\n" +
                        "buy garlic\n" +
                        "Profit?\n\n[Read more](https://garlic.wiki/index.php/Buying_Garlicoin)");
                break;
            case "howtosell":
            case "sell":
                eb.setDescription("[Read more](https://garlic.wiki/index.php/Selling_Garlicoin_(for_Fiat))");
                break;
            case "revive":
            case "restore":
                eb.setDescription("[Read more](https://garlic.wiki/index.php/Reviving_Garlicoin_Wallet_(from_.dat_file))");
                break;
            case "why":
            case "whybuy":
            case "whomst":
            case "whomstbuy":
                eb.setDescription("Garlicoin has XYZ features and a fun community.(insert elevator pitch here)");
                break;

            case "wgrlc":
                eb.setDescription("WGRLC is a BEP20 tokenized version of GRLC on the Binance Smart Chain. Each is backed by 1 GRLC in escrow.");
                break;
            case "mining":
            case "mine":
            case "howtomine":
                eb.setDescription("All you need to mine is a GPU/CPU and electricity! Mining is when XYZ.\n\n[Read more](https://garlic.wiki/index.php/How_To_Mine)");
                break;
            case "instant":
            case "dailypayout":
            case "daily":
            case "instantpayout":
                eb.setDescription("Instant payout is evil.  Do not use it if you love garlic.");
                break;

            case "garlium":
                eb.setDescription("Garlium is a wallet that does not require downloading the entire blockchain, because a server monitors the blockchain on your behalf.");
                break;

            case "core":
                eb.setDescription("Garlicoin core is the safest online wallet because it downloads the entire blockchain (8-10 GB is required to use).");
                break;
            case "coresync":
                eb.setDescription("`./garlicoin-cli addnode freshgrlc.net onetry` or run `addnode freshgrlc.net onetry` in the console ");
                break;
            case "bork":
                try {
                    bork(event);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        event.getMessage().reply(eb.build()).mentionRepliedUser(false).queue();
    }

    static void bork(GuildMessageReceivedEvent event) throws IOException {
        HttpURLConnection url = (HttpURLConnection) new URL("https://dog.ceo/api/breeds/image/random").openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
        String in = br.readLine();

        JSONObject obj = new JSONObject(in);
        String dog_img = obj.getString("message");
        BufferedImage im = ImageIO.read(new File("D:\\grlcfaq\\src\\main\\java\\garlicoin.png"));
        BufferedImage im2 = ImageIO.read(new URL(dog_img));
        BufferedImage overlayedImage = overlayImages(im2, im);


        writeImage(overlayedImage, "D:\\grlcfaq\\src\\main\\java\\output.jpg", "JPG");
        event.getMessage().reply("bark!").addFile(new File("D:\\grlcfaq\\src\\main\\java\\output.jpg")).mentionRepliedUser(false).queue();
    }

    public static void writeImage(BufferedImage img, String fileLocation,
                                  String extension) {
        try {
            BufferedImage bi = img;
            File outputfile = new File(fileLocation);
            ImageIO.write(bi, extension, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static BufferedImage overlayImages(BufferedImage bgImage,
                                       BufferedImage fgImage) {

        /**
         * Doing some preliminary validations.
         * Foreground image height cannot be greater than background image height.
         * Foreground image width cannot be greater than background image width.
         *
         * returning a null value if such condition exists.
         */
        if (fgImage.getHeight() > bgImage.getHeight()
                || fgImage.getWidth() > fgImage.getWidth()) {
            return null;
        }

        /**Create a Graphics  from the background image**/
        Graphics2D g = bgImage.createGraphics();
        /**Set Antialias Rendering**/
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * Draw background image at location (0,0)
         * You can change the (x,y) value as required
         */
        g.drawImage(bgImage, 0, 0, null);

        /**
         * Draw foreground image at location (0,0)
         * Change (x,y) value as required.
         */
        g.drawImage(fgImage, bgImage.getWidth() - 128, bgImage.getHeight() - 128, null);

        g.dispose();
        return bgImage;
    }
}
