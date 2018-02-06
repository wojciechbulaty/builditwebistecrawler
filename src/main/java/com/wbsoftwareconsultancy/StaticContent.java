package com.wbsoftwareconsultancy;

public class StaticContent implements WebElement {
    private final String url;

    public StaticContent(String url) {
        this.url = url;
    }

    @Override
    public String asString() {
        return url;
    }

    @Override
    public String asString(int indent) {
        return WebElement.padding(indent) + "Static file " + url;
    }
}
