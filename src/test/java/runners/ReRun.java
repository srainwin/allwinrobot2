package runners;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
		features="@target/result/rerun.txt",	//重跑文件的位置
		glue={"steps"},	//步骤定义文件位置，即实现feature代码的文件位置
		monochrome=true,	//控制台输出可读性
		plugin={	//输出报告格式
				"pretty",	//漂亮,使用其他颜色和堆栈跟踪打印 Gherkin源以查找错误;还有一种是"usage"能看每一步骤的时间消耗
				"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",	//生成allure报告
				"html:target/result/cucumber-reports/cucumber-pretty",	//生成html报告
				"json:target/result/cucumber-reports/CucumberTestReport.json",	//生成json报告，供第三方工具转换可视格式
				"junit:target/result/cucumber-reports/CucumberTestReport.xml" 	//生成xml报告，供第三方工具转换可视格式
		}
)

public class ReRun extends AbstractTestNGCucumberTests{

}