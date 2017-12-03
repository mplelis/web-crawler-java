package webcrawler.util;

import org.apache.commons.validator.routines.UrlValidator;

public class URLValidator {

	public static String getFixedUrl(String URL) {
		if (URL==null || URL.isEmpty()) {
			return null;
		}
		if (!URL.startsWith("http")) {
			URL = "http://" + URL;
		}
		return URL;
	}
	
	public static boolean isURLValid(String URL) {
		String[] schemes = {"http", "https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		
		if (urlValidator.isValid(URL)) {
			return true;
		}
		return false;
	}
	
}
