import levit104dami404.tpo.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class TrendingDestinationTest {
    private static WebDriver chromeDriver;
    private static WebDriver firefoxDriver;

    @BeforeAll
    static void init() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        System.setProperty("webdriver.edge.driver", TestUtils.EDGE_DRIVER);
        System.setProperty("webdriver.gecko.driver", TestUtils.GECKO_DRIVER);;
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

    @AfterAll
    static void destroy() {
        chromeDriver.quit();
        firefoxDriver.quit();
    }

    private void test(WebDriver driver) throws InterruptedException {
        driver.get("https://www.booking.com/");
        driver.manage().window().setSize(new Dimension(1500, 1000));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(465,774)", "");
        driver.findElement(By.xpath("//a/div/div/div/picture")).click();
        driver.findElement(By.xpath("//label")).click();
        {
            Thread.sleep((long) (Math.random() * 10000));
        }
//        js.executeScript("window.scrollBy(612,60)", "");
        driver.findElement(By.xpath("//form/div/div[2]/div")).click();
        String href = driver.findElement(By.xpath("//h3/a")).getAttribute("href");
        String name = driver.findElement(By.xpath("//h3/a/div")).getText();
        driver.get(href);
        String name2 = driver.findElement(By.xpath("//h2")).getText();
        assert(name2.equals(name));
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
