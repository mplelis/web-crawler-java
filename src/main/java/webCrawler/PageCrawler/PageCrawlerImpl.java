package webCrawler.PageCrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import webCrawler.PageProcessor.PageProcessor;
import webCrawler.PageProcessor.PageProcessorImpl;
import webCrawler.URLValidator.URLValidator;
import webCrawler.Page.Page;
import webCrawler.PageCrawler.PageCrawler;

public class PageCrawlerImpl implements PageCrawler {

	private List<Page> processedPagesList;
	private Set<String> visitedURLsSet;
	private PageProcessor pageProcessor;
	private List<String> URLsToBeVisited;
	public static int crawledPagesCounter = -1;
	public static int crawledPagesCounterThreshold = 5;

	public PageCrawlerImpl() {
		// Instantiate the page list and the unprocessed URLs list
		this.processedPagesList = new LinkedList<>();
		// Instantiate the set to import the unique URLs we have visited
		this.visitedURLsSet = new HashSet<>();
		this.URLsToBeVisited = new LinkedList<>();
	}

	@Override
	public void scanPage(String URL) throws IOException {
		String domain = URL.split("/")[2];
		int responseCode;
		do {
			if (!URL.split("/")[2].equals(domain)) {
				URL = getNextURL();
				continue;
			}
			
			if (!visitedURLsSet.contains(URL)) {
				visitedURLsSet.add(URL);
			} else {
				URL = getNextURL();
				continue;
			}
			
			pageProcessor = new PageProcessorImpl();
			boolean isProcessValid = pageProcessor.processURL(URL);
			responseCode = pageProcessor.getResponseCode();

			if (!isProcessValid) {
				URL = getNextURL();
				continue;
			}
			if (responseCode == 301) {
				URL = getFinalURLAfterRedirects(URL);
			}
			if (crawledPagesCounter >= 0) {
				crawledPagesCounter++;
			}
			
			processedPagesList.add(pageProcessor.getPage());
			if (crawledPagesCounter >= 0) {
				if (crawledPagesCounter == crawledPagesCounterThreshold) {
					return;
				}
			}
			
			URLsToBeVisited.addAll(pageProcessor.getContainedLinks());
			URL = getNextURL();
		} while (!URLsToBeVisited.isEmpty());
	}

	private String getNextURL() {
		String URL = null;
		if (!URLsToBeVisited.isEmpty()) {
			URL = URLsToBeVisited.remove(0);
		}
		URL = URLValidator.getFixedUrl(URL);
		return URL;
	}

	@Override
	public String getProcessedPagesStringInJsonFormat() {
		if (processedPagesList.size() < 1) {
			return null;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(processedPagesList);
		return "Printing URL's Representation in JSON Format...\n\n" + json + "\n";
	}

	String getFinalURLAfterRedirects(String URL) throws IOException {
		if (URL==null) {
			return null;
		}
		Response response = Jsoup.connect(URL).followRedirects(true).execute();
		return response.url().toString();
	}
}
