package webcrawler.results;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import webcrawler.page.Page;

public class ResultsHolder {

	private String domain;
	private AtomicInteger crawledPagesCounter;
	private int crawledPagesCounterThreshold;

	private List<Page> synchronizedProcessedPagesList = Collections.synchronizedList(new LinkedList<>());
	private Set<String> synchronizedVisitedURLsSet = Collections.synchronizedSet(new HashSet<>());
	private BlockingQueue<String> unvisitedUrlsBlockingQueue = new LinkedBlockingQueue<String>();


	public ResultsHolder(int crawledPagesCounter, int crawledPagesCounterThreshold, String urlDomain) {
		this.crawledPagesCounter = new AtomicInteger(crawledPagesCounter);
		this.crawledPagesCounterThreshold = crawledPagesCounterThreshold;
		this.domain = urlDomain;

		// Instantiate the processed page list
		this.synchronizedProcessedPagesList = Collections.synchronizedList(new LinkedList<>());
		// Instantiate the set to import the unique URLs we have visited
		this.synchronizedVisitedURLsSet = Collections.synchronizedSet(new HashSet<>());
		// Instantiate the unvisited URLs queue
		this.unvisitedUrlsBlockingQueue = new LinkedBlockingQueue<String>();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public AtomicInteger getCrawledPagesCounter() {
		return crawledPagesCounter;
	}

	public void setCrawledPagesCounter(AtomicInteger crawledPagesCounter) {
		this.crawledPagesCounter = crawledPagesCounter;
	}

	public int getCrawledPagesCounterThreshold() {
		return crawledPagesCounterThreshold;
	}

	public void setCrawledPagesCounterThreshold(int crawledPagesCounterThreshold) {
		this.crawledPagesCounterThreshold = crawledPagesCounterThreshold;
	}

	public List<Page> getSynchronizedProcessedPagesList() {
		return synchronizedProcessedPagesList;
	}

	public Set<String> getSynchronizedVisitedURLsSet() {
		return synchronizedVisitedURLsSet;
	}

	public BlockingQueue<String> getUnvisitedUrlsBlockingQueue() {
		return unvisitedUrlsBlockingQueue;
	}
	
	public String getProcessedPagesStringInJsonFormat() {
		if (synchronizedProcessedPagesList.size() < 1) {
			return null;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<Page> listOfResults = synchronizedProcessedPagesList.stream().limit(crawledPagesCounterThreshold).collect(Collectors.toList());
		String json = gson.toJson(listOfResults);
		return json;
	}

	public void incrementCrawledPagesCounter() {
		this.crawledPagesCounter.incrementAndGet();
	}

}
