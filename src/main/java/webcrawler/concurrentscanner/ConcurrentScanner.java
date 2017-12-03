package webcrawler.concurrentscanner;

import java.io.IOException;

import webcrawler.pagecrawler.PageCrawler;
import webcrawler.pagecrawler.PageCrawlerImpl;
import webcrawler.results.ResultsHolder;

public class ConcurrentScanner implements Runnable {

	private ResultsHolder resultsHolder;
	
	public ConcurrentScanner(ResultsHolder resultsHolder) {
		super();
		this.resultsHolder = resultsHolder;
	}

	@Override
	public void run() {
		PageCrawler pageCrawler = new PageCrawlerImpl(resultsHolder);
		try {
			pageCrawler.scanPage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
