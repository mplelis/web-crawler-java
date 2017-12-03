package webcrawler;

import static org.junit.Assert.*;

import webcrawler.pageprocessor.PageProcessor;
import webcrawler.pageprocessor.PageProcessorImpl;
import webcrawler.results.ResultsHolder;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class webCrawlerUnitTests {

	@Test
	public void newPageCrawlerObjectShouldReturnNullJsonStringRepresentation() throws IOException {
		ResultsHolder holder = new ResultsHolder(0, 0, "");
		String json = holder.getProcessedPagesStringInJsonFormat();
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
