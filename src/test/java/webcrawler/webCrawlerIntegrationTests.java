package webcrawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import webcrawler.concurrentscanner.ConcurrentScanner;
import webcrawler.pagecrawler.PageCrawler;
import webcrawler.pagecrawler.PageCrawlerImpl;
import webcrawler.pageprocessor.PageProcessor;
import webcrawler.pageprocessor.PageProcessorImpl;
import webcrawler.results.ResultsHolder;

public class webCrawlerIntegrationTests {

	@Test
	public void processingURLWithoutHttpProtocolPrefixShouldReturnFalse() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		boolean isProcessedURLValid = pageProcessor.processURL("www.bbc.com");
		assertFalse("\"www.bbc.com\" should return false", isProcessedURLValid);
		assertTrue(pageProcessor.getContainedLinks().isEmpty());
		assertEquals(0, pageProcessor.getResponseCode());
	}

	@Test
	public void processingURLWithHttpProtocolPrefixShouldReturnTrue() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		boolean isProcessedURLValid = pageProcessor.processURL("https://www.bbc.com/");
		assertTrue("\"https://www.bbc.com/\" should return true", isProcessedURLValid);
		assertNotNull(pageProcessor.getContainedLinks());
		assertEquals(200, pageProcessor.getResponseCode());
	}

	@Test
	public void crawlingValidURLShouldReturnANotNullJsonStringRepresentation()
			throws IOException, InterruptedException {
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10);
		PageCrawler pageCrawler = new PageCrawlerImpl(holder);
		holder.getUnvisitedUrlsBlockingQueue().add("https://www.bbc.com/");

		pageCrawler.scanPage();
		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}

	@Test
	public void crawlingInvalidURLShouldReturnANullJsonStringRepresentation() throws IOException, InterruptedException {
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10);
		PageCrawler pageCrawler = new PageCrawlerImpl(holder);
		holder.getUnvisitedUrlsBlockingQueue().add("https://www.bbccc.coom/");
		pageCrawler.scanPage();
		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNull("An invalid URL should return a Null json string representation.", json);
	}

	@Test
	public void crawlingValidURLShouldReturnANotNullJsonStringRepresentationUsingConcurrency()
			throws IOException, InterruptedException {
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10);
		holder.getUnvisitedUrlsBlockingQueue().add("https://www.bbc.com/");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		System.out.println("Crawling the following URLs\n");
		for (int i = 0; i < 10; i++) {
			executor.submit(new ConcurrentScanner(holder));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);

		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}

}
