package webcrawler;

import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import webcrawler.concurrentscanner.ConcurrentScanner;
import webcrawler.pageprocessor.PageProcessor;
import webcrawler.pageprocessor.PageProcessorImpl;
import webcrawler.results.ResultsHolder;
import webcrawler.util.URLValidator;

public class Application {

	public static void main(String[] args) throws IOException {
		System.out.println("Web Crawling Application has started!\n");
		int crawledPagesCounter = -1, crawledPagesCounterThreshold = 10;
		try {
			if (args[0] != null || !args[0].isEmpty() && args[1] != null || !args[1].isEmpty()) {
				if (args[0].equals("-cl") && Integer.parseInt(args[1]) > 0) {
					crawledPagesCounter = 0;
					crawledPagesCounterThreshold = Integer.valueOf(args[1]);
					System.out.println("The Application will crawl the first " + Integer.valueOf(args[1]) + " pages.\n");
				} else {
					System.out.println("The Application will crawl to the end.\n");
				}
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Invalid Arguments were provided.\n" + "The Application will crawl to the end.\n");
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
					if (responseCode >= 200 && responseCode < 400) {
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

		String urlDomain = userInputURL.split("/")[2];
		
		long timeStampMillisStart = Instant.now().toEpochMilli();
		final ResultsHolder resultsHolder = new ResultsHolder(crawledPagesCounter, crawledPagesCounterThreshold,
				urlDomain);
		resultsHolder.getUnvisitedUrlsBlockingQueue().add(userInputURL);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			System.out.println("Crawling URLs now...\n");
			IntStream.range(0,10).forEach(i -> executor.submit(new ConcurrentScanner(resultsHolder)));

			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);

			long timeStampMillisEnd = Instant.now().toEpochMilli();
			String resultsJsonRepresentation = resultsHolder.getProcessedPagesStringInJsonFormat();
			System.out.println("Printing URL's Representation in JSON Format...\n\n" + resultsJsonRepresentation + "\n");
			System.out.println("Took " + (timeStampMillisEnd - timeStampMillisStart) + " milliseconds");
		} catch (Exception e) {
			System.out.println("Unfortunately something went wrong.\n" + e.getMessage());
			e.printStackTrace();
		}

	}
}