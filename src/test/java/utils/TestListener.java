package utils;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testcase.BaseTest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Level;


public class TestListener extends BaseTest implements ITestListener {
    ATUTestRecorder recorder;

    public static Logger logger = Logger.getLogger(TestListener.class);

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    private static String getTestClassName(ITestResult iTestResult) {
        return iTestResult.getTestClass().getName();
    }

    /**
     * 传入全类名获得对应类中所有方法名
     */
    @SuppressWarnings("rawtypes")
    private static int getMethodnum(String pkgName) {
        int num = 0;
        try {
            Class clazz = Class.forName(pkgName);
            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.contains("test")) {
                    System.out.println("方法名称:" + methodName);
                    num = num + 1;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return num;
    }

    @Attachment(value = "recording", type = "video/webm")
    public static String encryptToBase64(String filePath) throws URISyntaxException {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Attachment(value = "recording", type = "video/mp4")
    public static File encryptToBase641(String filePath) throws URISyntaxException {
        if (filePath == null) {
            return null;
        }

//            byte[] b = Files.readAllBytes(Paths.get(filePath));
        URL refImgUrl = TestListener.class.getClassLoader().getResource(filePath);
        File refImgFile = Paths.get(refImgUrl.toURI()).toFile();
        return refImgFile;
    }

    //Text attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //HTML attachments for Allure
    @Attachment(value = "vide_link", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }


    @Override
    public void onStart(ITestContext iTestContext) {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        System.out.println("I am in onStart " + iTestContext.getName());
        logger.info("I am in onStart method " + iTestContext.getName());
//        iTestContext.setAttribute("WebDriver", this.driver);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
        logger.info("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
        String name = getTestClassName(iTestResult);
        try {
            String systemName = System.getProperty("os.name").toLowerCase();
            if (systemName.contains("window") || systemName.contains("mac os x")) {
                try {
                    String relativelyPath = System.getProperty("user.dir");
                    recorder = new ATUTestRecorder(relativelyPath, getTestMethodName(iTestResult), false);
                } catch (ATUTestRecorderException e) {
                    e.printStackTrace();
                }
                try {
                    recorder.start();
                } catch (ATUTestRecorderException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("跳过录屏");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
        logger.info("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");

        Object testClass = iTestResult.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        //Allure ScreenShotRobot and SaveTestLog
        if (driver instanceof WebDriver) {
            logger.info("Screenshot captured for test_draw case:" + getTestMethodName(iTestResult));
            saveScreenshotPNG(driver);
        }
        //Save a log on allure.
        saveTextLog(getTestMethodName(iTestResult) + " succeed and screenshot taken!");

        //Take base64Screenshot screenshot for extent reports
        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).
                getScreenshotAs(OutputType.BASE64);
        try {
            String systemName = System.getProperty("os.name").toLowerCase();
            if (systemName.contains("window") || systemName.contains("mac os x")) {
                try {
                    recorder.stop();
                    String testname = getTestMethodName(iTestResult) + ".mp4";
                    String fullurl = "<html>\n" +
                            "<script src=\"chrome-extension://bkjhhpofabdbekofdeijliifhaloginp/page/prompt.js\"></script>\n" +
                            "<script src=\"chrome-extension://bkjhhpofabdbekofdeijliifhaloginp/page/runScript.js\"></script>\n" +
                            "<head>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<a href=\"" + testname + "\">Click Download</a>\n" +
                            "<video controls autoplay name=\"media\">\n" +
                            "<source src =\"" + testname + "\" type=\"video/mp4\">\n" +
                            "</video>\n" +
                            "</body>\n" +
                            "</html>";
                    attachHtml(fullurl);
                } catch (ATUTestRecorderException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        logger.info("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        //Get driver from BaseTest and assign to local webdriver variable.
        Object testClass = iTestResult.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        //Allure ScreenShotRobot and SaveTestLog
        if (driver instanceof WebDriver) {
            System.out.println("Screenshot captured for testcase:" + getTestMethodName(iTestResult));
            saveScreenshotPNG(driver);
        }
        //Save a log on allure.
        saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");

        String systemName = System.getProperty("os.name").toLowerCase();
        System.out.println(systemName);
        if (systemName.contains("window") || systemName.contains("mac os x")) {
            try {
                recorder.stop();
                String testname = getTestMethodName(iTestResult) + ".mp4";
                String fullurl = "<html>\n" +
                        "<script src=\"chrome-extension://bkjhhpofabdbekofdeijliifhaloginp/page/prompt.js\"></script>\n" +
                        "<script src=\"chrome-extension://bkjhhpofabdbekofdeijliifhaloginp/page/runScript.js\"></script>\n" +
                        "<head>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<a href=\"" + testname + "\">Click Download</a>\n" +
                        "<video controls autoplay name=\"media\">\n" +
                        "<source src =\"" + testname + "\" type=\"video/mp4\">\n" +
                        "</video>\n" +
                        "</body>\n" +
                        "</html>";
                attachHtml(fullurl);
            } catch (ATUTestRecorderException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        logger.info("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        //Extentreports log operation for skipped tests.
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        logger.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

}
