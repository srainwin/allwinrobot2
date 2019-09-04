package runners;

import cucumber.api.CucumberOptions;

@CucumberOptions(
		features="src/test/java/features",	//feature文件位置
		glue={"steps"},	//步骤定义文件位置，即实现feature代码的文件位置
		monochrome=true,	//控制台输出可读性
		plugin={	//输出报告格式
				"pretty",	//漂亮,使用其他颜色和堆栈跟踪打印 Gherkin源以查找错误;还有一种是"usage"能看每一步骤的时间消耗
				"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",	//生成allure报告
				"html:target/result/cucumber-reports/cucumber-pretty",	//生成html报告
				"json:target/result/cucumber-reports/CucumberTestReport.json",	//生成json报告，供第三方工具转换可视格式
				"junit:target/result/cucumber-reports/CucumberTestReport.xml",	//生成xml报告，供第三方工具转换可视格式
				"rerun:target/result/rerun.txt"	//生成失败场景需重跑的文件
		},
//		tags = {"@featureGroup1 and (@scenarioGroup0 or @scenarioGroup1 or @scenarioGroup2)"}
		tags = {"@scenarioGroup0"}
)

public class TestRun extends BaseRun{
	
}