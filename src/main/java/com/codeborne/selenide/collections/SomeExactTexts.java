package com.codeborne.selenide.collections;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.SomeTextsMismatch;
import com.codeborne.selenide.impl.WebElementsCollection;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// todo the naming... Suggestion SomeTexts(for contains comparison) and SomeExactTexts (for equals comparison)
// todo we need to open the door -> people must be able to pass their own text comparison algorithm
public class SomeExactTexts extends ExactTexts {

    List<String> notFoundTexts = new ArrayList<>();

    public SomeExactTexts(String... expectedTexts) {
        super(expectedTexts);
    }

    public SomeExactTexts(List<String> expectedTexts) {
        super(expectedTexts);
    }

    @Override
    public boolean apply(List<WebElement> elements) {
        List<String> actualTexts = ElementsCollection.texts(elements);
        for (String expectedText : expectedTexts) {
            // todo remove if
            if (!actualTexts.remove(expectedText)) {
                notFoundTexts.add(expectedText);
            }
        }
        return notFoundTexts.isEmpty();
    }


    @Override
    public String toString() {
        return "Some exact texts " + expectedTexts;
    }

    @Override
    public void fail(WebElementsCollection collection, List<WebElement> elements, Exception lastError, long timeoutMs) {
        if (elements == null || elements.isEmpty()) {
            ElementNotFound elementNotFound = new ElementNotFound(collection, expectedTexts, lastError);
            elementNotFound.timeoutMs = timeoutMs;
            throw elementNotFound;
        } else {
            throw new SomeTextsMismatch(collection, ElementsCollection.texts(elements), expectedTexts, notFoundTexts, timeoutMs);
        }
    }
}
