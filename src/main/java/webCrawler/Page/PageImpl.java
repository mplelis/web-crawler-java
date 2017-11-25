package webCrawler.Page;

import java.util.List;

import webCrawler.Page.Page;

public class PageImpl implements Page {

	String url;
	List<String> assets;

	@Override
	public void setURL(String URL) {
		this.url = URL;
	}

	@Override
	public void setAssets(List<String> assets) {
		this.assets = assets;
	}

}
