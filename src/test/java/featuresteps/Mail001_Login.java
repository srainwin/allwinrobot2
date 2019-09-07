package featuresteps;

import org.apache.log4j.Logger;

import cucumber.PageContext;
import cucumber.TestContext;
import cucumber.api.java.zh_cn.假如;
import cucumber.api.java.zh_cn.当;
import cucumber.api.java.zh_cn.而且;
import cucumber.api.java.zh_cn.那么;
import selenium.SeleniumUtil;
import sikuli.SikuliUtil;

public class Mail001_Login {
	Logger logger = Logger.getLogger(Mail001_Login.class.getSimpleName());
	TestContext testContext;
	PageContext pageContext;
	SeleniumUtil seleniumUtil;
	SikuliUtil sikuliUtil;
	
	public Mail001_Login(TestContext context) {
		testContext = context;
		pageContext = testContext.getpageContext();
		seleniumUtil = testContext.getseleniumUtil();
		sikuliUtil = testContext.getsikuliUtil();
	}

	//场景大纲: 正确/错误/空用户名和正确/错误/空的密码登录126邮箱
	@假如("^进入登陆页面$")
	public void loginFail_step1(){
		logger.info("进入登陆页面");
		seleniumUtil.get(testContext.gettesturl());
	}
	@当("^输入正确错误空用户名\"(.*)\"$")
	public void loginFail_step2(String username) {
		logger.info("输入正确|错误|空用户名");
		pageContext.setPagefile("loginPage.json");
		seleniumUtil.click(pageContext.getElementLocator("loginByAccount"));
		seleniumUtil.inFrame(pageContext.getElementLocator("loginFrame"));
		seleniumUtil.clear(pageContext.getElementLocator("loginUsername"));
		seleniumUtil.type(pageContext.getElementLocator("loginUsername"), username);
		
	}

	@而且("^输入正确错误空密码\"(.*)\"$")
	public void loginFail_step3(String password) {
		logger.info("输入正确|错误|空密码");
		pageContext.setPagefile("loginPage.json");
		seleniumUtil.clear(pageContext.getElementLocator("loginPassword"));
		seleniumUtil.type(pageContext.getElementLocator("loginPassword"), password);
		
	}

	@那么("^点击登录提示相关信息\"(.*)\"$")
	public void loginFail_step4(String tips) {
		logger.info("点击登录提示相关信息");
		pageContext.setPagefile("loginPage.json");
		seleniumUtil.click(pageContext.getElementLocator("loginButton"));
		String actual = seleniumUtil.getText(pageContext.getElementLocator("loginErrorInfo"));
		seleniumUtil.assertEquals(actual, tips);
		
	}
}
