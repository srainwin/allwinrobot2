package cucumber;

import java.io.FileReader;
import java.util.Properties;

import selenium.SeleniumUtil;
import sikuli.SikuliUtil;

/**
 * @author XWR
 * @Description 共享测试上下文（共享变量），共享内容例如webdriver、page object、other variable、等等。
 * 				
 * 原理：cucumber-picocontainer容器能实例化每个步骤定义类(包括Hooks)并正确连接它们，
 * 		cucumber使用步骤定义扫描您的类，将它们传递给PicoContainer，
 * 		然后要求它为每个场景创建新实例，通过Step Definition类的构造函数把这些实例正确地连接在一起。
 * 					  
 * 		换个说法，就是类似于testng的每个@Test方法都可以加上ITestContext类为形式参数来获取测试上下文，
 *      而testng的测试上下文是早已内置好了，但是cucumber自身没有内置测试上下文，
 *      所以cucumber需要先自己建造测试上下文内容的类TestContext，
 *      再通过每个Step Definition类编写构造函数加上TestContext为形式参数，
 *      最后借助picocontainer把每个场景（picocontainer会知道哪些步骤方法是合为一个场景）实例化来传递TestContext，从而达到共享测试上下文。
 * 				 
 * 一般做法：
 * 		第1步：将PicoContainer库添加到Maven项目
 * 		第2步：创建一个Test Context类
 * 		第3步：划分步骤文件
 * 		第4步：为Step Definition类编写构造函数以共享TestContext
 * 
 * 规则：共享变量可以跨step、跨feature、跨scenario共享
 * 		共享变量不可变，自定义共享变量可变
 * 		
 */
public class TestContext {
	//默认必须有共享变量
	private static SeleniumUtil seleniumUtil; // seleniumUtil对象的driver成员变量最终会通过运行launchBrowser成员方法获取对应浏览器驱动webdriver，然后每个场景都使用这个共享webdriver对象
	private static SikuliUtil sikuliUtil; // sikuliUtil对象的Screen成员变量最终会通过运行launchScreen成员方法获取屏幕Screen，然后每个场景都使用这个共享screen对象
	private String browserName;
	private String testurl;
	private int pageLoadTimeout;
	private String logRootFolderPath;
	private String keepLogDay;
	private String cookiesConfigFilePath;
	private String testDataFilePath;
	private String sikuliImageFolderPath;
	private String autoitFolderPath;
	private String driverConfigFilePath;
	private String isRemote;
	private String huburl;
	private String isVNC;
	private String vncPassword;
	
	//page共享变量（page的上下文内容均在pages目录的json文件中，通过JsonPage）
	private PageContext pageContext;
	
	//自定义测试上下文的共享变量(自定义的上下文内容写在Context.java枚举类中即可，CustomContext只负责设置和获取)
	private CustomContext customContext ;
	
	//构造方法负责实例化共享变量，由于定义都是private权限，所以使用时用对应get方法
	public TestContext(){
		
		//基础共享变量，程序正常运行必须，需在BaseContext.properties文件中更改维护
		seleniumUtil = new SeleniumUtil();
		sikuliUtil = new SikuliUtil();
		browserName = key2value("browserName");
		testurl = key2value("testurl");
		pageLoadTimeout = Integer.parseInt(key2value("pageLoadTimeout"));
		logRootFolderPath = key2value("logRootFolderPath");
		keepLogDay = key2value("keepLogDay");
		cookiesConfigFilePath = key2value("cookiesConfigFilePath");
		testDataFilePath = key2value("testDataFilePath");
		sikuliImageFolderPath = key2value("sikuliImageFolderPath");
		autoitFolderPath = key2value("autoitFolderPath");
		driverConfigFilePath = key2value("driverConfigFilePath");
		isRemote = key2value("isRemote");
		huburl = key2value("huburl");
		isVNC = key2value("isVNC");
		vncPassword = key2value("vncPassword");
		
		//page共享变量，需在pages目录中json形式的page文件更改维护
		pageContext = new PageContext();
		
		//自定义测试上下文的共享变量，需在Context.java枚举类中更改维护
		customContext = new CustomContext();
		
	}
	
	private String key2value(String key){
		String value = "";
		try{
			Properties properties = new Properties();
			// 读取BaseContext.properties文件
			FileReader filereader = new FileReader(System.getProperty("user.dir") + "/src/main/resources/config/BaseContext.properties");
			properties.load(filereader);
			// 获取key对应的value值
			value = properties.getProperty(key);
		}catch(Exception e){
			if(value.equals("")){
				System.out.println("[error]:BaseContext.properties文件没有" + key + "对应的值");
			}
			e.printStackTrace();
		}
		return value;
		
	}
	
	public SeleniumUtil getseleniumUtil(){
		return seleniumUtil;
	}
	
	public SikuliUtil getsikuliUtil(){
		return sikuliUtil;
	}
	
	public String getbrowserName(){
		return browserName;
	}
	
	public String gettesturl(){
		return testurl;
	}
	
	public int getpageLoadTimeout(){
		return pageLoadTimeout;
	}
	
	public String getlogRootFolderPath(){
		return logRootFolderPath;
	}
	
	public String getkeepLogDay(){
		return keepLogDay;
	}
	
	public String getcookiesConfigFilePath(){
		return cookiesConfigFilePath;
	}
	
	public String gettestDataFilePath(){
		return testDataFilePath;
	}
	
	public String getsikuliImageFolderPath(){
		return sikuliImageFolderPath;
	}
	
	public String getautoitFolderPath(){
		return autoitFolderPath;
	}
	
	public String getdriverConfigFilePath(){
		return driverConfigFilePath;
	}
	
	public String getisRemote(){
		return isRemote;
	}
	
	public String gethuburl(){
		return huburl;
	}
	
	public String getisVNC(){
		return isVNC;
	}
	
	public String getvncPassword(){
		return vncPassword;
	}
	
	public CustomContext getcustomContext(){
		return customContext;
	}
	
	public PageContext pageContext(){
		return pageContext;
	}
	
}
