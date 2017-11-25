package webCrawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import webCrawler.PageCrawler.PageCrawler;
import webCrawler.PageCrawler.PageCrawlerImpl;
import webCrawler.PageProcessor.PageProcessor;
import webCrawler.PageProcessor.PageProcessorImpl;

public class webCrawlerUnitTests {

	@Test
	public void newPageCrawlerObjectShouldReturnNullJsonStringRepresentation() throws IOException {
		PageCrawler pageCrawler = new PageCrawlerImpl();
		String json = pageCrawler.getProcessedPagesStringInJsonFormat();
		assertNull(json);
	}
	
	@Test
	public void newPageProcessorObjectShouldReturnEmptyFieldValues() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		int responseCode = pageProcessor.getResponseCode();
		List<String> list = pageProcessor.getContainedLinks();
		assertEquals(0,responseCode);
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void processOfDummyURLShouldReturnFalse() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		boolean isProcessedURL = pageProcessor.processURL("dummy");
		assertFalse(isProcessedURL);
	}
	
}
