package com.ptithcm.sqa.selenium.Customer;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CancelOrderTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:2593";
    private static final String USER_PHONE = "0000000000"; 
    private static final String USER_PASSWORD = "123"; 
    private static final String PRODUCT_NAME = "Paracetamol"; 

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        login();
    }

    private void login() {
        driver.get(BASE_URL + "/authen/login");
        WebElement phoneInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        phoneInput.clear();
        phoneInput.sendKeys(USER_PHONE);
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.clear();
        passwordInput.sendKeys(USER_PASSWORD);
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        try {
            wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
            System.out.println("Đăng nhập thành công, chuyển hướng đến trang chủ.");
        } catch (Exception e) {
            System.err.println("Đăng nhập thất bại, URL hiện tại: " + driver.getCurrentUrl());
            throw new RuntimeException("Không thể đăng nhập. Vui lòng kiểm tra thông tin đăng nhập.");
        }
    }

    private void addProductToCart() {
        driver.get(BASE_URL + "/product/" + PRODUCT_NAME);
        WebElement addToCartForm = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form[action='/product/add-to-cart']")));
        WebElement outOfStockMessage = driver.findElement(By.id("out-of-stock-message"));
        assertTrue(outOfStockMessage.getAttribute("class").contains("d-none"),
                "Sản phẩm đã hết hàng, không thể thêm vào giỏ.");
        WebElement quantityInput = driver.findElement(By.id("quantity"));
        quantityInput.clear();
        quantityInput.sendKeys("2");
        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-btn"));
        addToCartButton.click();
        try {
            WebElement successMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
            String messageText = successMessage.getText().toLowerCase();
            System.out.println("Thông báo thêm vào giỏ: " + messageText);
            assertTrue(messageText.contains("thêm") && messageText.contains("giỏ"),
                    "Thông báo thành công không chứa từ khóa mong đợi. Nội dung thực tế: " + messageText);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm thông báo thành công: " + e.getMessage());
            List<WebElement> errorMessages = driver.findElements(By.cssSelector(".alert.alert-danger"));
            if (!errorMessages.isEmpty()) {
                String errorText = errorMessages.get(0).getText();
                System.err.println("Thông báo lỗi: " + errorText);
                throw new RuntimeException("Thêm sản phẩm thất bại với lỗi: " + errorText);
            }
            throw new RuntimeException("Không tìm thấy thông báo thành công khi thêm sản phẩm.");
        }
    }

    @Test
    public void testSuccessfulCancelOrder() {
        addProductToCart();
        driver.get(BASE_URL + "/cart");
        WebElement cartItem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table.table-bordered tbody tr")));
        assertTrue(cartItem.getText().toLowerCase().contains(PRODUCT_NAME.toLowerCase()),
                "Sản phẩm '" + PRODUCT_NAME + "' không có trong giỏ hàng.");
        WebElement cancelButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(".cancel-btn")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('cancel-form').submit();");
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
        assertTrue(successMessage.getText().contains("Hủy đơn hàng thành công"),
                "Thông báo hủy đơn hàng thành công không hiển thị.");
        driver.get(BASE_URL + "/cart");
        WebElement emptyCartMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Giỏ hàng trống.']")));
        assertTrue(emptyCartMessage.isDisplayed(), "Giỏ hàng không rỗng sau khi hủy đơn hàng.");
    }

    @Test
    public void testCancelOrderWithEmptyCart() {
        driver.get(BASE_URL + "/cart");
        WebElement emptyCartMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Giỏ hàng trống.']")));
        assertTrue(emptyCartMessage.isDisplayed(), "Thông báo 'Giỏ hàng trống.' không hiển thị.");
        List<WebElement> cancelButtons = driver.findElements(By.cssSelector(".cancel-btn"));
        assertTrue(cancelButtons.isEmpty(), "Nút hủy không nên hiển thị khi giỏ hàng rỗng.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}