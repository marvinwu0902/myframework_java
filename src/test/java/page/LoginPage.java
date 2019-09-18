package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Map;

public class LoginPage extends BasePage{
    @FindBy(xpath="//input[@id='account']")
    private WebElement loginpage_username;

    @FindBy(xpath="//input[@name='password']")
    private WebElement loginpage_password;

    @FindBy(xpath="//button[@id='submit']")
    private WebElement loginpage_submit;

    public LoginPage(WebDriver driver, String title) {
        super(driver, title);
    }
    public HomePage login(String username,String password) {

        typeusername(username);
        typepassword(password);
        loginpage_submit.click();
        return new HomePage(driver,"我的地盘 - 禅道");
    }
    public HomePage login(Map<String, String> param) {

        typeusername(param.get("用户名"));
        typepassword(param.get("密码"));
        loginpage_submit.click();
        return new HomePage(driver,"我的地盘 - 禅道");
    }

    private void typeusername(String name) {
        loginpage_username.clear();
        loginpage_username.sendKeys(name);
    }
    private void typepassword(String password) {
        loginpage_password.click();
        loginpage_password.clear();
        loginpage_password.sendKeys(password);
    }
}