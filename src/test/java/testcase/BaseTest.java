package testcase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


public class BaseTest {
    protected WebDriver driver;
    public WebDriver getDriver() {
        return driver;
    }
    @BeforeClass(description = "Class Level Setup!")
    public void setUp () throws Exception {
        try{
            String systemName = System.getProperty("os.name").toLowerCase();
            if(systemName.contains("window"))
            {
                System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"); //Chrome Driver
                System.setProperty("webdriver.chrome.silentOutput", "true"); //Chrome Driver
                driver = new ChromeDriver();
            }
            else if(systemName.contains("linux"))
            {
                System.setProperty("webdriver.chrome.driver", "/root/chrome/chromedriver");
                System.setProperty("webdriver.gecko.driver", "/usr/bin/google-chrome");
                System.setProperty("webdriver.chrome.silentOutput", "true");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                driver = new ChromeDriver(options);
            }
            else if (systemName.contains("mac os x"))
            {
                System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
                System.setProperty("webdriver.chrome.silentOutput", "true");
                driver = new ChromeDriver();
            }
            else
            {
                throw new RuntimeException("unsupport platform");
            }
            driver.manage().window().maximize();
        }
        catch (Exception e){
            throw e;
        }
    }

    @AfterClass(description = "Class Level Teardown!")
    public void tearDown () throws Exception {
        driver.quit();
    }

}
