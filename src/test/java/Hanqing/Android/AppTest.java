package Hanqing.Android;



import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class AppTest
{
	
	protected Logger l = LogManager.getLogger(this.getClass().getName());
	protected AndroidDriver d;
	
	@BeforeSuite
	public void setup_suite(
		) {
		try{
			l.entry();
			l.exit();
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
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
		try{
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
	        d = new AndroidDriver(new URL(appiumURL), capabilities);	
	        d.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
			l.exit();
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
	@AfterTest
	public void teardown_test() {
		try{
			l.entry();
			d.quit();
			l.exit();
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
	@AfterSuite
	public void teardown_suite() {
		try{
			l.entry();
			l.exit();
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
	//============================== Test cases ==============================
	
	/**
	 * baidu_16786191.apk
	 * @param keyword
	 */
	@Parameters({ 
		"keyword"
		})
	@Test
	public void test_baidu_search(
			String keyword
			){
		try{
			l.entry();
			//等待界面出现
			Thread.sleep(5000);
			//开始界面，划动3次
			for(int i=0;i<3;i++){
				l.info("swipe");
				d.swipe(800, 100, 100, 100, 1000);
				Thread.sleep(1000);
			}
			//点击进入链接
			d.findElementsByClassName("android.widget.ImageView").get(1).click();
			//搜索关键字
			Thread.sleep(2000);
	        d.findElement(By.id("com.baidu.searchbox:id/baidu_searchbox")).click();
	        d.findElement(By.id("com.baidu.searchbox:id/SearchTextInput")).sendKeys(keyword);
	        d.findElement(By.id("com.baidu.searchbox:id/float_search_or_cancel")).click();
	        //搜索结果页，切换到webView
	        Thread.sleep(5000);
	        for(String context : d.getContextHandles()){
	        	l.info("Context: {}", context);
	        }
	        //测试结束，等待10秒
	        Thread.sleep(10000);
			Assert.assertEquals(true, true);
			l.exit();
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}

	/**
	 * ContactManager.apk
	 * @param contactName
	 * @param contectEmail
	 */
	@Parameters({ 
		"contactName",
		"contectEmail"
		})
	@Test
	public void test_contactManager_addContect(
			String contactName,
			String contectEmail
			){
		try{
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
		}catch(Exception e){
			l.error("Error!");
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
}
