package integration.testng;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import integration.server.LocalHttpServer;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import static org.openqa.selenium.net.PortProber.findFreePort;

abstract class BaseTestNGTest {
  private static final Logger log = LoggerFactory.getLogger(BaseTestNGTest.class);
  private static LocalHttpServer server;

  protected static final String browser = System.getProperty("selenide.browser", "chrome");
  protected static String baseUrl;

  protected SelenideDriver driver = new SelenideDriver(
    new SelenideConfig().browser(browser).baseUrl(baseUrl)
  );

  @BeforeClass
  private void startServer() throws Exception {
    if (server == null) {
      int port = findFreePort();
      log.info("START {} Test NG tests", browser);
      server = new LocalHttpServer(port, true).start();
      baseUrl = "https://127.0.0.1:" + port;
    }
  }

  protected SelenideElement $(String cssSelector) {
    return driver.$(cssSelector);
  }

  protected SelenideElement $(By locator) {
    return driver.$(locator);
  }

  public ElementsCollection $$(String cssSelector) {
    return driver.$$(cssSelector);
  }
}
