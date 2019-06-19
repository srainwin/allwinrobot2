package com.demo.cases.home;

import org.testng.annotations.Test;

import com.demo.base.LoginBase;
import com.demo.pagesoperation.HomeOperation;
import com.demo.pagesoperation.LoginOperation;

import io.qameta.allure.Description;

public class HomePage_007_Overview_LoginProtect extends LoginBase {

	/**
	 * @Description 首页测试用例007：总览快捷跳转登录保护
	 * @param expect
	 * @param itestcontext
	 * @throws Exception
	 */
	@Test(dataProvider = "testdata", description = "总览快捷跳转登录保护")
	@Description("操作步骤：" + "1、打开已登录页面；" + "2、默认进入首页Tab后点击总览登录保护；" + "预期结果：" + "1、成功登录，且当前用户名信息正确；"
			+ "2、成功跳转到设置Tab页并显示邮件登录保护相关内容；")
	public void overviewLoginProtect(String expect) throws Exception {
		// 打开已登录页面
		LoginOperation.loginFree(seleniumUtil, testurl, cookiesConfigFilePath);
		// 点击总览安全度
		HomeOperation.overviewLoginProtectClick(seleniumUtil);
		// 进入安全度断言
		HomeOperation.assertOverviewLoginProtectSign(seleniumUtil, expect);
	}
}
