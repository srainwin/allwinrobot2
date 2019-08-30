package steps;

import java.util.Random;

import org.testng.Assert;

import cucumber.Context;
import cucumber.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Test {
	TestContext testContext;
	String browserName;
	
	public Test(TestContext context) {
		testContext = context;
		browserName = testContext.getbrowserName();
	}
	
	@Given("^Open Application and Enter url$")
	public void open_Application_and_Enter_url() {
		System.out.println("Open Application and Enter url");
		browserName = "firefox";
		System.out.println(browserName);

		testContext.getcustomContext().putContext(Context.username, "Allwin");
	}

	@When("^enter username$")
	public void enter_username() {
		System.out.println("enter username");
		System.out.println(browserName);
		
		System.out.println(testContext.getcustomContext().getContext(Context.username));
		
	}

	@When("^enter password$")
	public void enter_password() {
		System.out.println("enter password");
		int min = 1;
        int max = 3;
        int val = new Random().nextInt(max-min)+min;
        System.out.println(val);
        Assert.assertEquals(1, val);
	}

	@Then("^verify Msg$")
	public void verify_Msg() {
		System.out.println("verify Msg");
	}
	
	@Given("^Open Application and Enter url1$")
	public void open_Application_and_Enter_url1() {
		System.out.println("Open Application and Enter url1");
		System.out.println(browserName);
	}

	@When("^enter username1$")
	public void enter_username1() {
		System.out.println("enter username1");
		
		System.out.println(testContext.getcustomContext().getContext(Context.username));
	}

	@When("^enter password1$")
	public void enter_password1() {
		System.out.println("enter password1");
		int min = 1;
        int max = 3;
        int val = new Random().nextInt(max-min)+min;
        System.out.println(val);
        Assert.assertEquals(1, val);
	}

	@Then("^verify Msg1$")
	public void verify_Msg1() {
		System.out.println("verify Msg1");
	}
}
