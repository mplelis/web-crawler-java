package webcrawler.page;

import java.util.List;

import webcrawler.page.Page;

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
