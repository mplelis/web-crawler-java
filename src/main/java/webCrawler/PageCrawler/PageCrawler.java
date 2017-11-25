package webCrawler.PageCrawler;

import java.io.IOException;

public interface PageCrawler {
	void scanPage(String URL) throws IOException;
	String getProcessedPagesStringInJsonFormat();
}
