package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AgendaPage extends BasePage {

    @FindBy(xpath="//a[@id='create']")
    private WebElement agendapage_create;

    @FindBy(xpath="//select[@id='pri']")
    private WebElement agendapage_pri;

    @FindBy(xpath="//iframe[@class='ke-edit-iframe']")
    private WebElement agendapage_frame2;

    @FindBy(xpath="//iframe[@id='iframe-triggerModal']")
    private WebElement agendapage_frame1;

    @FindBy(xpath="//body[@class='article-content']")
    private WebElement agendapage_desc;

    @FindBy(xpath="(//input[@id='name'])[2]")
    private WebElement agendapage_name;

    @FindBy(xpath="//select[@id='type']")
    private WebElement agendapage_type;

    @FindBy(xpath="//button[@id='submit']")
    private WebElement agendapage_submit;

    public AgendaPage(WebDriver driver, String title) {
        super(driver, title);
    }
    private void clickCreate(){
        agendapage_create.click();
    }
    private void inputAgendaName(String agendaName){
        agendapage_name.sendKeys(agendaName);
    }

    private void selectPri(String value){
        selectByValue(agendapage_pri,value);
    }

    private void selectType(String type){
        selectByValue(agendapage_type,type);
    }

    private void inputAgendaDesc(String agendaDesc){
        agendapage_desc.sendKeys(agendaDesc);
    }

    private void clickSubmit(){
        agendapage_submit.click();
    }

    public AgendaPage createAgenda(){
        clickCreate();
        frame(agendapage_frame1);
        selectPri("3");
        selectType("custom");
        inputAgendaName("pagefactory test2");
        frame(agendapage_frame2);
        inputAgendaDesc("这是一条测试数据");
        frameToDefault();
        frame(agendapage_frame1);
        clickSubmit();
        return new AgendaPage(driver,"我的地盘::我的待办 - 禅道");
    }
}
