package webcrawler.pageprocessor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webcrawler.page.Page;
import webcrawler.page.PageImpl;
import webcrawler.pageprocessor.PageProcessor;

public class PageProcessorImpl implements PageProcessor {

	private Page page;
	private Document document;
	private List<String> containedLinks;
	private List<String> staticElementsList;
	private int responseCode=0;

	public PageProcessorImpl() {
		document = null;
		staticElementsList = new LinkedList<>();
		containedLinks = new LinkedList<>();
		page = new PageImpl();
	}

	public boolean processURL(String URL) throws IOException {
		try {
			page.setURL(URL);
			System.out.println("Thread: " + Thread.currentThread().getName() + " is processing URL : " + URL);
			
			Response response = Jsoup.connect(URL).timeout(1000).userAgent(USER_AGENT).execute();
			document = response.parse();
			responseCode = response.statusCode();	
			
			Elements media = document.select("[src]");
			Elements imports = document.select("link[href]");
			Elements links = document.select("a[href]");
			for (Element src : media) {
				staticElementsList.add(src.attr("abs:src"));
			}
			for (Element link : imports) {
				staticElementsList.add(link.attr("abs:href"));
			}

			page.setAssets(staticElementsList);
			
			for (Element link : links) {
				String containedLink = link.attr("abs:href");

				if (containedLink == null || containedLink.isEmpty()) {
					continue;
				}
				containedLinks.add(containedLink);
			}

		} catch (IOException ioe) {
			return false;
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<String> getContainedLinks() {
		return containedLinks;
	}

	public Page getPage() {
		return page;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
