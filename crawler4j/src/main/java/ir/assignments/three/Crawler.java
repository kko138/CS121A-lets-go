/**
 * authors: Cassie Jeansonne 18923914, Kevin Ko 56956077, Samuel Lin 52478518, Sophia Chan 33196560
 */
package ir.assignments.three;

import java.sql.SQLException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.*;
import edu.uci.ics.crawler4j.parser.*;
import java.sql.*;

/**
 * Changelog 2/6/2016
 * - Added: More comments
 * 			Changed Analyze.java output to only 500
 * - Notes: none
 * - TODO: turn in to checkmate
 * - Bugs: probably but we're turning it in anyway :o
 *
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
	 *
	 * We used regex to get the string from first instance of // to first instance of /
	 * @param referringPage, url
	 * @return boolean   (representing whether page should be visited or not)
	 *
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		String regex = "\\/\\/(.*?)\\/";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(href);

		if(m.find())
		{

			if(m.group(1).contains("duttgroup"))
				return false;
			else if(m.group(1).equals("ics.uci.edu"))
				return !FILTERS.matcher(href).matches();
			else if(m.group(1).equals("www.ics.uci.edu"))
				return !FILTERS.matcher(href).matches();
			else if(m.group(1).contains(".ics.uci.edu"))
				return !FILTERS.matcher(href).matches();

			else
				return false;

		}

		return false;
	}

	/**
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 * @param page
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

			//puts into database
			try {
				String sql = "INSERT INTO data (url, html, textfile) VALUES (?, ?, ?);";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, url);
				ps.setString(2, html);
				ps.setString(3, text);
				ps.executeUpdate();

			}
			catch (Exception e) {
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

	/**
	 * Gets sql connection
	 * @return connection
     */
	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Calls getSQLConnection (modularization!)
	 * @param user
	 * @param password
	 * @throws SQLException
     */
	public static void connect(String user, String password) throws SQLException {
		connection = getSQLConnection(user, password);
	}

	/**
	 * Runs the crawler
	 * @param args
	 * @throws Exception
     */
	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "./crawlStorageFolder/";
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(900);
		config.setResumableCrawling(true);
		config.setMaxDownloadSize(100000000);
		config.setUserAgentString("UCI Inf141-CS121 crawler 33196560 18923814 56956077 52478518");      //specified user agent string from Bidyuk


		try {
			connect("root","1234"); //connect to mysql database using mysql password and root
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


        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
		controller.addSeed("http://www.ics.uci.edu/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

		controller.start(Crawler.class, numberOfCrawlers);
	}
}
