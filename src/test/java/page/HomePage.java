package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {


    public HomePage(WebDriver driver, String title) {
        super(driver, title);
    }

    @FindBy(xpath="//a[contains(.,'日程')]")
    private WebElement homepage_agenda;

    public AgendaPage click_agenda() {
        homepage_agenda.click();
        return new AgendaPage(driver,"我的地盘::我的待办 - 禅道");
    }

}