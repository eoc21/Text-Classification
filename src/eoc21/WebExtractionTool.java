package eoc21;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


/**
 * Class that uses the webHarvester jar to extract content from the bbc news.
 * @author ed
 *
 */
public class WebExtractionTool {
	private String rssFeedURL;
	
	public WebExtractionTool(String feedURI){
		this.rssFeedURL = feedURI;
	}
	
	public void processFeed(String outputDirectory) throws IllegalArgumentException, FeedException, IOException{
        URL feedSource = new URL(rssFeedURL);	
        String currentdir = System.getProperty("user.dir");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        int counter=0;
        for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
	        SyndEntry entry = (SyndEntry) i.next();
	        String fileName = currentdir+outputDirectory+Integer.toString(counter)+".txt";
	        File file = new File(fileName);
	        String title = entry.getTitle();
	        String content = entry.getDescription().getValue();
	        FileUtils.writeStringToFile(file, title);
	        FileUtils.writeStringToFile(file, content);
	        counter++;
        }
	}
	
	
	
	public static void main(String[] args) throws IllegalArgumentException, FeedException, IOException{
		WebExtractionTool bbcNewsFeed = new WebExtractionTool("http://newsrss.bbc.co.uk/rss/newsonline_uk_edition/front_page/rss.xml");
		WebExtractionTool polymerFeed = new WebExtractionTool("http://www3.interscience.wiley.com/rss/journal/36444");
		WebExtractionTool polymerInternational = new WebExtractionTool("http://www3.interscience.wiley.com/rss/journal/5163");
		WebExtractionTool biopolymers = new WebExtractionTool("http://jbc.sagepub.com/rss/current.xml");
		bbcNewsFeed.processFeed("/Training/NewsText/NewsTr");
		System.out.println("Polymer feed");
		polymerFeed.processFeed("/Training/PolymerPapers/PolymerTr");
		polymerInternational.processFeed("/Training/PolymerPapers/PolymerTrInternational");
		biopolymers.processFeed("/Training/PolymerPapers/PolymerTrBio");
		//Test documents from RSS feed.
		WebExtractionTool timesNewsFeedWorld = new WebExtractionTool("http://feeds.timesonline.co.uk/c/32313/f/440158/index.rss");
		timesNewsFeedWorld.processFeed("/Testing/NewsText/World");
		WebExtractionTool newYorkTimesWorld =  new WebExtractionTool("http://feeds.nytimes.com/nyt/rss/World");
		newYorkTimesWorld.processFeed("/Testing/NewsText/NYWorld");
		WebExtractionTool newYorkTimesAfrica = new WebExtractionTool("http://feeds.nytimes.com/nyt/rss/Africa");
		newYorkTimesAfrica.processFeed("/Testing/NewsText/NYAfrica");
		WebExtractionTool polymersAdvancedTech = new WebExtractionTool("http://www3.interscience.wiley.com/rss/journal/5401");
		polymersAdvancedTech.processFeed("/Testing/PolymerPapers/PolyAdvTechnology");
		WebExtractionTool highPerformancePolymer = new WebExtractionTool("http://hip.sagepub.com/rss/current.xml");
		highPerformancePolymer.processFeed("/Testing/PolymerPapers/HighPerformancePoly");
		
	}
}
