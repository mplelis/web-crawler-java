package webcrawler.page;

import java.util.List;

public interface Page {
	void setURL(String URL);
	void setAssets(List<String> assets);
}
