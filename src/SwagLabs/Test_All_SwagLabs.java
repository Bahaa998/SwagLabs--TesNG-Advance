package SwagLabs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Test_All_SwagLabs {
	public WebDriver driver;
	SoftAssert softAssertProccess = new SoftAssert();

	@BeforeTest
	public void LoginBeforeTest() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		driver.get("https://www.saucedemo.com");
		driver.findElement(By.id("user-name")).sendKeys("standard_user" + Keys.TAB + "secret_sauce" + Keys.ENTER);
	}

//******************************* Check actual title of web page ***************************
	@Test(priority = 1)
	public void Check_Title() {
		String actualTitle = driver.getTitle();
		String expectedTitle = "Swag Labs";
		softAssertProccess.assertEquals(actualTitle, expectedTitle , "Check the Title : ");
		softAssertProccess.assertAll();
	}

// ******************************* sorting Low To High ***********************************
	@Test(priority = 2)
	public void sorting_Low_To_High() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div[2]/span/select")).click();
		driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div[2]/span/select/option[3]")).click();

		List<WebElement> PriceItems = driver.findElements(By.className("inventory_item_price"));

		List<Double> newListPRice = new ArrayList<>();

		for (int i = 0; i < PriceItems.size(); i++) {

			// Delete All Space
			String priceString_withoutSpace = PriceItems.get(i).getText();
			priceString_withoutSpace.trim();

			// Delete String " $ "
			String PriceItems_Without_String = priceString_withoutSpace.replace("$", "");

			// Convert String to Double
			double Price_Double = Double.parseDouble(PriceItems_Without_String);

			newListPRice.add(Price_Double);

		}
		for (int j = 0; j < newListPRice.size(); j++) {
			boolean checkFirstItem = newListPRice.get(0) < newListPRice.get(newListPRice.size() - 1);

			softAssertProccess.assertEquals(checkFirstItem, true , "Check sorting Low To High : ");
			softAssertProccess.assertAll();
		}

//		System.out.println("Lowest price : " + newListPRice.get(0));
//		System.out.println("Highest price : " + newListPRice.get(newListPRice.size() - 1));

	}

// *************************************** sorting High To Low *************************************************
	@Test(priority = 3)
	public void sorting_High_To_Low() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.navigate().refresh();
		driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div[2]/span/select")).click();
		driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div[2]/span/select/option[4]")).click();

		List<WebElement> PriceItems = driver.findElements(By.className("inventory_item_price"));
		List<Double> newListPRice = new ArrayList<>();

		for (int i = 0; i < PriceItems.size(); i++) {

			// Delete All Space
			String priceString_withoutSpace = PriceItems.get(i).getText();
			priceString_withoutSpace.trim();

			// Delete String " $ "
			String PriceItems_Without_String = priceString_withoutSpace.replace("$", "");

			// Convert String to Double
			double Price_Double = Double.parseDouble(PriceItems_Without_String);
			newListPRice.add(Price_Double);

		}
		for (int j = 0; j < newListPRice.size(); j++) {
			boolean checkFirstItem = newListPRice.get(0) > newListPRice.get(newListPRice.size() - 1);

			softAssertProccess.assertEquals(checkFirstItem, true  ,"Check sorting High To Low : ");
			softAssertProccess.assertAll();

		}
//		System.out.println("Lowest price : " + newListPRice.get(newListPRice.size() - 1));
//		System.out.println("Highest price : " + newListPRice.get(0));

	}

//******************************* Check actual size of list ***************************************
	@Test(priority = 4)
	public void Check_Cart() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.navigate().refresh();

		List<WebElement> addToCart = driver.findElements(By.className("btn_primary"));
//		System.out.println("Size of list items out cart : " + addToCart.size());

		for (int i = 0; i < addToCart.size(); i++) {
			addToCart.get(i).click();
//			Thread.sleep(1000);
		}
		
		List<WebElement> item_in_cart = driver.findElements(By.className("btn_secondary"));
//		System.out.println("item_in_cart : " + item_in_cart.size());
		

