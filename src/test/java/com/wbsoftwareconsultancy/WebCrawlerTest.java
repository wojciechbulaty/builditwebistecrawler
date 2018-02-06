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

        SiteMap siteMap = new WebCrawler().crawl("http://localhost" + WIREMOCK_PORT);

        assertNotNull(siteMap);
        assertThat(siteMap.asString()).isEqualTo("Page: http://localhost" + WIREMOCK_PORT +
                "    Internal links: none\n" +
                "    External links: none\n" +
                "    Static content links: none");
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
    // The crawler should be limited to one domain.
    // Given a starting URL – say http://wiprodigital.com - it should visit all pages within the domain,
    // but not follow the links to external sites such as Google or Twitter.
    public void doesNotFollowExternalLinks() throws Exception {
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
