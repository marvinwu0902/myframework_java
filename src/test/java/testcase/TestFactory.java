package testcase;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;
import page.AgendaPage;
import io.qameta.allure.*;
import utils.*;


import java.util.Map;


@Listeners({TestListener.class})
public class TestFactory extends BaseTest{
    @BeforeMethod
    @Override
    public void setUp() throws Exception {
        super.setUp();
        driver.get("http://127.0.0.1/zentao/user-login.html");
    }

    @AfterMethod(alwaysRun = true)
    public void end() {
        driver.quit();
    }
    @Story("测试登录")
    @Feature("登录模块")
    @Description("测试成功登录")
    @Severity(SeverityLevel.BLOCKER)
    @Test(dataProvider="logindata" ,dataProviderClass = DataPro.class)
    public void test_login(Map<String, String> param){
        String loginTitle = "用户登录 - 禅道";
        LoginPage lp = new LoginPage(driver, loginTitle);
        HomePage hp = lp.login(param);
    }

//@allure.severity("critical")               # 优先级，包含blocker, critical, normal, minor, trivial 几个不同的等级
//@allure.feature("测试模块_demo1")           # 功能块，feature功能分块时比story大,即同时存在feature和story时,feature为父节点
//@allure.story("测试模块_demo2")             # 功能块，具有相同feature或story的用例将规整到相同模块下,执行时可用于筛选
//@allure.issue("BUG号：123")                 # 问题表识，关联标识已有的问题，可为一个url链接地址

    @Story("测试添加待办")
    @Feature("日程模块")
    @Description("测试添加待办")
    @Severity(SeverityLevel.BLOCKER)
//    @Test
    public void test_createAgenda(){
        String loginTitle = "用户登录 - 禅道";
        LoginPage lp = new LoginPage(driver, loginTitle);
        HomePage hp = lp.login("admin","1qaz@WSX");
        AgendaPage ap = hp.click_agenda();
        ap.createAgenda();
    }
}