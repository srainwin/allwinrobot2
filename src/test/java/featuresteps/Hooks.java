package featuresteps;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import cucumber.PageContext;
import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import log.LogConfiguration;
import selenium.SeleniumUtil;

/**
 * @author XWR
 * @Description Hooks是cucumber中每一个Scenario都会执行的前置处理和后置处理
 *              Hooks.java文件与“step”.java文件一样只需放到steps文件夹便会被执行，不同的是Hooks不需要与之对应的feature文件
 *              @Before(order=int)：以递增顺序运行，例如值0将首先运行，1表示0之后。
 *				@After(order=int)：这是以递减顺序运行，例如值1将首先运行，0将在1之后运行。
 *				@Before(value="@XXX")：这是指定某个场景才会执行的前置处理
 *				@After(value="@XXX")：这是指定某个场景才会执行的后置处理
 */
public class Hooks {
	
	public Logger logger = Logger.getLogger(Hooks.class.getName());

	TestContext testContext;
	SeleniumUtil seleniumUtil;
	PageContext pageContext;
	
	public Hooks(TestContext context) {
		testContext = context;
		seleniumUtil = testContext.getseleniumUtil();
		pageContext = testContext.getpageContext();
	}
	
	@Before(order=0)
	public void setUp_LogInit(Scenario scenario) {
		// 日志配置初始化
		try{
			LogConfiguration.initLog(scenario.getName(),
									 testContext.getlogRootFolderPath(),
									 testContext.getkeepLogDay());
			System.out.println("日志配置初始化成功");
		}catch(Exception e){
			System.out.println("日志配置初始化失败");
			e.printStackTrace();
		}
	}
	
	@Before(order=1)
	public void setUp_seleniumInit(Scenario scenario) {
		logger.info(scenario.getName() + "------------start");
		// selenium启动初始化
		try{
			logger.info("正启动浏览器");
			// 启动本地或者远程的某款浏览器
			testContext.getseleniumUtil()
					   .launchBrowser(testContext.getbrowserName(),
							   		  testContext.getdriverConfigFilePath(),
							   		  testContext.getisRemote(),
							   		  testContext.gethuburl(),
							   		  testContext.getpageLoadTimeout());
			logger.info(testContext.getbrowserName() + "浏览器启动成功!");
		} catch (Exception e) {
			logger.error(testContext.getbrowserName() + "浏览器启动失败，请检查是不是被手动关闭或者其他原因", e);
		}
	}
	
	@Before(order=2)
	public void setUp_sikuliInit(Scenario scenario) {
		// sikuli启动初始化
		try{
			// 启动sikuli屏幕识别器
			logger.info("正启动屏幕图像识别器");
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
			logger.info("屏幕图像识别器启动成功!");
		}catch(Exception e){
			logger.error(testContext.getbrowserName() + "屏幕图像识别器启动失败", e);
		}
	}
	
	//loginfail场景不需要参与登陆邮箱这个before
	@Before(value="not @loginfail",order=3)
	public void setUp_login(Scenario scenario) {
		// 登陆126邮箱网站
		try{
			logger.info("开始输入126邮箱网址");
			seleniumUtil.get(testContext.gettesturl());
			File file = new File(testContext.getcookiesConfigFilePath());
			if(file.exists()) { //BaseRun.java的AfterSuite会在自动化测试整体结束后删除cookies文件，因此需要判断文件是否存在
				logger.info("使用cookies方式免登陆");
				seleniumUtil.delAllcookies();
				seleniumUtil.addcookies(testContext.getcookiesConfigFilePath());
				seleniumUtil.refresh();
			}else{
				pageContext.setPagefile("LoginPage.json");
				logger.info("使用账号方式登陆");
				seleniumUtil.click(pageContext.getElementLocator("loginByAccount"));
				logger.info("切换用户登录表单");
				seleniumUtil.inFrame(pageContext.getElementLocator("loginFrame"));
				logger.info("输入用户名");
				seleniumUtil.clear(pageContext.getElementLocator("loginUsername"));
				seleniumUtil.type(pageContext.getElementLocator("loginUsername"), "helloxwr");
				logger.info("输入密码");
				seleniumUtil.clear(pageContext.getElementLocator("loginPassword"));
				seleniumUtil.type(pageContext.getElementLocator("loginPassword"), "Gmcc_1234");
				logger.info("点击登录按钮");
				seleniumUtil.click(pageContext.getElementLocator("loginButton"));
				logger.info("保存网站cookies");
				seleniumUtil.cookiesSaveInFile(testContext.getcookiesConfigFilePath());
			}
			logger.info("校验当前登陆用户信息");
			pageContext.setPagefile("PortalPage.json");
			String actual = seleniumUtil.getText(pageContext.getElementLocator("loginCurrentUser"));
			seleniumUtil.assertEquals(actual, "helloxwr@126.com");
			logger.info("登陆126邮箱网站成功");
		}catch(Exception e){
			logger.error("登陆126邮箱网站失败",e);
		}
	}

	@After(order=1)
	public void tearDown_screenshot(Scenario scenario) {
		// 对cucumber-report嵌入失败场景截图，allure-cucumber4-jvm也会自动附加scenario.embed的图片到allure-report
		try{
			if (scenario.isFailed()) {
				byte[] screenshotAs = null;
				screenshotAs = ((TakesScreenshot) testContext.getseleniumUtil().threadWebDriver.get()).getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshotAs, "image/png");
				logger.info("场景运行失败，已成功截图到测试报告中");
			}
		} catch (Exception e) {
			logger.error("场景运行失败，但截图到测试报告中时发生异常", e);
		}
	}
	
	@After(order=0)
	public void tearDown_closeBrowserAndVNC(Scenario scenario) {
		// 关闭浏览器、关闭VNC
		try{
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
		logger.info(scenario.getName() + "------------end");
		logger.info(scenario.getName() + "------------" + scenario.getStatus().name());
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
