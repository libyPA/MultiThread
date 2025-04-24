import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class WebCrawler {
    private final ExecutorService executorService; //creating thread pool
    private final ConcurrentMap<String,Boolean> visitedUrls;
    private final int depthToCrawl;
    private final Logger logger;

    public WebCrawler(int numOfThreads, int depthToCrawl, Logger logger){
        this.executorService = Executors.newFixedThreadPool(numOfThreads); //assigning fixed number of threads to the pool.
        this.depthToCrawl = depthToCrawl;
        this.visitedUrls = new ConcurrentHashMap<>();
        this.logger = logger;
    }

    public void startCrawling(String startUrl){
            //System.out.println("inside startCrawling function");
            executorService.submit(new CrawlerWorker(startUrl,depthToCrawl,0,visitedUrls,executorService,logger));
            try { //time to spawn more tasks
                Thread.sleep(10000); // Wait 10 seconds (you can adjust)
            } catch (InterruptedException e) {
                logger.warning("Sleep interrupted");
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.severe("Crawling was Interrupted: " + e.getMessage());
            }
    }
}
