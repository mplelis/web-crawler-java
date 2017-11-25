package webCrawler.PageProcessor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import webCrawler.Page.Page;

public interface PageProcessor {

	static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 "
			+ "(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	
	boolean processURL(String URL) throws IOException;
	List <String> getContainedLinks();
	Page getPage();
	int getResponseCode();

	public static int getURLResponseCode(String URL) throws IOException {
		if (URL==null || URL.isEmpty()) { 
			return 0;
		}
		URL u = new URL(URL);
		HttpURLConnection huc = (HttpURLConnection) u.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();
		
		int responseCode = huc.getResponseCode();
		return responseCode;
	}


	public static boolean isURLAccessible(String URL) throws IOException {
		int responseCode = getURLResponseCode(URL);
		if (responseCode >= 200 && responseCode < 400) {
			return true;
		}
		return false;
	}
}
