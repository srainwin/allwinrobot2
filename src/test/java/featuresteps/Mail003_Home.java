package featuresteps;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cucumber.PageContext;
import cucumber.TestContext;
import cucumber.api.java.zh_cn.假如;
import cucumber.api.java.zh_cn.那么;
import io.cucumber.datatable.DataTable;
import selenium.SeleniumUtil;
import sikuli.SikuliUtil;

public class Mail003_Home {
	Logger logger = Logger.getLogger(Mail003_Home.class.getSimpleName());
	TestContext testContext;
	PageContext pageContext;
	SeleniumUtil seleniumUtil;
	SikuliUtil sikuliUtil;
	
	public Mail003_Home(TestContext context) {
		testContext = context;
		pageContext = testContext.getpageContext();
		seleniumUtil = testContext.getseleniumUtil();
		sikuliUtil = testContext.getsikuliUtil();
	}
	
	// 背景: 在门户中选择首页Tab
	@假如("^点击首页Tab$")
	public void homeTab_step1() {
		logger.info("homeTab_step1:点击切换首页tab");
		pageContext.setPagefile("portalPage.json");
		seleniumUtil.click(pageContext.getElementLocator("homeTab"));
	}

	@那么("^成功打开首页Tab$")
	public void homeTab_step2() {
		logger.info("homeTab_step2:成功打开首页");
		pageContext.setPagefile("HomePage.json");
		String actual = seleniumUtil.getText(pageContext.getElementLocator("homeTabSign"));
		seleniumUtil.assertEquals(actual, "helloxwr");
	}

	// 场景大纲: 进入用户概览的未读邮件
	@假如("^在用户概览处点击未读邮件$")
	public void homeUnreadMail_step1() {
	    logger.info("homeUnreadMail_step1:在用户概览处点击未读邮件");
	    seleniumUtil.click(pageContext.getElementLocator("overviewUnreadMail"));
	}

	@那么("^成功打开未读邮件Tab并正常显示$")
	public void homeUnreadMail_step2(DataTable datatable) {
		logger.info("homeUnreadMail_step2:成功打开未读邮件Tab并正常显示");
		String actual = seleniumUtil.getAttributeText(pageContext.getElementLocator("overviewUnreadMailTab"), "title");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewUnreadMailTab"); 
		seleniumUtil.assertEquals(actual, expect);
	}

	// 场景大纲: 进入用户概览的待办邮件
	@假如("^在用户概览处点击待办邮件$")
	public void homeTodoMail_step1() {
		logger.info("homeTodoMail_step1:在用户概览处点击待办邮件");
		seleniumUtil.click(pageContext.getElementLocator("overviewTodoMail"));
	}

	@那么("^成功打开待办邮件Tab并正常显示$")
	public void homeTodoMail_step2(DataTable datatable) {
		logger.info("homeTodoMail_step2:成功打开待办邮件Tab并正常显示");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewTodoMailTab"); 
		String actual = seleniumUtil.getAttributeText(pageContext.getElementLocator("overviewTodoMailTab"), "title");
		seleniumUtil.assertEquals(actual, expect);
	}

	// 场景大纲: 进入用户概览的联系人邮件
	@假如("^在用户概览处点击联系人邮件$")
	public void homeContactMail_step1() {
		logger.info("homeContactMail_step1:在用户概览处点击联系人邮件");
		seleniumUtil.click(pageContext.getElementLocator("overviewContactMail"));
	}

	@那么("^成功打开联系人邮件Tab并正常显示$")
	public void homeContactMail_step2(DataTable datatable) {
		logger.info("homeContactMail_step2:成功打开联系人邮件Tab并正常显示");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewContactMailTab"); 
		String actual = seleniumUtil.getAttributeText(pageContext.getElementLocator("overviewContactMailTab"), "title");
		seleniumUtil.assertEquals(actual, expect);
	}

	// 场景大纲: 进入用户概览的安全度
	@假如("^在用户概览处点击安全度$")
	public void homeSafetyDegree_step1() {
		logger.info("homeSafetyDegree_step1:在用户概览处点击安全度");
		seleniumUtil.click(pageContext.getElementLocator("overviewSafetyDegree"));
	}

	@那么("^成功打开设置Tab并正常显示邮箱安全设置内容$")
	public void homeSafetyDegree_setp2(DataTable datatable) {
		logger.info("homeSafetyDegree_step2:成功打开设置Tab并正常显示邮箱安全设置内容");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewSafetyDegreeSign"); 
		String actual = seleniumUtil.getText(pageContext.getElementLocator("overviewSafetyDegreeSign"));
		seleniumUtil.assertEquals(actual, expect);
	}

	// 场景大纲: 进入用户概览的登录保护
	@假如("^在用户概览处点击登录保护$")
	public void homeLoginProtect_step1() {
		logger.info("homeLoginProtect_step1:在用户概览处点击登录保护");
		seleniumUtil.click(pageContext.getElementLocator("overviewLoginProtect"));
	}

	@那么("^成功打开设置Tab并正常显示邮箱登录二次验证内容$")
	public void homeLoginProtect_step2(DataTable datatable) {
		logger.info("homeLoginProtect_step2:成功打开设置Tab并正常显示邮箱登录二次验证内容");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewLoginProtectSign"); 
		seleniumUtil.inFrame(pageContext.getElementLocator("overviewLoginProtectFrame"));
		String actual = seleniumUtil.getText(pageContext.getElementLocator("overviewLoginProtectSign"));
		seleniumUtil.assertEquals(actual, expect);
	}

	// 场景大纲: 进入用户概览的每日生活
	@假如("^在用户概览处点击每日生活$")
	public void homeDailyLife_step1() {
		logger.info("homeDailyLife_step1:在用户概览处点击每日生活");
		seleniumUtil.click(pageContext.getElementLocator("overviewDailyLife"));
	}

	@那么("^成功打开严选每日推荐Tab并正常显示广告推荐内容$")
	public void homeDailyLife_step2(DataTable datatable) {
		logger.info("homeDailyLife_step2:成功打开严选每日推荐Tab并正常显示广告推荐内容");
		List<Map<String,String>> data = datatable.asMaps();  
		String expect = data.get(0).get("overviewDailyLifeTab"); 
		String actual = seleniumUtil.getAttributeText(pageContext.getElementLocator("overviewDailyLifeTab"), "title");
		seleniumUtil.assertEquals(actual, expect);
	}
}
