package listeners;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * @author XWR
 * @Description 测试用例监听配置，暂时作用是生成report时移除重跑的结果,避免report生成多余用例数
 */
public class TestNGListener extends TestListenerAdapter {
	public Logger logger = Logger.getLogger(TestNGListener.class.getName());
	
	/* 测试用例级别，在测试类被实例化之前调用 */
	@Override
	public void onTestStart(ITestResult result) {
		super.onTestStart(result);
		logger.info(result.getName() + "-------------------Start-------------------");
	}
	
	/* 测试用例级别，每次测试成功时调用 */
	@Override
	public void onTestSuccess(ITestResult result) {
		super.onTestSuccess(result);
		logger.info(result.getName() + "-------------------End:Success-------------------");
	}

	/* 测试用例级别，每次测试失败时调用 */
	@Override
	public void onTestFailure(ITestResult result) {
		super.onTestFailure(result);
		logger.info(result.getName() + "-------------------End:Failure-------------------");
	}

	/* 测试用例级别，每次跳过测试时调用 */
	@Override
	public void onTestSkipped(ITestResult result) {
		super.onTestSkipped(result);
		logger.info(result.getName() + "-------------------End:Skipped-------------------");
	}

	/* 测试集级别，在所有测试运行之前调用 */
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
	}

	/* 测试集级别，在所有测试运行之后调用 */
	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		// 生成report时，移除重跑的结果，避免report生成多余用例数
		Iterator<ITestResult> listOfFailedTests = testContext.getFailedTests().getAllResults().iterator();
		while (listOfFailedTests.hasNext()) {
			ITestResult failedTest = (ITestResult) listOfFailedTests.next();
			ITestNGMethod method = failedTest.getMethod();
			if (testContext.getFailedTests().getResults(method).size() > 1) {
				listOfFailedTests.remove();
			} else {
				if (testContext.getPassedTests().getResults(method).size() > 0) {
					listOfFailedTests.remove();
				}
			}
		}
	}
}