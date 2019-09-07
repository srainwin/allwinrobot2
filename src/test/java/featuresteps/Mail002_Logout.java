package featuresteps;

import java.io.File;

import org.apache.log4j.Logger;

import cucumber.PageContext;
import cucumber.TestContext;
import cucumber.api.java.zh_cn.假如;
import cucumber.api.java.zh_cn.那么;
import selenium.SeleniumUtil;
import sikuli.SikuliUtil;

public class Mail002_Logout {
	Logger logger = Logger.getLogger(Mail001_Login.class.getSimpleName());
	TestContext testContext;
	PageContext pageContext;
	SeleniumUtil seleniumUtil;
	SikuliUtil sikuliUtil;
	
	public Mail002_Logout(TestContext context) {
		testContext = context;
		pageContext = testContext.getpageContext();
		seleniumUtil = testContext.getseleniumUtil();
		sikuliUtil = testContext.getsikuliUtil();
	}
	
	//场景: 126邮箱正常登出
	@假如("^在已登录的门户页面点击退出按钮$")
	public void logout_step1() {
		logger.info("logout_step1:在已登录的门户页面点击退出按钮");
		pageContext.setPagefile("portalPage.json");
		seleniumUtil.click(pageContext.getElementLocator("logoutButton"));
		File file = new File(testContext.getcookiesConfigFilePath());
		if(file.exists()) {
			file.delete();
		}
		
	}

	@那么("^页面跳转到已退出邮箱页面并显示【您已成功退出网易邮箱】$")
	public void logout_step2() {
		logger.info("logout_step2:页面跳转到已退出邮箱页面并显示【您已成功退出网易邮箱】");
		pageContext.setPagefile("logoutPage.json");
		String actual = seleniumUtil.getText(pageContext.getElementLocator("logoutInfo"));
		seleniumUtil.assertEquals(actual, "您已成功退出网易邮箱。");
	}
}