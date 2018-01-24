package com.codeborne.selenide.ex;

import com.codeborne.selenide.impl.WebElementsCollection;

import java.util.Collection;
import java.util.List;

public class SomeTextsMismatch extends UIAssertionError {

    public SomeTextsMismatch(WebElementsCollection collection, List<String> actualTexts, List<String> expectedTexts, Collection<String> notFoundTexts, long timeoutMs) {
        super("\nActual: " + actualTexts +
                "\nNot found text(s): " + notFoundTexts +
                "\nExpected texts: " + expectedTexts +
                "\nCollection: " + collection.description());
        super.timeoutMs = timeoutMs;
    }

}
