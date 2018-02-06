# Clarification

I would not write this crawler in a real environment.
I would just probably use https://github.com/yasserg/crawler4j
or something similar.

I am assuming you want me to write some code, so I will write the crawler code
but I will use jsoup to help me with that.

# Building the project
```
mvn clean package
```
# Run the web crawler on Linux
```
mvn clean install
java -cp target/buildit-webiste-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar com.wbsoftwareconsultancy.WebCrawler http://wiprodigital.com
```
# What I would do next?

* Run the build on Windows
* Ask business if the sitemap should be flat or hierarchical
* Replace recursion with a work queue
* Discuss the API with the stakeholders to see if this tools is what they
would like to use.
* Implement sad-paths, e.g. what happens when the host does not respond or does not exist
* Refactor WebElement, it has got a few code smells
* Implement all the ignored tests in WebCrawlerTest (after consulting with the business)
* More real tests on real websites to find bugs
* Validation of command line input