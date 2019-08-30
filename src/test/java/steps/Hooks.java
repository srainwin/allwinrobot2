package steps;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import log.LogConfiguration;

/**
 * @author XWR
 * @Description Hooks是cucumber中每一个Scenario都会执行的前置处理和后置处理
 *              Hooks.java文件与“step”.java文件一样只需放到steps文件夹便会被执行，不同的是Hooks不需要与之对应的feature文件
 */
public class Hooks {
	public Logger logger = Logger.getLogger(Hooks.class.getName());

	TestContext testContext;

	public Hooks(TestContext context) {
		testContext = context;
	}

	@Before
	public void setUp(Scenario scenario) {
		// 日志配置初始化
		System.out.println("开始日志配置初始化");
		LogConfiguration.initLog(scenario.getClass().getSimpleName(), 
								 testContext.getlogRootFolderPath(),
								 testContext.getkeepLogDay());
		System.out.println("日志配置初始化成功");
		try {
			logger.info("正启动浏览器");
			// 启动本地或者远程的某款浏览器
			testContext.getseleniumUtil()
					   .launchBrowser(testContext.getbrowserName(),
							   		  testContext.getdriverConfigFilePath(), 
							   		  testContext.getisRemote(), 
							   		  testContext.gethuburl(),
							   		  testContext.getpageLoadTimeout());

			// 启动sikuli屏幕操作器
			if (testContext.getisVNC().equals("true")) {
				// VNC
				testContext.getsikuliUtil()
						   .launchVNCScreen(testContext.getseleniumUtil().getGridIP(testContext.gethuburl()), 
								   			testContext.getvncPassword(), 
								   			testContext.getsikuliImageFolderPath());
			} else {
				// local
				testContext.getsikuliUtil().launchScreen(testContext.getsikuliImageFolderPath());
			}
			logger.info(testContext.getbrowserName() + "浏览器启动成功!");
		} catch (Exception e) {
			logger.error(testContext.getbrowserName() + "浏览器不能正常工作，请检查是不是被手动关闭或者其他原因", e);
		}

	}

	@After
	public void tearDown(Scenario scenario) {
		// 对cucumber-report嵌入失败场景截图，allure-cucumber4-jvm也会自动附加scenario.embed的图片到allure-report
		try {
			if (scenario.isFailed()) {
				byte[] screenshotAs = null;
				screenshotAs = ((TakesScreenshot) testContext.getseleniumUtil().threadWebDriver.get()).getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshotAs, "image/png");
			}
			logger.info("测试报告成功嵌入失败场景的截图");
		} catch (Exception e) {
			logger.error("测试报告嵌入失败场景的截图时发生异常", e);
		}
		
		// 关闭浏览器、关闭VNC、等等的善后工作
		try {
			// 关闭浏览器
			testContext.getseleniumUtil().quit();
			logger.info("关闭浏览器成功");
			// 当开启VNC时关闭VNC连接
			if( testContext.getisVNC().equals("true") ){
				testContext.getsikuliUtil().closeVNC();
			}
			logger.info("关闭VNC成功");
		} catch (Exception e) {
			logger.error("关闭浏览器、关闭VNC、等等的善后工作发生异常", e);
		}
	}

	// /**
	// * @Description 失败用例截图备用方法,兼容web与windows截图
	// */
	// @After
	// public void tearDown(Scenario scenario) {
	// // 对cucumber-report嵌入失败场景截图
	// try {
	// File screenfolder = new File(System.getProperty("user.dir") +
	// "/target/result/cucumber-extentreports/screenshots/");
	// if (!screenfolder.exists() && !screenfolder.isDirectory()) {
	// screenfolder.mkdir();
	// }
	// String screenName = String.valueOf(new Date().getTime()) + ".png";
	// String screenPath = screenfolder.getAbsolutePath() + "/" + screenName;
	// File screenFile = new File(screenPath);
	//
	// Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	// BufferedImage screenshot = (new Robot()).createScreenCapture(new
	// Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
	//
	// // 将screenshot图像对象写入文件
	// ImageIO.write(screenshot, "png", screenFile);
	//
	// scenario.embed(File2byte(screenFile), "image/png");
	// logger.info("成功进行失败用例截图，截图保存路径是：" + screenFile.getAbsolutePath());
	// } catch (Exception e) {
	// logger.error("失败用例截图发生异常",e);
	// }
	// }
	//
	// /**
	// * @Description File对象转byte[]对象
	// * @param tradeFile
	// * @return
	// */
	// public byte[] File2byte(File tradeFile){
	// byte[] buffer = null;
	// try{
	// FileInputStream fis = new FileInputStream(tradeFile);
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// byte[] b = new byte[1024];
	// int n;
	// while ((n = fis.read(b)) != -1){
	// bos.write(b, 0, n);
	// }
	// fis.close();
	// bos.close();
	// buffer = bos.toByteArray();
	// }catch (FileNotFoundException e){
	// e.printStackTrace();
	// }catch (IOException e){
	// e.printStackTrace();
	// }
	// return buffer;
	// }
}
