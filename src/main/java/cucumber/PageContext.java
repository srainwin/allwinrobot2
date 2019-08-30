package cucumber;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.jayway.jsonpath.JsonPath;

/**
 * @author XWR
 * @Description 解析json形式的页面对象文件，以此获取selenium元素定位或sikuli图像名字，也可通过测试上下文来获取
 */
public class PageContext {
	private File jsonPageFile;
	private Logger logger;

	public PageContext() {
		logger = Logger.getLogger(PageContext.class.getName());
		}
	
	public void setPagefile(String jsonPagefileName){
		jsonPageFile = new File(System.getProperty("user.dir") + "/src/main/java/com/demo/pages/" + jsonPagefileName);
	}

	/**
	 * @Description 获取selenium元素定位
	 * @param locatorName json文件里的key名字
	 * @return
	 */
	public By getElementLocator(String locatorName){
		By locator = null;
		try{
			/*jsonpath提供了类似正则表达式的语法
			  $是从根元素开始查询
			  ..是深沉扫描子孙元素
			  locatorName是需要匹配的元素名
			  [1]是匹配到的元素名对应的值数组中的第1+1个值
			  $..locatorName[1]正则表达式是获取json中locatorName数组的第2个值
			 */
			List<String> locatorTypeList = JsonPath.read(jsonPageFile, "$.." + locatorName + "[1]"); 
			List<String> locatorValueList = JsonPath.read(jsonPageFile, "$.." + locatorName + "[2]");
			String locatorType = locatorTypeList.get(0).toLowerCase();
			String locatorValue = locatorValueList.get(0);

			switch (locatorType) {
			//id、name、className、tagName、linkText、partialLinkText、xpath、cssSelector、imageName
			case "id":
				locator = By.id(locatorValue);
				break;
			case "name":
				locator = By.name(locatorValue);
				break;
			case "classname":
				locator = By.className(locatorValue);
				break;
			case "tagname":
				locator = By.tagName(locatorValue);
				break;
			case "linktext":
				locator = By.linkText(locatorValue);
				break;
			case "partiallinktext":
				locator = By.partialLinkText(locatorValue);
				break;
			case "xpath":
				locator = By.xpath(locatorValue);
				break;
			case "cssselector":
				locator = By.cssSelector(locatorValue);
				break;
			}
		}catch(Exception e){
			logger.error("获取元素定位值发生异常",e);
		}
		return locator;
	}
	
	/**
	 * @Description 获取sikuli图像名字
	 * @param locatorName json文件里的key名字
	 * @return
	 */
	public String getImageLocator(String locatorName) {
		try{
			List<String> locatorTypeList = JsonPath.read(jsonPageFile, "$.." + locatorName + "[1]");
			List<String> locatorValueList = JsonPath.read(jsonPageFile, "$.." + locatorName + "[2]");
			String locatorType = locatorTypeList.get(0).toLowerCase();
			String locatorValue = locatorValueList.get(0);
			if("imagename".equals(locatorType)){
				return locatorValue;
			}else{
				logger.warn("定位的类型非imagename，请检查！");
				return null;
			}
		}catch(Exception e){
			logger.error("获取图像定位值发生异常", e);
			return null;
		}
	}
}
