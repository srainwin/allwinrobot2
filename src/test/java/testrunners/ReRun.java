package testrunners;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
		features="@target/result/cucumber-reports/rerun.txt",	//重跑文件的位置
		glue={"steps"},	//步骤定义文件位置，即实现feature代码的文件位置
		monochrome=true,	//控制台输出可读性
		plugin={	//输出报告格式
				"pretty",	//漂亮,使用其他颜色和堆栈跟踪打印 Gherkin源以查找错误;还有一种是"usage"能看每一步骤的时间消耗
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:target/result/cucumber-extentreports/report.html",	//第三方extentreport工具生成扩展报告
				"html:target/result/cucumber-reports/cucumber-pretty",	//生成html报告
				"json:target/result/cucumber-reports/CucumberTestReport.json"	//生成json报告，供第三方工具转换可视格式
				}
		)

public class ReRun extends AbstractTestNGCucumberTests{

}
//public class TestRun extends TestCaseBase{
//
//}