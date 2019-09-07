package featuresteps;

import java.util.Random;

import org.testng.Assert;

import cucumber.TestContext;
import cucumber.api.java.zh_cn.假如;
import cucumber.api.java.zh_cn.当;
import cucumber.api.java.zh_cn.那么;
import database.JdbcUtil;

public class TestExample {
	TestContext testContext;
	String browserName;
	
	public TestExample(TestContext context) {
		testContext = context;
		browserName = testContext.getbrowserName();
	}
	
	@假如("^打开软件并输入地址\"(.*)\"和\"(.*)\"和\"(.*)\"$")
	public void open_Application_and_Enter_url(String username,String password,String tips) throws InterruptedException {
		System.out.println(username+password+tips);
		JdbcUtil j = new JdbcUtil();
		System.out.println(j.excuteQuery("select id,name from jdbctab1", "id"));
		j.closeConn();
		
		
//		Thread.sleep(2000);
		System.out.println("Open Application and Enter url");
		
		browserName = "firefox";
		System.out.println(browserName);
		System.out.println(testContext.getbrowserName());
		
		String username2 = "Allwin";
		testContext.getcustomContext().addContext("username", username2);
	}

	@当("^enter username$")
	public void enter_username() {
		System.out.println("enter username");
		System.out.println(browserName);
		
		System.out.println(testContext.getcustomContext().getContext("username"));
		
	}

	@当("^enter password$")
	public void enter_password() {
		System.out.println("enter password");
		

		testContext.getcustomContext().addContext("password", "123456");
		testContext.getcustomContext().setContext("username", "Allwin2");
		System.out.println(testContext.getcustomContext().getContext("username"));
		
		int min = 1;
        int max = 3;
        int val = new Random().nextInt(max-min)+min;
        System.out.println(val);
        Assert.assertEquals(1, val);
	}

	@那么("^verify Msg$")
	public void verify_Msg() {
		System.out.println("verify Msg");
	}
	
	@假如("^Open Application and Enter url1$")
	public void open_Application_and_Enter_url1() throws InterruptedException {
//		Thread.sleep(2000);
		System.out.println("Open Application and Enter url1");
		
		System.out.println(browserName);
		System.out.println(testContext.getbrowserName());
	}

	@当("^enter username1$")
	public void enter_username1() {
		System.out.println("enter username1");
		
		System.out.println(testContext.getcustomContext().getContext("username"));
	}

	@当("^enter password1$")
	public void enter_password1() {
		System.out.println("enter password1");
		int min = 1;
        int max = 3;
        int val = new Random().nextInt(max-min)+min;
        System.out.println(val);
        Assert.assertEquals(1, val);
	}

	@那么("^verify Msg1$")
	public void verify_Msg1() {
		System.out.println("verify Msg1");
	}
}
