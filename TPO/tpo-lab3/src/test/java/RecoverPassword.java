import levit104dami404.tpo.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.List;

public class RecoverPassword {
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
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.manage().window().setSize(new Dimension(1500, 1000));
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//header/nav/div[2]/a[2]")).click();
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//input[@type=\"email\"]")).click();
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//input[@type=\"email\"]")).sendKeys("123aa123@mail.ru");
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//a[@class=\"nw-link-account-recovery\"]")).click();
        { Thread.sleep((long) (Math.random() * 10000)); }
        driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
        { Thread.sleep((long) (Math.random() * 10000)); }
        {
            List<WebElement> elements = driver.findElements(By.xpath("input[@type=\"email\"]"));
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
