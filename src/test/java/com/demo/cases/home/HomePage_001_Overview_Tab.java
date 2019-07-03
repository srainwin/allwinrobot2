package com.demo.cases.home;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.demo.base.LoginBase;
import com.demo.pagesteps.HomeOperation;
import com.demo.pagesteps.LoginOperation;
import com.demo.utils.TestNGListener;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
@Listeners({ TestNGListener.class}) //用例监听，主要是失败用例截图功能
@Epic("126邮箱自动化测试实战") // allure用例组织，一级模块
@Feature("用户首页") // allure组织，二级模块
public class HomePage_001_Overview_Tab extends LoginBase {

	@Story("首页Tab") // allure用例组织，三级模块
	@Test(dataProvider = "testdata", description = "切换为首页Tab") // allure用例名是description
	@Description("登录邮箱后，点击切换为首页Tab") // allure用例描述
	@Severity(SeverityLevel.NORMAL) // allure用例重要等级
	public void overviewTabSwitch(String username, String expect) {
		// 打开已登录页面
		LoginOperation.loginFree(seleniumUtil, testurl, cookiesConfigFilePath);
		// 点击首页tab
		HomeOperation.homepageTabClick(seleniumUtil);
		// 进入首页tab断言
		HomeOperation.assertHomepageSign(seleniumUtil, "expect");//模仿断言失败看截图与重跑效果
	}
}