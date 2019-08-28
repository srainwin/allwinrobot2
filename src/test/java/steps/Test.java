package steps;

import java.util.Random;

import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Test {
	@Given("^Open Application and Enter url$")
	public void open_Application_and_Enter_url() {
		System.out.println("Open Application and Enter url");
	}

	@When("^enter username$")
	public void enter_username() {
		System.out.println("enter username");
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
}
