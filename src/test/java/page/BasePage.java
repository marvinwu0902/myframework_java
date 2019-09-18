package page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;
import org.testng.Assert;


import java.util.Set;


class BasePage {
    WebDriver driver;
    private static Logger logger = Logger.getLogger(BasePage.class);

    BasePage(WebDriver driver, final String title) {
        this.driver = driver;

        int TIMEOUT = 10;
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        try{
            boolean flag = wait.until((ExpectedCondition<Boolean>) arg0 -> {
                String acttitle = arg0.getTitle();
                return acttitle.equals(title);
            });
        }catch(TimeoutException te) {
            throw new IllegalStateException("当前不是预期页面，当前页面title是：" + driver.getTitle());
        }
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, TIMEOUT) , this);

    }
    public void click(WebElement el) {
        try {
            el.click();
        } catch (NoSuchElementException e) {
            Assert.fail("未找到元素",e);
            logger.error("未找到元素：");
        }
    }
    void inputText(WebElement el, String text) {
        el.clear();
        el.sendKeys(text);
    }

    void selectByIndex(WebElement el, int index) {
        new Select(el).selectByIndex(index);
    }

    void selectByVisibleText(WebElement el, String text) {
        new Select(el).selectByVisibleText(text);
    }

    void selectByValue(WebElement el, String value) {
        new Select(el).selectByValue(value);
    }
    void frame(WebElement frame) {
        driver.switchTo().frame(frame);
    }
    void frameToDefault() {
        driver.switchTo().defaultContent();
    }

    public void assertAttribute(WebElement el, String attribute, String except) {
        //实现对元素的某一属性值进行断言
        //el:需断言的元素
        //attribute:属性名
        //except:期望的属性值
        String actual = el.getAttribute(attribute);
        try {
            assert actual.equals(except);
        } catch (AssertionError e) {
            logger.error("未能成功断言元素属性值");
            Assert.fail("未能成功断言元素属性值");
            throw e;
        }
    }

    public void assertEquals(WebElement el, String expect) {
        String actual = el.getText();
        try {
            assert actual.equals(expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
            Assert.fail("实际值：" + actual + " ; 期望值：" + expect);
            throw e;
        }

    }
    public void assertNotEquals(WebElement el, String expect) {
        String actual = el.getText();
        try {
            Assert.assertNotEquals(actual, expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
            Assert.fail("实际值：" + actual + " ; 期望值：" + expect);
            throw e;
        }
    }

    public void assertDtContain(WebElement el, String expect) {
        String actual = el.getText();
        try {
            assert (actual.contains(expect));
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
            Assert.fail("实际值：" + actual + " ; 期望值：" + expect);
            throw e;
        }
    }

    public void doubleClick(WebElement el) {
        //实现双击
        Actions actiondc = new Actions(driver);
        actiondc.doubleClick(el).perform();
    }

    public void carriageReturn() {
        //实现键盘上的回车操作
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ENTER).perform();
    }

    public void contextClick(WebElement el) {
        //实现右键点击
        Actions actioncc = new Actions(driver);
        actioncc.contextClick(el).perform();
    }


    public void inputByAutoIT(WebElement el, String exepath) {
        //通过AutoIT实现上传文件
        //el:触发选择文件上传窗口的元素，支持各种定位方式
        //exepath:AutoIT脚本文件名（绝对路径）
        try {
            el.click();
            Thread.sleep(2000);
            Runtime.getRuntime().exec(exepath);
            logger.info("上传文件成功");
        } catch (Exception e) {
            logger.error("执行上传文件失败");
        }
    }

    public void clickAndHold(WebElement el) {
        //实现长按鼠标左键
        Actions actioncnh = new Actions(driver);
        actioncnh.clickAndHold(el).perform();

    }

    public void window() throws InterruptedException {
        Thread.sleep(3000);
        Set<String> handles = driver.getWindowHandles();
        System.out.println(handles);
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }
    }

    public void window(String windowTitle) {
        boolean flag = false;
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String s : handles) {
            driver.switchTo().window(s);
            if (driver.getTitle().contains(windowTitle)) {
                flag = true;
                logger.info("成功切换窗口"+windowTitle);
                break;
            } else
                continue;
        }
        if (!flag) {
            logger.error("切换窗口失败，找不到"+windowTitle);
        }
    }
    public void mouseOver(WebElement el) {
        Actions actionmo = new Actions(driver);
        actionmo.clickAndHold(el).perform();
    }

    public void executeJS(String js, Object... args) {
        if (js == null) {
            logger.error("没有传入正确的JS脚本");
        } else {
            ((JavascriptExecutor) driver).executeScript(js, args);
        }
    }

    public void swipe(WebElement el) {
        //实现滑动到指定元素
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", el);
    }

    public void drag_and_drop(WebElement elSource, WebElement elTarget) {
        Actions actiondnd = new Actions(driver);
        actiondnd.dragAndDrop(elSource, elTarget).perform();
    }

}