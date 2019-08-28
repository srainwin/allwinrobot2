package listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentBDDReporter;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import utils.SeleniumUtil;

/**
 * @author XWR
 * @Description 测试用例监听配置，暂时作用是生成report时移除重跑的结果,避免report生成多余用例数
 */
public class TestNGListener extends TestListenerAdapter {
	public Logger logger = Logger.getLogger(TestNGListener.class.getName());
	ExtentBDDReporter  extenthtmlReporter;
	ExtentReports extentreport;
	/* 测试用例级别，在测试类被实例化之前调用 */
	@Override
	public void onTestStart(ITestResult result) {
		super.onTestStart(result);
		logger.info(result.getName() + "-------------------Start-------------------");
	}
	
	/* 测试用例级别，每次测试成功时调用 */
	@Override
	public void onTestSuccess(ITestResult result) {
		super.onTestSuccess(result);
		logger.info(result.getName() + "-------------------End:Success-------------------");
	}

	/* 测试用例级别，每次测试失败时调用 */
	@Override
	public void onTestFailure(ITestResult result) {
		super.onTestFailure(result);
		logger.info(result.getName() + "-------------------End:Failure-------------------");

		
//		ExtentHtmlReporter extenthtmlReporter;
//		ExtentReports extentreport;
		String reportpath = "target/result/BddReport/";
		String configxmlpath = "/src/main/resources/config/bdd-config.xml";
//		
//		// 生成extentreport并初始化报告配置
		extenthtmlReporter = new ExtentBDDReporter(System.getProperty("user.dir") + reportpath);
//		extenthtmlReporter.config().setAutoCreateRelativePathMedia(true);
//		extenthtmlReporter.start();
		extenthtmlReporter.loadXMLConfig(System.getProperty("user.dir") + configxmlpath);
//		extenthtmlReporter.config().setTheme(Theme.STANDARD);
//		extenthtmlReporter.config().setEncoding("UTF-8");
//		extenthtmlReporter.config().setProtocol(Protocol.HTTPS);
//		extenthtmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
//		extenthtmlReporter.config().setDocumentTitle("AllWinRobot2");
//		extenthtmlReporter.config().setReportName("AllWinRobot2 Report");
//		String js = "$(document).ready(function() {"
//		    	+"var pathName = document.location.pathname;"
//				+"var index = pathName.substr(1).indexOf('target');"
//				+"var homepath = pathName.substr(0,index+1);"
//				+"var div=document.querySelector('.brand-logo > img');"
//				+"div.src=homepath + 'src/main/resources/reportlogo/allwin.png';"
//				+"div.style.width=60+'px';"
//				+"div.style.height=35+'px';"
//				+"});";
//		extenthtmlReporter.config().setJS(js);
		extentreport = new ExtentReports();
		extentreport.attachReporter(extenthtmlReporter);

		ExtentTest extentTest; //extentreport添加截图、日志等需要使用ExtentTest
		extentTest = extentreport.createTest("runScenario", "sample description");
		try {
			File screenfolder = new File(System.getProperty("user.dir") + "/target/result/cucumber-extentreports/screenshots/");
			if (!screenfolder.exists() && !screenfolder.isDirectory()) {
				screenfolder.mkdirs();
			}
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
			String screenname = /*scenario.getName() +*/ dateformat.format(new Date()) + ".png";
			File srcFile = ((TakesScreenshot) SeleniumUtil.threadWebDriver.get()).getScreenshotAs(OutputType.FILE);
			File destFile = new File(screenfolder.getAbsolutePath() + "/" + screenname);
			FileUtils.copyFile(srcFile, destFile);
			extentTest.fail("details", MediaEntityBuilder.createScreenCaptureFromPath(destFile.getAbsolutePath()).build());
//			extentTest.addScreenCaptureFromPath(destFile.getAbsolutePath());
			System.out.println("成功在扩展报告嵌入截图");
		} catch (IOException e) {
			System.out.println("在扩展报告嵌入截图异常");
			e.printStackTrace();
		}
	}

	/* 测试用例级别，每次跳过测试时调用 */
	@Override
	public void onTestSkipped(ITestResult result) {
		super.onTestSkipped(result);
		logger.info(result.getName() + "-------------------End:Skipped-------------------");
	}

	/* 测试集级别，在所有测试运行之前调用 */
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
	}

	/* 测试集级别，在所有测试运行之后调用 */
	@Override
	public void onFinish(ITestContext testContext) {
		extentreport.flush();
		super.onFinish(testContext);
		// 生成report时，移除重跑的结果，避免report生成多余用例数
		Iterator<ITestResult> listOfFailedTests = testContext.getFailedTests().getAllResults().iterator();
		while (listOfFailedTests.hasNext()) {
			ITestResult failedTest = (ITestResult) listOfFailedTests.next();
			ITestNGMethod method = failedTest.getMethod();
			if (testContext.getFailedTests().getResults(method).size() > 1) {
				listOfFailedTests.remove();
			} else {
				if (testContext.getPassedTests().getResults(method).size() > 0) {
					listOfFailedTests.remove();
				}
			}
		}
	}
}