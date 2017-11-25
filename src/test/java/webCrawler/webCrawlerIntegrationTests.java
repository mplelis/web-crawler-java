package webCrawler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import webCrawler.PageCrawler.PageCrawler;
import webCrawler.PageCrawler.PageCrawlerImpl;
import webCrawler.PageProcessor.PageProcessor;
import webCrawler.PageProcessor.PageProcessorImpl;

public class webCrawlerIntegrationTests {

	@Test
	public void processingURLWithoutHttpProtocolPrefixShouldReturnFalse() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		boolean isProcessedURLValid = pageProcessor.processURL("www.bbc.com");
		assertFalse("\"www.bbc.com\" should return false",isProcessedURLValid);
		assertTrue(pageProcessor.getContainedLinks().isEmpty());
		assertEquals(0,pageProcessor.getResponseCode());
	}
	
	@Test
	public void processingURLWithHttpProtocolPrefixShouldReturnTrue() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		boolean isProcessedURLValid = pageProcessor.processURL("https://www.bbc.com/");
		assertTrue("\"https://www.bbc.com/\" should return true",isProcessedURLValid);
		assertNotNull(pageProcessor.getContainedLinks());
		assertEquals(200,pageProcessor.getResponseCode());
	}
	
	@Test
	public void crawlingValidURLShouldReturnANotNullJsonStringRepresentation() throws IOException {
		PageCrawler pageCrawler = new PageCrawlerImpl();
		// setting crawledPagesCounter to zero, to enable counting crawled pages.
		PageCrawlerImpl.crawledPagesCounter=0;
		// setting crawledPagesCounterThreshold to ten, to set the threshold
		// of crawled pages to a limit.
		PageCrawlerImpl.crawledPagesCounterThreshold=10;
		pageCrawler.scanPage("https://www.bbc.com/");
		String json = pageCrawler.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.",json);
	}
	
	@Test
	public void crawlingInvalidURLShouldReturnANullJsonStringRepresentation() throws IOException {
		PageCrawler pageCrawler = new PageCrawlerImpl();
		// setting crawledPagesCounter to zero, to enable counting crawled pages.
		PageCrawlerImpl.crawledPagesCounter=0;
		// setting crawledPagesCounterThreshold to ten, to set the threshold
		// of crawled pages to a limit.
		PageCrawlerImpl.crawledPagesCounterThreshold=10;
		pageCrawler.scanPage("https://www.bbccc.coom/");
		String json = pageCrawler.getProcessedPagesStringInJsonFormat();
		assertNull("An invalid URL should return a Null json string representation.",json);
	}

}
