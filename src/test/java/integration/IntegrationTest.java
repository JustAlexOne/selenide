package integration;

import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.*;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.timeout;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.*;
import static org.openqa.selenium.net.PortProber.findFreePort;

public abstract class IntegrationTest {
  @Rule
  public ScreenShooter img = ScreenShooter.failedTests() ;

  private static int port;
  protected static LocalHttpServer server;

  @BeforeClass
  public static void runLocalHttpServer() throws Exception {
    if (server == null) {
      synchronized (IntegrationTest.class) {
        port = findFreePort();
        server = new LocalHttpServer(port).start();

        System.out.println("START " + browser + " TESTS");
      }
    }
  }

  @AfterClass
  public static void restartUnstableWebdriver() {
    if (isIE() || isPhantomjs()) {
      closeWebDriver();
    }
  }

  protected void openFile(String fileName) {
    open("http://localhost:" + port + "/" + fileName);
  }

  protected <T> T openFile(String fileName, Class<T> pageObjectClass) {
    return open("http://localhost:" + port + "/" + fileName, pageObjectClass);
  }

  private long defaultTimeout;

  @Before
  public final void rememberTimeout() {
    defaultTimeout = timeout;
  }

  @After
  public final void restoreTimeout() {
    timeout = defaultTimeout;
  }
}
