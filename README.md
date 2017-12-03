# Web Crawler
### Author : Mihalis Plelis

The concurrent web crawler, crawls URLs and for each page, it determines the URLs of every static asset (images, javascript, stylesheets) on that page. By default, a thread pool of 10 threads is used.
The crawler outputs to STDOUT in JSON format listing the URLs of every static asset, grouped by page.

Example:  
```json
[
  {
    "url": "http://www.example.org",
    "assets": [
      "http://www.example.org/image.jpg",
      "http://www.example.org/script.js"
    ]
  },
  {
    "url": "http://www.example.org/about",
    "assets": [
      "http://www.example.org/company_photo.jpg",
      "http://www.example.org/script.js"
    ]
  }
]
```

### Running the web crawler

1. Under the target directory of the project, the file **webCrawler.jar** is generated after maven builds the project with the command **clean package**.
2. In order to execute that jar file, it has to be called from command line, using the **java -jar** command.
3. It can receive two arguments. The first one is **-cl** and the second one is a number.  
**-cl** stands for crawl limit and when it is used, it sets a limit for the pages to be crawled and this number is parsed from the second argument which is given to the program.  
If no arguments are given, then the web crawler, will crawl all the URLs till there are no more left.

- Examples:  
	* java -jar webCrawler.jar -cl 10  
	* java -jar webCrawler.jar  

4. When the application is executed, it is asking for a valid URL to be given, in order to start crawling.