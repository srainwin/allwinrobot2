/***
 * @author xwr
 * @Description 由测试用例继承此基类。启动浏览器、关闭浏览器、扩展报告配置加载、提供测试数据、测试前后浏览器驱动清理。
*/
package runners;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import cucumber.TestContext;
import cucumber.api.testng.AbstractTestNGCucumberTests;

public class BaseRun extends AbstractTestNGCucumberTests {
    
	public TestContext context = new TestContext();

	/**
	 * @Description 清理本地机残留的浏览器程序和driver进程，远程机暂不支持清理，另外还有清理测试报告和上次测试留下的数据库脏数据
	 * 				BeforeSuite不能用logger记录日志，只能用system.out.println，因为日志初始化在hooks的before
	 * @param itestcontext
	 */
	@BeforeSuite
	public void setupCleanup() {
		// 清理本地机残留的浏览器程序和driver进程
		System.out.println("开始清理本地机残留的浏览器程序和driver进程");
		killDriver();
		
		// 清理Allure测试报告旧数据
		System.out.println("开始清理Allure测试报告旧数据");
		cleanAllureReport();
		
		// 清理上次测试留下的数据库脏数据（使用JdbcUtil类，testContext.getjdbcUtil()）
		//System.out.println("开始清理上次测试留下的数据库脏数据");
		//try{}catch(Exception e){}
	}

	/**
	 * @Description 清理本地机残留的浏览器程序和driver进程，远程机暂不支持清理，另外还有清理cookies文件
	 * @param itestcontext
	 */
	@AfterSuite
	public void teardownCleanup() {
		Logger logger = Logger.getLogger(BaseRun.class.getName());
		// 清理本地机残留的浏览器程序和driver进程
		logger.info("清理本地机残留的浏览器程序和driver进程");
		killDriver();
		
		// 自动化测试整体结束时删除临时cookies文件
		deleteCookiesFile();
		
		// 关闭数据库连接
		context.getjdbcUtil().closeConn();
	}
	
	/**
	 * 为cucumber开启并行运行scenario功能（cucumber-jvm4.0.0以上版本才有并行功能）
	 * 默认提供线程数为10，需要自定义线程数要在pom文件的maven-surefire-plugin里设置dataproviderthreadcount属性
	 * 不要使用testng.xml里配置的并发，会出问题的，因为cucumber和testng的运行方式始终是有区别，只可用cucumber自带的并行功能
	 */
	@Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
	
	/**
	 * @Description 用于beforesuite和aftersuite清理服务器上的浏览器driver进程
	 */
	private void killDriver() {
		try {
			String browserName = context.getbrowserName();
			Runtime rn = Runtime.getRuntime();
			if (browserName.equalsIgnoreCase("ie")) {
				// 杀遗留旧进程
				rn.exec("taskkill /t /f /im iexplore.exe");
				rn.exec("taskkill /t /f /im IEDriverServer.exe");

			} else if (browserName.equalsIgnoreCase("chrome")) {
				// 杀遗留旧进程
				rn.exec("taskkill /t /f /im chrome.exe");
				rn.exec("taskkill /t /f /im chromedriver.exe");

			} else if (browserName.equalsIgnoreCase("firefox")) {
				// 杀遗留旧进程
				rn.exec("taskkill /t /f /im firefox.exe");
				rn.exec("taskkill /t /f /im geckodriver.exe");

			} else if (browserName.equalsIgnoreCase("ghost")) {
				// 杀遗留旧进程
				rn.exec("taskkill /t /f /im phantomjs.exe");
			} else {
				System.out.println(browserName + "浏览器不支持，支持ie、chrome、firefox和ghost，将默认使用chrome浏览器进行");
				// 杀遗留旧进程
				rn.exec("taskkill /t /f /im chrome.exe");
				rn.exec("taskkill /t /f /im chromedriver.exe");
			}
			System.out.println("成功清理本地机残留的浏览器程序和driver进程");
		} catch (Exception e) {
			System.out.println("清理本地机残留的浏览器程序和driver进程发生异常");
			e.printStackTrace();
		}
	}
	

