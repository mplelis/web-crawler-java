package webcrawler.pagecrawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import webcrawler.page.Page;
import webcrawler.pagecrawler.PageCrawler;
import webcrawler.pageprocessor.PageProcessor;
import webcrawler.pageprocessor.PageProcessorImpl;
import webcrawler.results.ResultsHolder;
import webcrawler.util.URLValidator;

public class PageCrawlerImpl implements PageCrawler {
	
	private List<Page> synchronizedProcessedPagesList;
	private Set<String> synchronizedVisitedURLsSet;
	private BlockingQueue<String> unvisitedUrlsBlockingQueue;
	
	private PageProcessor pageProcessor;
	
	private ResultsHolder resultsHolder;

	public PageCrawlerImpl(ResultsHolder resultsHolder) {
		this.resultsHolder=resultsHolder;
		populateReferences();
	}

	private void populateReferences() {
		synchronizedProcessedPagesList = resultsHolder.getSynchronizedProcessedPagesList();
		synchronizedVisitedURLsSet = resultsHolder.getSynchronizedVisitedURLsSet();
		unvisitedUrlsBlockingQueue = resultsHolder.getUnvisitedUrlsBlockingQueue();
	}

	@Override
	public void scanPage() throws IOException, InterruptedException {
		
		String URL;

		URL = unvisitedUrlsBlockingQueue.take();
		
		String domain = URL.split("/")[2];
		int responseCode;
		do {
			if (!URL.split("/")[2].equals(domain)) {
				URL = getNextURL();
				continue;
			}
			if (!synchronizedVisitedURLsSet.contains(URL)) {
				synchronizedVisitedURLsSet.add(URL);
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

			if (resultsHolder.getCrawledPagesCounter() >= 0) {
				resultsHolder.setCrawledPagesCounter(resultsHolder.getCrawledPagesCounter() + 1);
			}
			
			synchronizedProcessedPagesList.add(pageProcessor.getPage());

			unvisitedUrlsBlockingQueue.addAll(pageProcessor.getContainedLinks());
			URL = getNextURL();
		} while (!unvisitedUrlsBlockingQueue.isEmpty() && resultsHolder.getCrawledPagesCounter() < resultsHolder.getCrawledPagesCounterThreshold());
	}

	private String getNextURL() {
		String URL = null;

		if (!unvisitedUrlsBlockingQueue.isEmpty()) {
			try {
				URL = unvisitedUrlsBlockingQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		URL = URLValidator.getFixedUrl(URL);
		return URL;
	}

	String getFinalURLAfterRedirects(String URL) throws IOException {
		if (URL == null) {
			return null;
		}
		Response response = Jsoup.connect(URL).followRedirects(true).execute();
		return response.url().toString();
	}
}
