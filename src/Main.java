import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        String startUrl = "https://www.youtube.com/";
        int depthToCrawl = 2;
        int numOfThreads = 4;
        WebCrawler crawler = new WebCrawler(numOfThreads,depthToCrawl,logger);
        //System.out.println("crawlerstart");
        crawler.startCrawling(startUrl);
    }
}
