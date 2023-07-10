package com.gymshark.app.gymshark;
import java.text.Normalizer;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class App 
{
	private static final String USERNAME_TEXTBOX = "login-email";
	private static final String PASSWORD_TEXTBOX = "login-password";
	private static final String LOGIN_BUTTON = ".btn.py-3-2.w-100.btn-brand-primary";
	private static final String BOOKING_BUTTON = ".svg-inline--fa.fa-plus"; //blue plus button on home page that opens booking pop-up
	private static final String START_TIME_DROPDOWN = ".col-6.col-lg-3.mb-std.pe-2"; 
	private static final String END_TIME_DROPDOWN = ".col-6.col-lg-3.mb-std.ps-2";
	private static final String SPACES_DROPDOWN = ".svg-inline--fa.fa-triangle-exclamation.fa-fw.text-danger";
	private static final String SELECTED_SPACES_SQUARES = ".svg-inline--fa.fa-grip.fa-fw";
	private static final String MAIN_GYM1 = "MAIN GYM - Person 1";
	private static final String MAIN_GYM2 = "MAIN GYM - Person 2";
	private static final String SUITE_NUMBER_LABEL = ".form-label.text-uppercase";
	private static final String CONFIRM_BOOKING_BUTTON = ".overlap-children";

	private static final ChromeOptions options = new ChromeOptions()
			.addArguments("--no-sandbox")
			.addArguments("--headless")
			.addArguments("start-maximized")
			.addArguments("disable-infobars")
			.addArguments("--disable-extensions");

	//Initiating your Chrome driver
	private static WebDriver driver = new ChromeDriver();
	
	//Fields
	private static String startTime;
	private static String endTime;  
	
	public static void main(String[] args) throws InterruptedException, ParseException {
	
		//Handle command line args 
		String username = args[0]; 
		String password = args[1];
		startTime = args[2];
		endTime = args[3];
		String url = args[4];
		String suiteNumber = args[5];
			
		//Applied wait time
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//maximize window
		driver.manage().window().maximize();
		//open browser with desired URL
		driver.get(url);
		
		login(username, password); 
		
		if(!isLoginSuccessful()) {
			System.out.println("Login Unsuccessful"); 
			System.exit(0);
		}
		
		//Switch to the next day in the calendar 
		clickNextDayButton();
	
		//Open the new booking pop-up
		clickBookingButton();
		
		//Select start time drop down
		clickStartTimeDropDown();
		
		//Select start time 
		selectStartTime();
		
		//select end time drop down
		clickEndTimeDropDown();
		
		//Select the end time 
		selectEndTime();
		
		//Open spaces drop down
		clickSpacesDropDown();
		
		//Book both main gym spaces
		selectMainGymCheckboxes();
		
		//Close spaces drop down 
		closeSpacesDropDown();
		
		//Fill out suite number textbox
		enterSuiteNumber(suiteNumber);
		
		//Click Confirm Booking button
	    confirmBooking();               
		
		//closing the browser
		driver.close();
	}
	
	private static void login(String username, String password) throws InterruptedException {
		WebElement usernameField = driver.findElement(By.id(USERNAME_TEXTBOX));
		WebElement passwordField = driver.findElement(By.id(PASSWORD_TEXTBOX));
		WebElement loginButton = driver.findElement(By.cssSelector(LOGIN_BUTTON));
		
		usernameField.clear();
		passwordField.clear();
		usernameField.sendKeys(username);
		passwordField.sendKeys(password);
		loginButton.click();
	}	
	
	private static boolean isLoginSuccessful() {
		return driver.findElement(By.cssSelector(BOOKING_BUTTON)).isDisplayed();
	}
	
	private static void clickNextDayButton() {
		WebElement nextDayButton = driver.findElement(By.xpath("//*[@title='Next day']"));
		nextDayButton.click();
	}
	
	private static void clickBookingButton() {
		WebElement bookingButton = driver.findElement(By.cssSelector(BOOKING_BUTTON));
		bookingButton.click();
	}
	
	private static void clickStartTimeDropDown() {
		WebElement startTimeDropdown = driver.findElement(By.cssSelector(START_TIME_DROPDOWN));
		startTimeDropdown.click();
	}
	
	private static void clickEndTimeDropDown() {
		WebElement endTimeDropdown = driver.findElement(By.cssSelector(END_TIME_DROPDOWN));
		endTimeDropdown.click();
	}
	
	private static void clickSpacesDropDown() {
		WebElement spacesDropDown = driver.findElement(By.cssSelector(SPACES_DROPDOWN));
		spacesDropDown.click();
	}
	
	private static void closeSpacesDropDown( ) {
		WebElement spacesSquares = driver.findElement(By.cssSelector(SELECTED_SPACES_SQUARES));
		spacesSquares.click();
	}
	
	
	private static void selectStartTime() throws ParseException {
		WebElement startTimeButton = driver.findElement(By.cssSelector(".dropdown-menu.show"));
		List<WebElement> timeList = startTimeButton.findElements(By.tagName("li"));
		
		for (WebElement li : timeList) {
			//Get time from drop down element
			String dropDownTime = li.getText().toString();
			//Remove the space
			String formattedDropDownTime = normalizeHTMLText(dropDownTime);
			
			if (formattedDropDownTime.equals(startTime)) {
			     li.click();
			     break;
		    }
		}
	}
	
	private static void selectEndTime() {
		WebElement endTimeButton = driver.findElement(By.cssSelector(".dropdown-menu.show"));
		List<WebElement> timeList = endTimeButton.findElements(By.tagName("li"));
		
		for (WebElement li : timeList) {
			//Get time from drop down element
			String dropDownTime = li.getText().toString();
			//Remove the space
			String formattedDropDownTime = normalizeHTMLText(dropDownTime);
			
			if (formattedDropDownTime.equals(endTime)) {
			     li.click();
			     break;
		    }
		}
	}
	
	private static void selectMainGymCheckboxes() {
		WebElement spacesDropDown = driver.findElement(By.cssSelector(".dropdown-menu.show"));
		List<WebElement> gymSpaces = spacesDropDown.findElements(By.tagName("li"));
		
		for (WebElement li : gymSpaces) {
			//Get gym space from drop down element
			String gymSpaceName = li.getText().toString();
			
			if (gymSpaceName.equals(MAIN_GYM1) || gymSpaceName.equals(MAIN_GYM2)) {
			     li.click();
		    }
		}
	}
	
	private static void enterSuiteNumber(String suiteNumber) {
		WebElement suiteNumberLabel = driver.findElement(By.cssSelector(SUITE_NUMBER_LABEL));
		String textboxId = suiteNumberLabel.getAttribute("for");
		WebElement textbox = driver.findElement(By.id(textboxId));
		textbox.clear();
		textbox.sendKeys(suiteNumber);
	}
	
	private static void confirmBooking() {
		WebElement confirmBookingButton = driver.findElement(By.cssSelector(CONFIRM_BOOKING_BUTTON));
		confirmBookingButton.click();
	}
	
	private static String normalizeHTMLText(String text) {
		String canonicalDecomposition = Normalizer.normalize(text, Normalizer.Form.NFD);
		//Compare each character against the ASCII character.
		String normalString = canonicalDecomposition.replaceAll("[^\\p{ASCII}]", "");
		return normalString;
	}
}
