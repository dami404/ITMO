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
import java.util.List;

public class StaysTest {
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
        {
            Thread.sleep((long) (Math.random() * 10000));
        }
        driver.findElement(By.xpath("//input[@name=\"ss\"]")).sendKeys( Keys.chord(Keys.LEFT_CONTROL, "a"));
        {
            Thread.sleep((long) (Math.random() * 10000));
        }
        driver.findElement(By.xpath("//input[@name=\"ss\"]")).sendKeys(Keys.BACK_SPACE);
        {
            Thread.sleep((long) (Math.random() * 10000));
        }

        driver.findElement(By.xpath("//input[@name=\"ss\"]")).sendKeys("Antalya");
        {
            Thread.sleep((long) (Math.random() * 10000));
        }
        driver.findElement(By.xpath("//li/div/div/div/div")).click();
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[3]/div/button")));
        }
        driver.findElement(By.xpath("//div[3]/div/button")).click();
        driver.findElement(By.xpath("//div[2]/div[2]/button[2]")).click();
        driver.findElement(By.xpath("//select")).click();
        driver.findElement(By.xpath("//select/option[19]")).click();
        driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
        for( String tab: driver.getWindowHandles()){
            driver.switchTo().window(tab);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("/h3/a/div"));
            assert(elements.size() == 0);
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
