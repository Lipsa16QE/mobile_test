package src_Appium;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

//added git lfs
public class RainApk {
	
	WebDriver driver;
	
	/*
	 *Defining the page Elements , for implementing in a proper FF would have written in a repo file
	 * 
	 * 
	 * */
	String getStartedButton="com.rainmanagement.rain.staging:id/getStartedButton";
	String skipButton="com.rainmanagement.rain.staging:id/skipButton";
	
	String header_path="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.widget.TextView[1]";
	String header_value="Let's Get Started";
	
	String selCountry="com.rainmanagement.rain.staging:id/text_input_end_icon";
	String typeCountry="com.rainmanagement.rain.staging:id/search_src_text";
	String country="com.rainmanagement.rain.staging:id/textView";
	
	String firstName="com.rainmanagement.rain.staging:id/firstNameTextInput";
	String lastName="com.rainmanagement.rain.staging:id/lastNameTextInput";
	
	String header2_path="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[2]/android.widget.TextView[1]";
	
	String signInButton="com.rainmanagement.rain.staging:id/signInButton";
	
	@BeforeClass
	public void setUp() throws MalformedURLException{
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("VERSION", "11"); 
		capabilities.setCapability("deviceName","MyPhone");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("app", "/Users/lipsapanda/eclipse-workspace/Mobile/src/test/resources/staging.apk"); 
	   capabilities.setCapability("appPackage", "com.rainmanagement.rain.staging");
	   driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	  

	}
	
	
	
	
	
	
	@Test(priority=0)
	public void testRegFlow() throws Exception {
		
		//Clicking on Get started Button
		wait(2000);
	    clickOnbutton(getStartedButton);
		wait(2000);
		//Click on skip
		clickOnbutton(skipButton);
		//validate string on landing page
		assertString(header_path,header_value);
		
		//Select UAE as country
		clickOnbutton(selCountry);
		wait(2000);
		typeText(typeCountry,"United Arab");
		wait(2000);
		//Not a button but clicking element works
		clickOnbutton(country);
		clickSubmit();
		wait(2000);
	   
		
	   //Type mandatory first and last Name
		typeText(firstName,"Lipsa");
		typeText(lastName,"test");
		wait(2000);
	    clickSubmit();
	    wait(2000);
	    
	    //Enter Email id and pwd
	    String mail_id="test"+getRandomNumberString()+"@gmail.com"; // Create Random email id everytime to avoid duplicate email , 
	    															//this can be cleaned up at end of test from database to avoid junk
	    SignInDetails(mail_id,"Test@test@#12345678990123456##");
	    clickSubmit();
	   
	    wait(6000);
	    //validate string on landing page
	    assertString(header2_path,"We sent a confirmation email to:");
	   
	    //sign out
	    clickSignOut();

	}
	
	@Test(priority=1,dataProvider="testdata",dependsOnMethods="testRegFlow")// takes data from Data Provider
	public void testSignIn(String uname,String password) throws Exception {
		wait(3000);
		clickOnbutton(signInButton); // Click Sign in
		wait(2000);
		try {
		SignInDetails(uname,password); //Eneter email and password
		clickOnbutton(signInButton);
		wait(6000);
		assertString(header2_path,"We sent a confirmation email to:");//assert string on landing page
		clickSignOut();	
		}
		catch(Throwable e){
			
			WebElement text=driver.findElement(By.id("com.rainmanagement.rain.staging:id/messageTextView"));	
			System.out.println(text.getText()+e.getMessage());
			
		}
		
	}
	
	@DataProvider(name="testdata")
	public Object[][] TestDataFeed(){

	Object [][] loginData=new Object[2][2];
	//1st set of data input
	loginData[0][0]="test8083@gmail.com";
	loginData[0][1]="Test@test@#12345678990123456##";
	//Sec set of data Input
	loginData[1][0]="username2@gmail.com";
	loginData[1][1]="Password290";
	
	return loginData;
	}
	
	
	
	
	
	/*
	 * Adding all the utility methods here , In a FF would have added the same under Utility Class
	 */
	
	//Click on element Function
	public void clickOnbutton(String path) {
		       driver.findElement(By.id(path)).click();	
 	}
	
	//Type data in textbox function
	public void typeText(String path,String val) {
	       driver.findElement(By.id(path)).sendKeys(val);;	
	}
	
	//Assert any string
	public void assertString(String path,String val) {
		
		 WebElement header=driver.findElement(By.xpath(path));
		 System.out.println(header.getText());
		 assert header.getText().equals(val):"Actual value is : "+header.getText()+" did not match with expected value";
		
	}
	
	//Click on submit button
	public void clickSubmit() {
		clickOnbutton("com.rainmanagement.rain.staging:id/submitButton");
	}
	
	//Click on SignOut
	public void clickSignOut() {
		clickOnbutton("com.rainmanagement.rain.staging:id/logoutButton");
	}
	
	//Signin Function
	public void SignInDetails(String mail,String password) throws InterruptedException {
			typeText("com.rainmanagement.rain.staging:id/emailTextInput",mail);
			typeText("com.rainmanagement.rain.staging:id/passwordTextInput",password);
		
		  // email.sendKeys("test8073@gmail.com");
		   //password.sendKeys("Test@test@#12345678990123456##");
		   wait(3000);
		
	}
	
	//wait --> if implementing a proper FF we can use wait until and fluent wait 
	public void wait(int sec) throws InterruptedException
	
	{
		Thread.sleep(sec);
	}
	
	//method to generate 6 digit random number
	public static String getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return String.format("%06d", number);
	}
	
	
	@AfterClass
public void teardown(){
//close the app
	driver.quit();
	}

}
