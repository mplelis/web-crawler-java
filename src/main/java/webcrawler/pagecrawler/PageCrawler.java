package webcrawler.pagecrawler;

import java.io.IOException;

public interface PageCrawler {
	void scanPage() throws IOException, InterruptedException;
}