//		String actualSize = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")).getText();
//		int actualSize_Updated = Integer.parseInt(actualSize);
//		int expectedSize = addToCart.size(); // 6
//		Assert.assertEquals(actualSize_Updated, expectedSize);

		// Go to Cart
		driver.findElement(By.className("shopping_cart_link")).click();

		// Size of list in Cart
		List<WebElement> myCart = driver.findElements(By.className("cart_item"));
//		System.out.println("Size of list items in cart : " + myCart.size());

		// Check actual size of list
		int actualSize = myCart.size(); // 6
		int expectedSize = item_in_cart.size(); // 6
		softAssertProccess.assertEquals(actualSize, expectedSize, "Check actual size : ");
		softAssertProccess.assertAll();
	}
	

	// **************************** Check Total Prace in The Cart ****************************************
	@SuppressWarnings("deprecation")
	@Test(priority = 5)
	public void Check_Total_Prace_in_The_Cart() throws InterruptedException, IOException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		// Scroll Down
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,750)");

		Thread.sleep(1000);
		driver.findElement(By.id("checkout")).click();

		// Scroll Up
		js.executeScript("window.scrollBy(0,-320)");
		Thread.sleep(1000);

		driver.findElement(By.id("first-name")).sendKeys("Bahaa" + Keys.TAB + "Algemzawe" + Keys.TAB + "11902");
		driver.findElement(By.id("continue")).click();
		
		js.executeScript("window.scrollBy(0,1000)");

		List<WebElement> checkout = driver.findElements(By.className("inventory_item_price"));

		List<Double> newListPrice = new ArrayList<>();

		Double counts = 0.0;
		for (int i = 0; i < checkout.size(); i++) {

			// Delete All Space
			String priceString_withoutSpace = checkout.get(i).getText();
			priceString_withoutSpace.trim();

			// Delete String " $ "
			String PriceItems_Without_String = priceString_withoutSpace.replace("$", "");

			// Convert String to Double
			double Price_Double = Double.parseDouble(PriceItems_Without_String);

			newListPrice.add(Price_Double);
			counts += Price_Double;
		}
//		System.out.println("Total Count Before Taxt : " + counts);

		String Tax = driver.findElement(By.className("summary_tax_label")).getText();

		// Delete String " $ "
		String Tax_Without_Sting = Tax.replace("Tax: $", "").trim();
//	    System.out.println("Tax : " + Tax_Without_Sting);

		// Convert String to Double
		double Final_Tax = Double.parseDouble(Tax_Without_Sting);

		// Item Total + Tax
		double Total_Price_with_Tax = Final_Tax + counts;

		// Round the number to two decimal places
		double Expected_Total_prace = BigDecimal.valueOf(Total_Price_with_Tax).setScale(2, BigDecimal.ROUND_HALF_DOWN)
				.doubleValue();
		System.out.println("Expected Total price with Tax : " + Expected_Total_prace);

		// Total from Cart
		String Actual_Total = driver.findElement(By.className("summary_total_label")).getText();
		String Actual_Total_Updated = Actual_Total.replace("Total: $", "").trim();
		double Actual_Total_Fix = Double.parseDouble(Actual_Total_Updated);
		System.out.println("Actual Total price : " + Actual_Total_Fix);
		
		
		//(((( Process of taking a screenshot )))
		Date currentDate = new Date();
		String TheActualDate = currentDate.toString().replace(":", "_");
		TakesScreenshot Capture = ((TakesScreenshot) driver);
		File srcFile = Capture.getScreenshotAs(OutputType.FILE);
		File Destination = new File(".\\Secreenshots\\" + TheActualDate + ".png");
		FileUtils.copyFile(srcFile, Destination);
		

		softAssertProccess.assertEquals(Actual_Total_Fix, Expected_Total_prace , "Check The Total Prace : ");
		softAssertProccess.assertAll();
	}


	@AfterTest
	public void afterMyTest() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		Thread.sleep(2000);
		driver.findElement(By.id("finish")).click();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-320)");
		Thread.sleep(3000);

		driver.findElement(By.id("back-to-products")).click();
		Thread.sleep(2000);

		driver.quit();
	}

}



