package com.demowebshop.demowebshop;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DemoWebShopTest {

    private WebDriver driver;
    private String baseUrl;

    @SuppressWarnings("deprecation")
	@Before
    public void setUp() throws Exception {
        // Set up the WebDriver instance and base URL
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Sarath Kumar\\Desktop\\WorkSpace\\demowebshop\\src\\main\\resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        baseUrl = "http://demowebshop.tricentis.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void test() throws Exception {
        // Navigate to the URL
        driver.get(baseUrl);

        // Click on Login button
        driver.findElement(By.className("ico-login")).click();

        // Log in with given credentials
        driver.findElement(By.id("Email")).sendKeys("planittest78@gmail.com");
        driver.findElement(By.id("Password")).sendKeys("123456");
        driver.findElement(By.cssSelector("input[value='Log in']")).click();

        // Validate the user account ID on top right
        WebElement accountLink = driver.findElement(By.className("account"));
        String accountText = accountLink.getText();
        assert(accountText.contains("planittest78@gmail.com"));

        // Clear the shopping cart
        driver.findElement(By.className("cart-label")).click();
        if (driver.findElements(By.name("removefromcart")).size() > 0) {
            driver.findElement(By.name("removefromcart")).click();
            driver.findElement(By.name("updatecart")).click();
        }

        // Mouse hover on the “Computers” from Categories
        Actions builder = new Actions(driver);
        WebElement computersLink = driver.findElement(By.xpath("//a[contains(text(),'Computers')]"));
        builder.moveToElement(computersLink).build().perform();

        // Select “Desktops” from the hover menu
        driver.findElement(By.xpath("//a[contains(text(),'Desktops')]")).click();

        // Select a computer from the list displayed
        driver.findElement(By.linkText("Build your own cheap computer")).click();

        // Get the price details and enter the quantity (more than one)
        String price = driver.findElement(By.xpath("//*[@class='product-price']")).getText();
        System.out.println(price);
      
        Actions actions = new Actions(driver);
        WebElement qty = driver.findElement(By.xpath("//input[@id='addtocart_72_EnteredQuantity']"));
        actions.moveToElement(qty).build().perform();
        qty.clear();
        qty.sendKeys("2");

        // Click on “Add to cart”
        driver.findElement(By.xpath("//*[@class='add-to-cart-panel']//*[@value='Add to cart']")).click();

        // Validate “The product has been added to your shopping cart” message
        String message = driver.findElement(By.cssSelector(".bar-notification.success")).getText();
        assert(message.contains("The product has been added to your shopping cart"));

        // Click on “shopping cart” on top right and validate the “Sub-Total” Price for selected computer.
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,-1000)");
        driver.findElement(By.className("cart-label")).click();
        String subTotal = driver.findElement(By.xpath("//*[text()='Sub-Total:']/../following-sibling::td//span[@class='product-price']")).getText();
        /*int price1 = Integer.parseInt(price);
        int totalPrice = price1 + price1 + 30;
        System.out.println(subTotal);
        System.out.println(totalPrice);
        assert(subTotal.equals(totalPrice));*/

        // Select I agree Checkbox
        driver.findElement(By.id("termsofservice")).click();

        // Click on “Check-out”
        driver.findElement(By.id("checkout")).click();

        // If "Select a billing address from your address book or enter a new address" exists, select “New Address” From the drop down, else skip this step
        if (driver.findElements(By.id("billing-address-select")).size() > 0) {
            driver.findElement(By.id("billing-address-select")).sendKeys("New Address");
        }

        // Fill all mandatory fields in “Billing Address” and click “Continue”
        Select country = new Select(driver.findElement(By.id("BillingNewAddress_CountryId")));
        country.selectByVisibleText("Algeria");
        driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Oran");
        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("121 Rue Didouche Moura");
        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("16000");
        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("21321620267");
        driver.findElement(By.xpath("(//*[@class='button-1 new-address-next-step-button'])[1]")).click();       
        
        // Select the “Shipping Address” as same as “Billing Address” from “Shipping Address” drop down and click on “Continue”.
        Select shippingAddress = new Select(driver.findElement(By.id("shipping-address-select")));
        shippingAddress.selectByVisibleText("planit test, 121 Rue Didouche Moura, Oran 16000, Algeria");
        driver.findElement(By.xpath("(//*[@class='button-1 new-address-next-step-button'])[2]")).click();
        
        // Select the shipping method as “Next Day Air” and click on “Continue”
        driver.findElement(By.xpath("//*[text() = 'Next Day Air (0.00)']")).click();
        driver.findElement(By.xpath("//*[@class='button-1 shipping-method-next-step-button']")).click();
        
        // Choose the payment method as COD (Cash on delivery) and click on “Continue”
        driver.findElement(By.xpath("//*[contains(text(), 'Cash On Delivery')]")).click();
        driver.findElement(By.xpath("//*[@class='button-1 payment-method-next-step-button']")).click();
        
        // Validate the message “You will pay by COD” and click on “Continue”
        String verifyMessageforCOD = driver.findElement(By.xpath("//*[@class='info']//td//p")).getText();
        assert(verifyMessageforCOD.equals("You will pay by COD"));
        driver.findElement(By.xpath("//*[@class='button-1 payment-info-next-step-button']")).click();
        
        // Click on “Confirm” button
        driver.findElement(By.xpath("//*[@class='button-1 confirm-order-next-step-button']")).click();
        
        // Validate the message “Your order has been successfully processed!” and print the Order number
        Thread.sleep(10000);
        String verifyMessageforOrderSuccess = driver.findElement(By.xpath("//*[@class='title']")).getText();
        System.out.println(verifyMessageforOrderSuccess);
        assert(verifyMessageforOrderSuccess.equals("Your order has been successfully processed!"));
        String orderNo = driver.findElement(By.xpath("//*[@class='details']/li[1]")).getText();
        System.out.println(orderNo);
        
        // Click on “Continue” and log out from the application
        driver.findElement(By.xpath("//*[@class='button-2 order-completed-continue-button']")).click();
        driver.findElement(By.className("ico-logout")).click();
    }
    /*
    @After
    public void closeBrowser() {
        driver.close();
    }*/
}
