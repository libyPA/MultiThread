import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CrawlerWorker implements Runnable{
    private final String url;
    private final int depthToCrawl;
    private final int currentDepth;
    private final ConcurrentMap<String, Boolean> visitedUrls;
    private final ExecutorService executorService;
    private final Logger logger;
    private static final AtomicInteger crawledCount = new AtomicInteger(0);

    public CrawlerWorker(String url, int depthToCrawl, int currentDepth, ConcurrentMap<String,Boolean> visitedUrls,ExecutorService executorService, Logger logger){
        this.url = url;
        this.depthToCrawl = depthToCrawl;
        this.currentDepth = currentDepth;
        this.visitedUrls = visitedUrls;
        this.executorService = executorService;
        this.logger = logger;
        //System.out.println("insideCrawlerWorkerConstructor");
    }

    @Override
    public void run(){ //To implement Runnable make the class abstract or override run().
        if(currentDepth > depthToCrawl || visitedUrls.putIfAbsent(url,true)!=null) {
            return;
        }
        System.out.println(Thread.currentThread().getName() + " crawling: " + url);
            logger.info("Crawling at depth "+currentDepth);
            //System.out.println("inside run");
            //writing url to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("urlOutput.txt",true))){
                writer.write(url);
                writer.newLine();
            } catch (IOException e) {
                logger.warning("Failed to write url "+url);
            }
            //Extract links from current page
            List<String> links = Utils.extractLinks(url);
            /*System.out.println("returned links "+links.size());
            for(String link:links){
                System.out.println(link);
            }*/
            for(String link:links){
                executorService.submit(new CrawlerWorker(link,depthToCrawl,currentDepth+1,visitedUrls,executorService,logger));
            }
            //log the number of crawled pages.
            if(crawledCount.incrementAndGet()%10==0){
                logger.info(crawledCount.get()+ " pages crawled so far!");
            }
        }
}

