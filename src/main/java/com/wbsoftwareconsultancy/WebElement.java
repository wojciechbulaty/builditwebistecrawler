package com.wbsoftwareconsultancy;

public interface WebElement {
    String asString();
    String asString(int indent);

    static String padding(int indent) {
        return new String(new char[indent * 4]).replace("\0", " ");
    }
}
