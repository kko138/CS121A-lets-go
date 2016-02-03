//package ir.assignments.three;
//
//import edu.uci.ics.crawler4j.crawler.CrawlConfig;
//import edu.uci.ics.crawler4j.crawler.CrawlController;
//import edu.uci.ics.crawler4j.fetcher.PageFetcher;
//import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
//import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
//
//import java.sql.Connection;
//
///**
// * Created by Sophia,Cassie,Kevin on 1/23/2016. (And Sam!)
// * Class that contains the main. Sets up storage location, politeness delay, user agent string, and seed URL. Runs the crawler.
// */
//public class Controller {
//    public static void main(String[] args) throws Exception {
//        String crawlStorageFolder = "./crawlStorageFolder/";       //change to match whomever's computer we're using
//        int numberOfCrawlers = 7;
//
//        CrawlConfig config = new CrawlConfig();
//        config.setCrawlStorageFolder(crawlStorageFolder);
//        config.setPolitenessDelay(900);         // lets not get blacklisted :(
//        config.setUserAgentString("UCI Inf141-CS121 crawler 33196560 18923814 56956077 52478518");      //specified user agent string from Bidyuk
//
//        /*
//         * Instantiate the controller for this crawl.
//         */
//        PageFetcher pageFetcher = new PageFetcher(config);
//        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
//        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
//        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
//        config.setResumableCrawling(true);
//
//        /*
//         * For each crawl, you need to add some seed urls. These are the first
//         * URLs that are fetched and then the crawler starts following links
//         * which are found in these pages
//         */
//        //controller.addSeed("http://www.ics.uci.edu/~lopes/");
//        //controller.addSeed("http://www.ics.uci.edu/~welling/");
//        //controller.addSeed("http://www.ics.uci.edu/");
//        controller.addSeed("http://www.ics.uci.edu/");       //les do it
//
//        /*
//         * Start the crawl. This is a blocking operation, meaning that your code
//         * will reach the line after this only when crawling is finished.
//         */
//
//        controller.start(Crawler.class, numberOfCrawlers);
//    }
//}


// DON'T USE THIS