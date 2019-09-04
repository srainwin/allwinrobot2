package steps;

import java.util.Random;

import org.testng.Assert;

import cucumber.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestExample2 {
	TestContext testContext;
	String browserName;
	
	public TestExample2(TestContext context) {
		testContext = context;
		browserName = testContext.getbrowserName();
	}
	
	@Given("^Open Application and Enter url2$")
	public void open_Application_and_Enter_url2() throws InterruptedException {
//		Thread.sleep(2000);
		System.out.println("Open Application and Enter url2");
		System.out.println(browserName);
	}

	@When("^enter username2$")
	public void enter_username2() {
		System.out.println("enter username2");

		System.out.println(testContext.getcustomContext().getContext("username"));
		testContext.getcustomContext().addCoverContext("username", "Allwin3");
		System.out.println(testContext.getcustomContext().getContext("username"));
		testContext.getcustomContext().delContext("username");
		testContext.getcustomContext().addCoverContext("123", "123");
	}

	@When("^enter password2$")
	public void enter_password2() {
		System.out.println("enter password2");
		int min = 1;
        int max = 3;
        int val = new Random().nextInt(max-min)+min;
        System.out.println(val);
        Assert.assertEquals(1, val);
	}

	@Then("^verify Msg2$")
	public void verify_Msg2() {
		System.out.println("verify Msg2");
	}
}

