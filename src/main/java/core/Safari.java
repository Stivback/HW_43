package core;

import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;
import java.math.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;

public class Safari {

	public static void main(String[] args) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/payment_tax/index.html";

		if (!System.getProperty("os.name").contains("Mac")) throw new IllegalArgumentException("Safari is available only on Mac");

		WebDriver driver = new SafariDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get(url);
		String string_monthly_payment_and_tax = driver.findElement(By.id("id_monthly_payment_and_tax")).getText();

	    
		
		String[] urls = { 
				"http://alex.academy/exe/payment_tax/index.html",
				"http://alex.academy/exe/payment_tax/index2.html", 
				"http://alex.academy/exe/payment_tax/index3.html",
				"http://alex.academy/exe/payment_tax/index4.html", 
				"http://alex.academy/exe/payment_tax/index5.html",
				"http://alex.academy/exe/payment_tax/indexE.html" };
		
		
		//String url_1 = "http://alex.academy/exe/payment_tax/index.html";
		//String url_2 = "http://alex.academy/exe/payment_tax/index2.html";
		//String url_3 = "http://alex.academy/exe/payment_tax/index3.html";
		//String url_4 = "http://alex.academy/exe/payment_tax/index4.html";
		//String url_5 = "http://alex.academy/exe/payment_tax/index5.html";
		//String url_6 = "http://alex.academy/exe/payment_tax/indexE.html";
		
		
		
		
		
		
		for (String allUrls : urls) {
		driver.get(allUrls);
		
		

		// Field "Monthly Payment with Tax" 
		String string_monthly_payment_and_tax1 = driver.findElement(By.id("id_monthly_payment_and_tax")).getText();

		String regex = "^"   
				    + "(?:.*?)?"		// looks like title or first string
				    + "(?:\\$*)?"
				    + "(?:\\s*)?"	// Everything exclude main title
				    + "((?:\\d*)|(?:\\d*)(?:\\.)(?:\\d*))"	// Find only one symbol "\"
				    + "(?:\\s*)?"	// Everything exclude main title
				    + "(?:[/]*|,\\s*[A-Z]*[a-z]*\\s*[:]*)?"
				    + "(?:\\s*)?"	// Everything exclude main title
				    + "((?:\\d*)|(?:\\d*)(?:\\.)(?:\\d*))"
				    + "(?:\\s*)?"	// Everything exclude main title
				    + "(?:%)?"		// Everything exclude main title
				    + "(?:\\s*)?"	// Everything exclude main title
			     + "$";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string_monthly_payment_and_tax1); 
		m.find();

		double monthly_payment = Double.parseDouble(m.group(1));
		double tax = Double.parseDouble(m.group(2));
		
		
		
		// Current URL:
		// System.out.println("url_6 " + url_6);
		// Should be: 91.21 or 91.23
		// String mpwt = driver.findElement(By.id("id_monthly_payment_and_tax")).getText();
		// System.out.println("mpwt: Should be: 91.23 in fact: " + mpwt);
		
		
		
		// (91.21 * 8.25) / 100 = 7.524825    rounded => 7.52
		double monthly_and_tax_amount = new BigDecimal((monthly_payment * tax) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
		// System.out.println("monthly_and_tax_amount " + monthly_and_tax_amount);
		
		// 91.21 + 7.52 = 98.72999999999999   rounded => 98.73 or 99.48
		double monthly_payment_with_tax = new BigDecimal(monthly_payment + monthly_and_tax_amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
		// System.out.println("monthly_payment_with_tax " + monthly_payment_with_tax);
		
		// double annual_payment_with_tax = monthly_payment_with_tax * 12 (should be 1184,76)
		double annual_payment_with_tax = new BigDecimal(monthly_payment_with_tax * 12).setScale(2, RoundingMode.HALF_UP).doubleValue();
		// System.out.println("annual_payment_with_tax " + annual_payment_with_tax);
		

		

		
		driver.findElement(By.id("id_annual_payment_with_tax")).sendKeys(String.valueOf(annual_payment_with_tax));
		driver.findElement(By.id("id_validate_button")).click();

		String actual_result = driver.findElement(By.id("id_result")).getText();
		System.out.println("------------------------------------------------");
		System.out.println("Browser: Safari");
		System.out.println("URL: " + allUrls);
		System.out.println("String: \t" + string_monthly_payment_and_tax1);
		System.out.println("Annual Payment with Tax: " + annual_payment_with_tax);
		System.out.println("Result: \t" + actual_result);		
		
		

		
		}
///////////////////////		The End		///////////////////////
		driver.quit();
	}
}
