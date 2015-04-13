package Hanqing.Android;


import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class AppTest {

    protected Logger l = LogManager.getLogger(this.getClass().getName());
    protected AndroidDriver d;

    @BeforeSuite
    public void setup_suite(
    ) {
        try {
            l.entry();
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    /**
     * 初始化
     *
     * @param appDir             APK路径
     * @param appName            APK名称
     * @param deviceName         设备名称
     * @param platformName       平台名称
     * @param platformVersion    系统版本
     * @param appPackage         被测试包名
     * @param appActivity        被测试activity名
     * @param appiumURL          appium服务地址
     * @param implicitlyWaitTime 隐性等待时间
     */
    @Parameters({
            "appDir",
            "appName",
            "deviceName",
            "platformName",
            "platformVersion",
            "appPackage",
            "appActivity",
            "appiumURL",
            "implicitlyWaitTime"
    })
    @BeforeTest
    public void setup_test(
            String appDir,
            String appName,
            String deviceName,
            String platformName,
            String platformVersion,
            String appPackage,
            String appActivity,
            String appiumURL,
            int implicitlyWaitTime
    ) {
        try {
            l.entry();
            File dir = new File(appDir);
            File app = new File(dir, appName);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", app.getAbsolutePath());
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformName", platformName);
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("appPackage", appPackage);
            capabilities.setCapability("appActivity", appActivity);
            capabilities.setCapability("unicodeKeyboard", "True");    //可以输入中文
            capabilities.setCapability("resetKeyboard", "True");        //隐藏键盘
            d = new AndroidDriver(new URL(appiumURL), capabilities);
            d.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    @AfterTest
    public void teardown_test() {
        try {
            l.entry();
            d.quit();
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    @AfterSuite
    public void teardown_suite() {
        try {
            l.entry();
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    //============================== Test cases ==============================
    //同程App

    /**
     * 同程App
     * @param searchKeyword -- 搜索关键字
     * @param uid -- 用户名
     * @param pwd -- 密码
     */
    @Parameters({
            "searchKeyword",
            "uid",
            "pwd"
    })
    @Test
    public void test_tc(
            String searchKeyword,
            String uid,
            String pwd) {
        try {
            l.entry();

            log("启动应用");

//            //等待界面出现
//            Thread.sleep(5000);


            //首页，点击‘跳过’按钮
            d.findElement(By.id("com.tongcheng.android:id/iv_close")).click();

            //===== 底部导航 =====
            //发现
            log("底部导航");
            d.findElement(By.id("com.tongcheng.android:id/iv_home_wallet")).click();
            //发现页，点击‘跳过’按钮
            d.findElement(By.name("跳过")).click();
            //抢购
            d.findElement(By.id("com.tongcheng.android:id/iv_home_order")).click();
            //我的
            d.findElement(By.id("com.tongcheng.android:id/iv_home_my")).click();
            //点击登录链接
            log("登录");
            d.findElement(By.id("com.tongcheng.android:id/btn_mytc_login")).click();
            //填写登录信息并登录
            d.findElement(By.id("com.tongcheng.android:id/login_account")).sendKeys(uid);
            d.findElement(By.id("com.tongcheng.android:id/login_password")).sendKeys(pwd);
            d.findElement(By.id("com.tongcheng.android:id/login_commit_btn")).click();
            //首页
            d.findElement(By.id("com.tongcheng.android:id/iv_home_main")).click();

            //===== 首页搜索 =====
            log(String.format("搜索：%s", searchKeyword));
            //点击搜索框
            d.findElement(By.id("com.tongcheng.android:id/tv_home_actionbar_search")).click();
            //输入关键字
            d.findElement(By.id("com.tongcheng.android:id/keyword")).sendKeys(searchKeyword);
            //点击结果
            List<WebElement> names = d.findElements(By.id("com.tongcheng.android:id/name"));
            log(String.format("搜索结果数：%d", names.size()));
            List<WebElement> counts = d.findElements(By.id("com.tongcheng.android:id/count"));
            log(String.format("搜索结果数量数：%d", names.size()));
            for (int i = 0; i < (names.size() - 1); i++) {
                try {
                    log(String.format("搜索结果：%s，结果数量：%s", names.get(i).getText(), counts.get(i).getText()));
                } catch (Exception e) {
                    //do nothing
                }
            }

            //===== 点击一次搜索结果，展示二次搜索结果 =====
            log("点击第一个产品");
            if (names.size() > 0) {
                names.get(0).click();
            }
            //名称
            List<WebElement> names2 = d.findElements(By.id("com.tongcheng.android:id/sceneryNameTextView"));
            log(String.format("产品数：%d", names2.size()));
            //价格
            List<WebElement> prices2 = d.findElements(By.id("com.tongcheng.android:id/priceTextView"));
            log(String.format("价格数：%d", prices2.size()));
            //评分
            List<WebElement> values2 = d.findElements(By.id("com.tongcheng.android:id/ratingTextView"));
            log(String.format("评分数：%d", values2.size()));
            for (int i = 0; i < names2.size(); i++) {
                try {
                    log(String.format("名称：%s，价格：%s，评分：%s", names2.get(i).getText(), prices2.get(i).getText(), values2.get(i).getText()));
                } catch (Exception e) {
                    //do nothing
                }
            }

            //===== 点击二次搜索结果 =====
            if (names2.size() > 0) {
                names2.get(0).click();
            }

            //屏幕截图
            takeScreenshot(d, System.getProperty("user.dir"), "testScreenshot", "jpg");
            log("屏幕截图完成");

            //测试结束，等待10秒
            Thread.sleep(10000);
            l.exit();
        } catch (Exception e) {
            log("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    /**
     * baidu_16786191.apk
     *
     * @param keyword 搜索关键字
     */
    @Parameters({
            "keyword"
    })
    @Test
    public void test_baidu_search(String keyword) {
        try {
            l.entry();
            //等待界面出现
            Thread.sleep(5000);
            //开始界面，划动1次
            d.swipe(800, 100, 100, 100, 1000);
            //搜索关键字
            Thread.sleep(2000);
            d.findElement(By.id("com.baidu.searchbox:id/baidu_searchbox")).click();
            d.findElement(By.id("com.baidu.searchbox:id/SearchTextInput")).sendKeys(keyword);
            d.findElement(By.id("com.baidu.searchbox:id/float_search_or_cancel")).click();
            //搜索结果页，切换到webView
            Thread.sleep(5000);
            for (String context : d.getContextHandles()) {
                l.info("Context: {}", context);
            }
            for (String context : d.getContextHandles()) {
                if (context.contains("WEBVIEW")){
                    d.context(context);
                    //搜索关键字
                    d.findElement(By.id("index-kw")).sendKeys("test");
                    d.findElement(By.id("index-kw")).submit();
                }
            }
            //测试结束，等待10秒
            Thread.sleep(10000);
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    @Test
    public void test_hqapp() {
        try {
            l.entry();
            //等待界面出现
            Thread.sleep(5000);
            //点击Open URL按钮
            d.findElementById("com.example.hqapp:id/button_1").click();
            Thread.sleep(10000);
            //列出所有view
            for (String context : d.getContextHandles()) {
                l.info("Context: {}", context);
            }
            //切换到webview
            d.context("WEBVIEW_com.example.hqapp");
            //搜索关键字
            d.findElement(By.name("word")).sendKeys("test");
            d.findElement(By.name("word")).submit();
            //获取页面搜索结果
            Thread.sleep(10000);
            l.info("Result text: {}", d.findElement(By.id("results")).findElements(By.tagName("a")).get(0).findElement(By.tagName("em")).getText());
            //测试结束，等待10秒
            Thread.sleep(10000);
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    //点评app
    @Parameters({
            "city"
    })
    @Test
    public void test_dianping(String city) {
        try {
            l.entry();
            //等待界面出现
            Thread.sleep(10000);
            //开始界面，划动2次
            for (int i = 0; i < 2; i++) {
                d.swipe(800, 100, 100, 100, 1000);
                Thread.sleep(1000);
            }
            //点击进入按钮
            d.findElementsByClassName("android.widget.ImageView").get(2).click();
            //搜索并选择城市
            d.findElement(By.id("com.dianping.v1:id/start_search")).click();
            d.findElement(By.id("com.dianping.v1:id/search_edit")).sendKeys(city);
            d.findElement(By.id("android:id/text1")).click();
            //测试结束，等待10秒
            Thread.sleep(10000);
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

    /**
     * ContactManager.apk
     *
     * @param contactName  联系人名称
     * @param contectEmail 联系人邮箱
     */
    @Parameters({
            "contactName",
            "contectEmail"
    })
    @Test
    public void test_contactManager_addContect(
            String contactName,
            String contectEmail
    ) {
        try {
            l.entry();
            WebElement el = d.findElement(By.name("Add Contact"));
            el.click();
            List<WebElement> textFieldsList = d.findElementsByClassName("android.widget.EditText");
            textFieldsList.get(0).sendKeys(contactName);
            textFieldsList.get(2).sendKeys(contectEmail);
            d.swipe(100, 500, 100, 100, 2);
            d.findElementByName("Save").click();
            Thread.sleep(10000);
            Assert.assertEquals(true, true);
            l.exit();
        } catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
            Assert.assertEquals(true, false);
        }
    }

//    ======================================== 公共方法 ========================================
    
    /**
     * 错误截图
     * @param d -- WebDriver
     * @param filePath -- 保存图片路径
     * @param fileName -- 保存图片文件名
     * @param extName -- 保存图片扩展名
     */
    public void takeScreenshot(WebDriver d, String filePath, String fileName, String extName){
        l.entry();
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timeStr = df.format(new Date());
            File screenShotFile = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
            String fileFullPath = String.format("%s//%s_%s.%s", filePath, fileName, timeStr, extName);
            FileUtils.copyFile(screenShotFile, new File(fileFullPath));
            log(String.format("Screenshot file name: %s_%s.%s", fileName, timeStr, extName));
            l.exit();
        }catch (Exception e) {
            l.error("Error!");
            e.printStackTrace();
        }
    }

    /**
     * 同时使用log4j2和reportng记录日志*
     * @param info
     */
    public void log(String info){
        l.info(info);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Reporter.log(String.format("%s - %s", sdf.format(Calendar.getInstance().getTime()), info));
    }
}
