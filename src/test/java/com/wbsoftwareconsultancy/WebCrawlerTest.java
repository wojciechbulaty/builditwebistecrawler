package com.wbsoftwareconsultancy;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class WebCrawlerTest {
    private static final int WIREMOCK_PORT = 9999;

    @Rule
    public WireMockRule wm = new WireMockRule(WireMockConfiguration.options().port(WIREMOCK_PORT));

    @Test
    public void crawlsASinglePageWhenNoLinksFound() throws Exception {
        wm.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<html><body>Hello world!</body></html>")));

        Page page = new WebCrawler().crawl("http://localhost:" + WIREMOCK_PORT);

        assertNotNull(page);
        assertThat(page.asString()).isEqualTo("Page http://localhost:" + WIREMOCK_PORT + "\n" +
                "External domain links: none\n");
    }

    @Test
    public void crawlsASubPageWithOneRelativeLink() throws Exception {
        wm.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<html><body><a href=\"contact.html\"></a></body></html>")));
        wm.stubFor(get(urlEqualTo("/contact.html"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<html><body>This is contact!</body></html>")));

        Page page = new WebCrawler().crawl("http://localhost:" + WIREMOCK_PORT);

        assertNotNull(page);
        assertThat(page.asString()).isEqualTo("Page http://localhost:" + WIREMOCK_PORT + "\n" +
                "External domain links: none\n" +
                "    Page http://localhost:" + WIREMOCK_PORT + "/contact.html\n" +
                "    External domain links: none\n");
    }

    @Test
    // The crawler should be limited to one domain.
    // Given a starting URL – say http://wiprodigital.com - it should visit all pages within the domain,
    // but not follow the links to external sites such as Google or Twitter.
    public void doesNotFollowExternalLinks() throws Exception {
        wm.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<html><body><a href=\"http://google.com\"></a><a href=\"http://twitter.com\"></a></body></html>")));

        Page page = new WebCrawler().crawl("http://localhost:" + WIREMOCK_PORT);

        assertNotNull(page);
        assertThat(page.asString()).isEqualTo("Page http://localhost:" + WIREMOCK_PORT + "\n" +
            "External domain links: http://google.com, http://twitter.com\n");
    }

    @Test
    @Ignore
    // The crawler should be limited to one domain.
    // Given a starting URL – say http://wiprodigital.com - it should visit all pages within the domain,
    // but not follow the links to external sites such as Google or Twitter.
    public void fetchesImages() throws Exception {
        wm.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("<html><body><a href=\"img.jpg\"></a></body></html>")));

        Page page = new WebCrawler().crawl("http://localhost:" + WIREMOCK_PORT);

        assertNotNull(page);
        assertThat(page.asString()).isEqualTo("Page http://localhost:" + WIREMOCK_PORT + "\n");
    }

    @Test
    public void supportsOnlyRootUrls() throws Exception {
        try {
            new WebCrawler().crawl("/aRelativeUrl");
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("The url should start with http");
        }
    }

    @Test
    @Ignore
    public void loadsTheSubPagesInParallel() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void skipsAnchorTagsWithNoHrefAttribute() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void handlesTimeoutsGracefully() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void reportsAnErrorOnFailedConnection() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void reportsInvalidExternalLinks() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void followsRelativeLinks() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void followsAbsoluteLinksToTheSameDomain() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void crawlsHttps() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void skipsSitesWithIncorrectHttpsCertificate() throws Exception {
        fail("TODO");
    }

    @Test
    @Ignore
    public void submitsFormsWithRandomDataToGetAccessToHiddenContent() throws Exception {
        fail("TODO");
    }
}
