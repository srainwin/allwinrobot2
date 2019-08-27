package steps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.cucumber.listener.Reporter;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.SeleniumUtil;

public class Hooks {
	public Logger logger = Logger.getLogger(Hooks.class.getName());
	
	@Before
	public void setUp() throws  Exception{
		
	}
	
	@After
	public void tearDown(Scenario scenario){
		// 对cucumber-report嵌入失败场景截图
		try{
			if (scenario.isFailed()) {
				byte[] screenshotAs = null;
				screenshotAs = ((TakesScreenshot) SeleniumUtil.threadWebDriver.get()).getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshotAs, "image/png"); 
			}
			logger.info("cucumber-report成功嵌入失败场景截图");
		}catch(Exception e){
			logger.error("cucumber-report嵌入失败场景截图时发生异常",e);
		}
		
		// 对cucumber-extentreport嵌入失败场景截图
		try{
			if (scenario.isFailed()) {
				File screenfolder = new File(System.getProperty("user.dir") + "/target/result/cucumber-extentreports/screenshots/");
				if (!screenfolder.exists() && !screenfolder.isDirectory()) {
					screenfolder.mkdirs();
				}
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
				String screenname = scenario.getName() + dateformat.format(new Date()) + ".png";
				File srcFile = ((TakesScreenshot) SeleniumUtil.threadWebDriver.get()).getScreenshotAs(OutputType.FILE);
				File destFile = new File(screenfolder.getAbsolutePath() + "/" + screenname);
				FileUtils.copyFile(srcFile, destFile);
				Reporter.addScreenCaptureFromPath(destFile.getAbsolutePath());
			}
			logger.info("cucumber-extentreport成功嵌入失败场景截图");
		}catch(Exception e){
			logger.error("cucumber-extentreport嵌入失败场景截图时发生异常",e);
		}
	}
		
//	/**
//	 * @Description 失败用例截图备用方法,兼容web与windows截图
//	 */
//	public void takePhoto() {
//		try {
//			File screenfolder = new File(System.getProperty("user.dir") + "/target/result/cucumber-extentreports/screenshots/");
//			if (!screenfolder.exists() && !screenfolder.isDirectory()) {
//				screenfolder.mkdir();
//			}
//			String screenName = String.valueOf(new Date().getTime()) + ".png";
//			String screenPath = screenfolder.getAbsolutePath() + "/" + screenName;
//			File screenFile = new File(screenPath);
//
//			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//			BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
//
//			// 将screenshot图像对象写入文件
//			ImageIO.write(screenshot, "png", screenFile);
//			Reporter.addScreenCaptureFromPath(screenFile.getAbsolutePath());
//			logger.info("成功进行失败用例截图，截图保存路径是：" + screenFile.getAbsolutePath());
//		} catch (Exception e) {
//				logger.error("失败用例截图发生异常",e);
//		}
//	}
}
