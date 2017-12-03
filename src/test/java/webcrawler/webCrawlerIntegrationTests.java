package webcrawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
		String url = "www.bbc.com";
		boolean isProcessedURLValid = pageProcessor.processURL(url);
		assertFalse("\"" + url + "\" should return false", isProcessedURLValid);
		assertTrue(pageProcessor.getContainedLinks().isEmpty());
		assertEquals(0, pageProcessor.getResponseCode());
	}

	@Test
	public void processingURLWithHttpProtocolPrefixShouldReturnTrue() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		String url = "https://www.bbc.com/";
		boolean isProcessedURLValid = pageProcessor.processURL(url);
		assertTrue("\"" + url + "\" should return true", isProcessedURLValid);
		assertNotNull(pageProcessor.getContainedLinks());
		assertEquals(200, pageProcessor.getResponseCode());
	}

	@Test
	public void crawlingValidURLShouldReturnANotNullJsonStringRepresentation()
			throws IOException, InterruptedException {
		String url = "https://www.bbc.com/";
		String urlDomain = url.split("/")[2];
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10, urlDomain);
		PageCrawler pageCrawler = new PageCrawlerImpl(holder);
		holder.getUnvisitedUrlsBlockingQueue().add(url);
		pageCrawler.scanPage();
		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}

	@Test
	public void crawlingInvalidURLShouldReturnANullJsonStringRepresentation() throws IOException, InterruptedException {
		String url = "https://www.bbccc.coom/";
		String urlDomain = url.split("/")[2];
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10, urlDomain);
		PageCrawler pageCrawler = new PageCrawlerImpl(holder);
		holder.getUnvisitedUrlsBlockingQueue().add(url);
		pageCrawler.scanPage();
		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNull("An invalid URL should return a Null json string representation.", json);
	}

	@Test
	public void crawlingValidURLShouldReturnANotNullJsonStringRepresentationUsingConcurrency()
			throws IOException, InterruptedException {
		String url = "https://www.bbc.com/";
		String urlDomain = url.split("/")[2];
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		ResultsHolder holder = new ResultsHolder(0, 10, urlDomain);
		holder.getUnvisitedUrlsBlockingQueue().add(url);

		ExecutorService executor = Executors.newFixedThreadPool(3);

		IntStream.range(0, 3).forEach(i -> executor.submit(new ConcurrentScanner(holder)));

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.MINUTES);

		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}

}
