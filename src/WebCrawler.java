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
        try{
            //System.out.println("inside startCrawling function");
            executorService.submit(new CrawlerWorker(startUrl,depthToCrawl,0,visitedUrls,executorService,logger));
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e){
            logger.severe("Crawling was Interrupted: "+e.getMessage());
        }
    }
}