	/**
	 * @Description 用于beforesuite和aftersuite清理服务器上的浏览器driver进程（备用方案）
	 */
	@SuppressWarnings("unused") //忽略never use警告
	private void killDriver2() {
		Logger logger = Logger.getLogger(BaseRun.class.getName());
		try {
			String browserName = context.getbrowserName();
			if (browserName.equalsIgnoreCase("ie")) {
				// 杀遗留旧进程
				WindowsUtils.killByName("iexplore.exe");
				WindowsUtils.killByName("IEDriverServer.exe");

			} else if (browserName.equalsIgnoreCase("chrome")) {
				// 杀遗留旧进程
				WindowsUtils.killByName("chrome.exe");
				WindowsUtils.killByName("chromedriver.exe");

			} else if (browserName.equalsIgnoreCase("firefox")) {
				// 杀遗留旧进程
				WindowsUtils.killByName("firefox.exe");
				WindowsUtils.killByName("geckodriver.exe");

			} else if (browserName.equalsIgnoreCase("ghost")) {
				// 杀遗留旧进程
				WindowsUtils.killByName("phantomjs.exe");
			} else {
				logger.warn(browserName + "浏览器不支持，支持ie、chrome、firefox和ghost，将默认使用chrome浏览器进行");
				// 杀遗留旧进程
				WindowsUtils.killByName("chrome.exe");
				WindowsUtils.killByName("chromedriver.exe");
			}
			System.out.println("成功清理本地机残留的浏览器程序和driver进程");
		} catch (Exception e) {
			System.out.println("清理本地机残留的浏览器程序和driver进程发生异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description 自动化测试整体结束时删除cookies文件
	 */
	public void deleteCookiesFile(){
		Logger logger = Logger.getLogger(BaseRun.class.getName());
		boolean result = false; 
		try{
			TestContext testContext = new TestContext();
			File file = new File(testContext.getcookiesConfigFilePath());
			if(file.exists()) {
				result = file.delete();
			}
			if(result){
				logger.info("自动化测试已结束，成功删除cookies临时文件");
			}else{
				logger.warn("自动化测试已结束，但删除cookies临时文件失败，可能文件本身不存在或其他原因，请检查是否有影响");
			}
		}catch(Exception e){
			logger.error("自动化测试已结束，但删除cookies临时文件失败",e);
		}
	}
	
	/**
	 * @Description 清理Allure测试报告旧数据
	 */
	public void cleanAllureReport(){
		try{
			TestContext testContext = new TestContext();
			if("true".equals(testContext.getisCleanAllureReport().toLowerCase())){
				File file = new File(System.getProperty("user.dir") + "/target/result/allure-results");
				if(file.exists()){
					// File只能删文件和空目录，非空目录要递归深度删除
					delFile(file);
					System.out.println("成功清理Allure测试报告旧数据");
				}else{
					System.out.println("Allure测试报告本身不存在，不需要清理");
				}
			}else{
				System.out.println("已确认Allure测试报告旧数据不清理");
			}
		}catch(Exception e){
			System.out.println("清理Allure测试报告旧数据发生异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description 删除文件功能，需要递归因此独立一个方法出来
	 * @param file
	 */
	private static void delFile(File file){
		try{
		// File只能删文件和空目录，非空目录要递归深度删除
		if (file.isFile()) {
			file.delete();// 文件删除
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();// 空子目录删除
			} else {
				for (int i = 0; i < files.length; i++) {
					delFile(files[i]);// 递归深度删除子孙文件和目录
				}
				file.delete();// 空父目录删除
			}
		}
		}catch(Exception e){
			System.out.println("删除文件发生异常");
			e.printStackTrace();
		}
	}
}
