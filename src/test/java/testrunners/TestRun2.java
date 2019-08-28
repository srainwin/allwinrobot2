package testrunners;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.BeforeClass;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
		features="src/test/java/features",	//feature文件位置
		glue={"steps"},	//步骤定义文件位置，即实现feature代码的文件位置
		monochrome=true,	//控制台输出可读性
		plugin={	//输出报告格式
				"pretty",	//漂亮,使用其他颜色和堆栈跟踪打印 Gherkin源以查找错误;还有一种是"usage"能看每一步骤的时间消耗
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",	//生成extentreport扩展报告
				"html:target/result/cucumber-reports/cucumber-pretty",	//生成html报告
				"json:target/result/cucumber-reports/CucumberTestReport.json",	//生成json报告，供第三方工具转换可视格式
				"junit:target/result/cucumber-reports/CucumberTestReport.xml",	//生成xml报告，供第三方工具转换可视格式
				"rerun:target/result/rerun.txt"	//生成失败场景需重跑的文件
		},
		tags = {"@featureGroup1","@scenarioGroup1,@scenarioGroup2"}
)

public class TestRun2 extends AbstractTestNGCucumberTests{
	@BeforeClass
	public void setup(){
		try{
			Properties prop = new Properties();
			FileInputStream fileInput = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config/extent.properties");
			prop.load(fileInput);
			System.out.println("ExtentReport初始化属性如下：");
			for(Object key:prop.keySet()){
				System.out.println(key.toString() + "=" + prop.get(key.toString()));
				System.setProperty(key.toString(), prop.get(key).toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
