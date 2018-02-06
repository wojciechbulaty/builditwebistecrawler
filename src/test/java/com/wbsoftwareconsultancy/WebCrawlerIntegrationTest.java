package com.wbsoftwareconsultancy;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WebCrawlerIntegrationTest {
    @Test
    @Ignore
    public void integrationTestOnARealWebsite() throws Exception {
        WebElement page = new WebCrawler().crawl("http://wiprodigital.com");

        assertThat(page.asString()).isEqualTo("sdfsfd");
    }
}
