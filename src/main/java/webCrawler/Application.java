package webCrawler;

import java.io.IOException;
import java.util.Scanner;

import webCrawler.PageCrawler.PageCrawler;
import webCrawler.PageCrawler.PageCrawlerImpl;
import webCrawler.PageProcessor.PageProcessor;
import webCrawler.PageProcessor.PageProcessorImpl;
import webCrawler.URLValidator.URLValidator;

public class Application {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Web Crawling Application has started!\n");
		try {
			if (args[0] != null || !args[0].isEmpty() && args[1] != null || !args[1].isEmpty()) {
				if (args[0].equals("-cl") && Integer.parseInt(args[1]) > 0) {
					PageCrawlerImpl.crawledPagesCounter = 0;
					PageCrawlerImpl.crawledPagesCounterThreshold = Integer.valueOf(args[1]);
					System.out.println("The Application will crawl the first "
							+ PageCrawlerImpl.crawledPagesCounterThreshold + " pages.\n");
				} else {
					System.out.println("The Application will crawl to the end.\n");
				}
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Invalid Arguments were provided.\n" + 
					"The Application will crawl to the end.\n");
		}
		
		String userInputURL;
		// scan for user input URL
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter a valid URL:");
		// scan for user input URL until it's valid, with 2xx/3xx HTTP Response Code
		while (true) {
			userInputURL = sc.next();
			// check if the provided URL is not empty
			if (!userInputURL.isEmpty()) {
				// validate that the URL contains the HTTP protocol as prefix,
				// otherwise attach it as prefix
				userInputURL = URLValidator.getFixedUrl(userInputURL);
				if (!URLValidator.isURLValid(userInputURL)) {
					System.out.println("Invalid URL, please try again...");
					continue;
				}
				try {
					PageProcessor pageProcessor = new PageProcessorImpl();
					pageProcessor.processURL(userInputURL);
					int responseCode = pageProcessor.getResponseCode();
					if (responseCode>=200 && responseCode<400) {
						System.out.println("Accessible URL, proceeding with web crawling...\n");
						break;
					} else {
						System.out.println("Inaccessible URL, please try again...");
					}
				} catch (Exception e) {
					System.out.println("Inaccessible URL, please try again...");
				}
			} else {
				System.out.println("Please enter a URL.");
			}
		}
		sc.close();
		
		PageCrawler pageScanner = new PageCrawlerImpl();
		try {
			System.out.println("Crawling the following URLs\n");
			pageScanner.scanPage(userInputURL);
			System.out.println("\n" + pageScanner.getProcessedPagesStringInJsonFormat());
		} catch (Exception e) {
			System.out.println("Unfortunately something went wrong.\n" + e.getMessage());
			e.printStackTrace();
		}

	}
}