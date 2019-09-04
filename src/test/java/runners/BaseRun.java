/***
 * @author xwr
 * @Description 由测试用例继承此基类。启动浏览器、关闭浏览器、扩展报告配置加载、提供测试数据、测试前后浏览器驱动清理。
*/
package runners;

import org.apache.log4j.Logger;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import cucumber.TestContext;
import cucumber.api.testng.AbstractTestNGCucumberTests;

public class BaseRun extends AbstractTestNGCucumberTests {
    Logger logger = Logger.getLogger(BaseRun.class.getName());
	TestContext context = new TestContext();

	/**
	 * @Description 在BeforeClass的setup前清理本地机残留的浏览器程序和driver进程，远程机暂不支持清理，另外还有ExtentReport初始化
	 * @param itestcontext
	 */
	@BeforeSuite
	public void setupCleanup() {
		killDriver();
	}

	/**
	 * @Description 在AfterClass的teardown后清理本地机残留的浏览器程序和driver进程，远程机暂不支持清理
	 * @param itestcontext
	 */
	@AfterSuite
	public void teardownCleanup() {
		killDriver();
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
		} catch (Exception e) {
			logger.error("清理driver进程发生异常", e);
		}
	}
	

	/**
	 * @Description 用于beforesuite和aftersuite清理服务器上的浏览器driver进程（备用方案）
	 */
	@SuppressWarnings("unused") //忽略never use警告
	private void killDriver2() {
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
		} catch (Exception e) {
			logger.error("清理driver进程发生异常", e);
		}
	}
}
