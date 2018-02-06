package com.wbsoftwareconsultancy;

public abstract class CrawlElement {
    public final String asString() {
        return asString(0);
    }

    protected abstract String asString(int indent);

    protected String padding(int indent) {
        return new String(new char[indent * 4]).replace("\0", " ");
    }
}
