package com.codeborne.selenide.collections;

import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.WebElementsCollection;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SomeExactTextsTest {
  // updated
  @Test
  public void varArgsConstructor() {
    SomeExactTexts someExactTexts = new SomeExactTexts("One", "Two", "Three");
    assertEquals("Expected texts list", asList("One", "Two", "Three"), someExactTexts.expectedTexts);
  }

  // new - in progress
  @Test
  public void testApplyOnCorrectSingleElement() {
    SomeExactTexts someExactTexts = new SomeExactTexts("One");

    WebElement mock1 = mock(WebElement.class);
    WebElement mock2 = mock(WebElement.class);
    WebElement mock3 = mock(WebElement.class);
    when(mock1.getText()).thenReturn("One");
    when(mock2.getText()).thenReturn("Two");
    when(mock3.getText()).thenReturn("Three");
    assertTrue(someExactTexts.apply(asList(mock1, mock2, mock3)));
  }

  @Test
  public void testApplyOnTheSameList() {
    SomeExactTexts someExactTexts = new SomeExactTexts("One", "Two", "Three");

    WebElement mock1 = mock(WebElement.class);
    WebElement mock2 = mock(WebElement.class);
    WebElement mock3 = mock(WebElement.class);
    when(mock1.getText()).thenReturn("One");
    when(mock2.getText()).thenReturn("Two");
    when(mock3.getText()).thenReturn("Three");
    assertTrue(someExactTexts.apply(asList(mock1, mock2, mock3)));
  }

  @Test
  public void testApplyOnTheBiggerList() {
    SomeExactTexts someExactTexts = new SomeExactTexts("One", "Two", "Three", "Four");

    WebElement mock1 = mock(WebElement.class);
    WebElement mock2 = mock(WebElement.class);
    WebElement mock3 = mock(WebElement.class);
    when(mock1.getText()).thenReturn("One");
    when(mock2.getText()).thenReturn("Two");
    when(mock3.getText()).thenReturn("Three");
    assertFalse(someExactTexts.apply(asList(mock1, mock2, mock3)));
  }

  @Test
  public void testApplyOnCorrectSizeAndCorrectElementsText() {
    testApplyMethodOnDifferentCondtions(true);
  }

  @Test
  public void testApplyOnCorrectListSizeButWrongElementsText() {
    testApplyMethodOnDifferentCondtions(false);
  }

  private void testApplyMethodOnDifferentCondtions(boolean shouldMatch) {
    String exactText1 = "One";
    String exactText2 = "Two";
    ExactTexts exactTexts = new ExactTexts(exactText1, exactText2);
    WebElement mockedWebElement1 = mock(WebElement.class);
    WebElement mockedWebElement2 = mock(WebElement.class);
    when(mockedWebElement1.getText()).thenReturn(exactText1);
    when(mockedWebElement2.getText()).thenReturn(shouldMatch ? exactText2 : exactText1);
    assertEquals(shouldMatch, exactTexts.apply(asList(mockedWebElement1, mockedWebElement2)));
  }

  @Test
  public void testFailWithNullElementsList() {
    failOnEmptyOrNullElementsList(null);
  }

  @Test
  public void testFailWithEmptyElementsLIst() {
    failOnEmptyOrNullElementsList(emptyList());
  }

  private void failOnEmptyOrNullElementsList(List<WebElement> elements) {
    ExactTexts exactTexts = new ExactTexts("One");
    Exception exception = new Exception("Exception method");
    try {
      exactTexts.fail(mock(WebElementsCollection.class), elements, exception, 10000);
    } catch (ElementNotFound ex) {
      assertEquals("Element not found {null}\nExpected: [One]", ex.getMessage());
    }
  }

  @Test
  public void failOnTextMismatch() {
    ExactTexts exactTexts = new ExactTexts("One");
    Exception exception = new Exception("Exception method");

    WebElement mockedWebElement = mock(WebElement.class);
    when(mockedWebElement.getText()).thenReturn("Hello");

    WebElementsCollection mockedElementsCollection = mock(WebElementsCollection.class);
    when(mockedElementsCollection.description()).thenReturn("Collection description");

    try {
      exactTexts.fail(mockedElementsCollection,
                      singletonList(mockedWebElement),
                      exception,
                      10000);
    } catch (TextsMismatch ex) {
      assertEquals("\nActual: [Hello]\n" +
          "Expected: [One]\n" +
          "Collection: Collection description", ex.getMessage());
    }
  }
  // updated
  @Test
  public void testToString() {
    SomeExactTexts someExactTexts = new SomeExactTexts("One", "Two", "Three");
    assertEquals("Some exact texts [One, Two, Three]", someExactTexts.toString());
  }
  // updated
  @Test
  public void emptyArrayIsNotAllowed() {
    try {
      new SomeExactTexts();
      fail("expected IllegalArgumentException");
    }
    catch (IllegalArgumentException expected) {
      assertThat(expected.getMessage(), is("No expected texts given"));
    }
  }
  // updated
  @Test
  public void emptyListIsNotAllowed() {
    try {
      new SomeExactTexts(emptyList());
      fail("expected IllegalArgumentException");
    }
    catch (IllegalArgumentException expected) {
      assertThat(expected.getMessage(), is("No expected texts given"));
    }
  }
}
