package ir.assignments.three;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.*;
//import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.parser.*;
//import edu.uci.ics.crawler4j.robotstxt.*;
//import edu.uci.ics.crawler4j.util.*;
import java.sql.*;

/**
 * Changelog 1/28/2016:
 *  - Added: HTML links are no longer written locally to project/data/...
 *  		 They are now written to a db -- install mysql!
 *	         Now crawls dynamic links
 *
 *  - Notes: crawl all subdomains (i don't know how to do this) -- jk got it
 *  		 urlLength is no longer needed :)
 *  		 We ready for prime time boys
 *  		 
 *  - TODO: not much
 *  - Bugs: probably some
 *  		(does robots.txt work? idk)
 *
 *
 * Changelog 1/24/2016:
 * 	- Added: HTML links are written locally to project/data/...
 * 			 Checks if folder names exist. If not, we create them.
 * 	- Notes: urlLength is HARD CODED. ex) for http://www.sharonypark.com, urlLength = 26 cuz 26 characters in that string.
 * 	- TODO: dynamically change urlLength without hardcoding.
 * 	- Bugs: probably some
 */

public class Crawler extends WebCrawler{
	static Connection connection;
	public static int iteration = 0;

	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	public static Collection<String> crawl(String seedURL) {
		// TODO implement me
		return null;
	}

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			+ "|png|mp3|mp3|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page
	 * in which we have discovered this new url and the second parameter is
	 * the new url. You should implement this function to specify whether
	 * the given url should be crawled or not (based on your crawling logic).
	 * In this example, we are instructing the crawler to ignore urls that
	 * have css, js, git, ... extensions and to only accept urls that start
	 * with "http://www.ics.uci.edu/". In this case, we didn't need the
	 * referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {		//need to work on this method, (dynamic links)
		String href = url.getURL().toLowerCase();
//		if(href.contains("?")) {
//			int posQ = href.indexOf("?");
//			href = href.substring(0, posQ);
//			//return false;
//		}
		return !FILTERS.matcher(href).matches()
				// changed to contains so we can crawl subdomains!
				&& href.contains("ics.uci.edu");
	}

	/**
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();


		System.out.println("URL: " + url);


		if (page.getParseData() instanceof HtmlParseData) {

			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			System.out.println("Number of websites crawled: " + ++iteration);

			try {
				String sql = "INSERT INTO data (url, html, textfile) VALUES (?, ?, ?);";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, url);
				ps.setString(2, html);
				ps.setString(3, text);
				ps.executeUpdate();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Opening connection to mysql database.
	 * @param user
	 * @param pw
	 * @throws SQLException
     */
	public void connectDB(String user, String pw) throws SQLException {
		this.connection = this.getSQLConnection(user, pw);
	}

	/**
	 * Getting connection from mysql database.
	 * @param user
	 * @param pw
	 * @return
	 * @throws SQLException
     */
	public static Connection getSQLConnection(String user, String pw) throws SQLException {
		return DriverManager.getConnection("jdbc:mysql:///crawldata?useSSL=false", user, pw);
	}

	public void execSql(String statement) throws SQLException{
		Statement select = this.getConnection().createStatement();

		select.execute(statement);
	}

	public Connection getConnection() {
		return this.connection;
	}

	public static void connect(String user, String password) throws SQLException {
		connection = getSQLConnection(user, password);
	}

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "./crawlStorageFolder/";       //change to match whomever's computer we're using
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(900);         // lets not get blacklisted :(
		config.setUserAgentString("UCI Inf141-CS121 crawler 33196560 18923814 56956077 52478518");      //specified user agent string from Bidyuk


		try {
			connect("root","1234");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        /*
         * Instantiate the controller for this crawl.
         */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		config.setResumableCrawling(true);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
		//controller.addSeed("http://www.ics.uci.edu/~lopes/");
		//controller.addSeed("http://www.ics.uci.edu/~welling/");
		//controller.addSeed("http://www.ics.uci.edu/");
		controller.addSeed("http://www.ics.uci.edu/");       //les do it

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

		controller.start(Crawler.class, numberOfCrawlers);
	}
}

