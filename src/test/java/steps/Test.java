package steps;

import java.util.Random;

import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Test {
	@Given("Open Application and Enter url")
	public void open_Application_and_Enter_url() {
		System.out.println("1");
	}

	@When("enter username")
	public void enter_username() {
		System.out.println("2");
	}

	@When("enter password")
	public void enter_password() {
		System.out.println("3");
		int min = 1;
        int max = 3;
        for(int i=0;i<10;i++){
            Assert.assertEquals(1, new Random().nextInt(max-min)+min);
        }
	}

	@Then("verify Msg")
	public void verify_Msg() {
		System.out.println("4");
	}
}
