import levit104dami404.tpo.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class CarRentTest {
  private static WebDriver chromeDriver;
  private static WebDriver firefoxDriver;
  @BeforeAll
  static void init() {

    System.setProperty("webdriver.http.factory", "jdk-http-client");
    System.setProperty("webdriver.edge.driver", TestUtils.EDGE_DRIVER);
    System.setProperty("webdriver.gecko.driver", TestUtils.GECKO_DRIVER);
    System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
    EdgeOptions options = new EdgeOptions();
    options.addArguments("--remote-allow-origins=*");
    options.addArguments("--disable-blink-features=AutomationControlled");
    options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0");

    options.setPageLoadStrategy(PageLoadStrategy.NONE);
    chromeDriver = new EdgeDriver();
    chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    FirefoxOptions options1 = new FirefoxOptions();
    options1.addArguments("--remote-allow-origins=*");
    options1.addArguments("--disable-blink-features=AutomationControlled");
    options1.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0");
    options1.setPageLoadStrategy(PageLoadStrategy.NONE);
    firefoxDriver = new FirefoxDriver();
  }

  private void test(WebDriver driver) throws InterruptedException {
    driver.get("https://www.booking.com//");
    driver.manage().window().setSize(new Dimension(1500, 1000));
    driver.findElement(By.xpath("//header/nav[2]/div/ul/li[3]/a")).click();
    driver.findElement(By.xpath("//main/div[2]/div/div/div/div/div/div[2]/div/div/div/div/span/div/div/div/div[2]/div/input")).click();
    driver.findElement(By.xpath("//main/div[2]/div/div/div/div/div/div[2]/div/div/div/div/span/div/div/div/div[2]/div/input")).sendKeys("Международный аэропорт Вашингтон Даллес (IAD), Вашингтон, США");
    { Thread.sleep((long) (Math.random() * 10000)); }
    driver.findElement(By.xpath("//button/div/div[2]")).click();
    JavascriptExecutor js = (JavascriptExecutor)driver;
    js.executeScript("window.scrollBy(863,689)", "");
    driver.findElement(By.xpath("//tr[4]/td/span")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//tr[4]/td[7]/span")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//select[@name=\"pickup-time\"]")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//select[@name=\"pickup-time\"]/option[23]")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//select[@name=\"dropoff-time\"]")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//select[@name=\"pickup-time\"]/option[35]")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    driver.findElement(By.xpath("//div[@class=\"submit-button-container\"]/button")).click();
    { Thread.sleep((long) (Math.random() * 10000)); }

    { Thread.sleep((long) (Math.random() * 10000)); }
    {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//main/div/div/div/div/div/div[2]/div/div[4]/div/div")));
    }
  }

  @AfterAll
  static void destroy() {
    chromeDriver.quit();
    firefoxDriver.quit();
  }
  @Test
  public void Chrome() throws InterruptedException {
    test(chromeDriver);
  }

  @Test
  public void Firefox() throws InterruptedException {
    test(firefoxDriver);
  }

}
