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

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddToCartTest {

        private WebDriver driver;
        private WebDriverWait wait;
        private static final String BASE_URL = "http://localhost:2593";
        private static final String USER_PHONE = "0000000000";
        private static final String USER_PASSWORD = "123";
        private static final String PRODUCT_NAME = "Paracetamol";
        private static final String OUT_OF_STOCK_PRODUCT_NAME = "Viên sủi Efferalgan 500mg UPSA SAS giảm đau, hạ sốt (4 vỉ x 4 viên)";

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

                wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
                System.out.println("Đăng nhập thành công, chuyển hướng đến trang chủ.");
        }

        @Test
        public void testAddProductToCart() {
                driver.get(BASE_URL + "/product/" + PRODUCT_NAME);
                WebElement addToCartForm = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.cssSelector("form[action='/product/add-to-cart']")));
                WebElement outOfStockMessage = driver.findElement(By.id("out-of-stock-message"));
                assertTrue(outOfStockMessage.getAttribute("class").contains("d-none"),
                                "Sản phẩm đã hết hàng, không thể thêm vào giỏ.");
                WebElement quantityInput = driver.findElement(By.id("quantity"));
                quantityInput.clear();
                quantityInput.sendKeys("2");
                WebElement addToCartButton = driver.findElement(By.id("add-to-cart-btn"));
                addToCartButton.click();
                WebElement successMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
                assertTrue(successMessage.getText().contains("Đã thêm sản phẩm vào giỏ hàng!"),
                                "Thông báo thành công không hiển thị.");
        }

        @Test
        public void testAddOutOfStockProductToCart() {
                driver.get(BASE_URL + "/product/" + OUT_OF_STOCK_PRODUCT_NAME);

                WebElement outOfStockMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("out-of-stock-message")));
                assertFalse(outOfStockMessage.getAttribute("class").contains("d-none"),
                                "Thông báo 'Hết hàng' không hiển thị khi sản phẩm hết hàng.");

                boolean isFormHidden = true;
                try {
                        WebElement addToCartForm = driver
                                        .findElement(By.cssSelector("form[action='/product/add-to-cart']"));
                        isFormHidden = addToCartForm.getAttribute("class").contains("d-none");
                } catch (Exception e) {
                        isFormHidden = true;
                }
                assertTrue(isFormHidden, "Form thêm vào giỏ hàng vẫn hiển thị khi sản phẩm hết hàng.");
        }

        @AfterEach
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }
}