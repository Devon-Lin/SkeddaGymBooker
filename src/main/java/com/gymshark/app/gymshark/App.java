package com.gymshark.app.gymshark;
import java.time.Duration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;  

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App 
{
	//Constants
	static final String USERNAME_TEXTBOX = "login-email";
	static final String PASSWORD_TEXTBOX = "login-password";
	static final String LOGIN_BUTTON = ".btn.py-3-2.w-100.btn-brand-primary";
	static final String BOOKING_BUTTON = ".svg-inline--fa.fa-plus"; //blue plus button on home page that opens booking pop-up
	static final String START_TIME_DROPDOWN = "f1fb16f5-436e-46f9-a280-f7a222673458"; 
	static final String END_TIME_DROPDOWN = "d71068fc-d478-4863-a159-4ba5809f81be";
	
	
	//Initiating your Chrome driver
	static WebDriver driver =new ChromeDriver();
	
	//Fields
	private static String startTime;
	private static String endTime;  
	
	public static void main(String[] args) throws InterruptedException {
	
	//Handle command line args 
	String username = args[0]; 
	String password = args[1];
	startTime = args[2];
	endTime = args[3];
	String url = args[4];
	String suiteNumber = args[5];
	
	//Handle time formatting
	startTime = formatTime(startTime);
	endTime = formatTime(endTime);
		
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
	
	//TODO: Fix the method to select dropdown
	//Select Start Time Dropdown
	clickStartTimeDropdown();
	
	//Select Start Time 
	selectStartTime(startTime);
	
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
	
	private static void clickStartTimeDropdown() {
		WebElement startTimeDropdown = driver.findElement(By.id(START_TIME_DROPDOWN));
		startTimeDropdown.click();
	}
	
	private static void clickEndTimeDropdown() {
		WebElement endTimeDropdown = driver.findElement(By.id(END_TIME_DROPDOWN));
		endTimeDropdown.click();
	}
	
	private static void selectStartTime(String startTime) {
		WebElement startTimeButton = driver.findElement(By.xpath("//*[text()=startTime]"));
		startTimeButton.click();
	}
	
	private static void selectEndTime() {
		//TODO
	}
	
	private static String formatTime(String time) {
		String[] timearray = time.split("-");
		return timearray[0] + " " + timearray[1];
	}
}
