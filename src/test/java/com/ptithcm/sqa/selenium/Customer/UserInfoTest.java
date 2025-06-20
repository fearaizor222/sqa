package com.ptithcm.sqa.selenium.Customer;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInfoTest {

        private WebDriver driver;
        private WebDriverWait wait;
        private JavascriptExecutor jsExecutor;
        private static final String BASE_URL = "http://localhost:2593";
        private static final String USER_PHONE = "0000000000";
        private static final String USER_PASSWORD = "123";
        private static final String ORIGINAL_NAME = "Phạm Minh Nghi";
        private static final String ORIGINAL_ADDRESS = "25/42";

        @BeforeEach
        public void setUp() {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(options);
                jsExecutor = (JavascriptExecutor) driver;
                wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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

        @Test
        public void testDisplayUserInfo() {
                login();
                driver.get(BASE_URL + "/user/userinfo");
                WebElement nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                WebElement phoneInput = driver.findElement(By.id("sdt"));
                WebElement addressInput = driver.findElement(By.id("dia_chi"));

                assertEquals(ORIGINAL_NAME, nameInput.getAttribute("value"),
                                "Họ và tên không hiển thị đúng.");
                assertEquals(USER_PHONE, phoneInput.getAttribute("value"),
                                "Số điện thoại không hiển thị đúng.");
                assertEquals(ORIGINAL_ADDRESS, addressInput.getAttribute("value"),
                                "Địa chỉ không hiển thị đúng.");
        }

        @Test
        public void testUpdateUserInfoSuccess() {
                login();
                driver.get(BASE_URL + "/user/userinfo");
                WebElement nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                WebElement phoneInput = driver.findElement(By.id("sdt"));
                WebElement addressInput = driver.findElement(By.id("dia_chi"));
                WebElement submitButton = driver.findElement(By.cssSelector(".btn.btn-primary"));
                nameInput.clear();
                nameInput.sendKeys("Updated User");
                phoneInput.clear();
                phoneInput.sendKeys("0123456789");
                addressInput.clear();
                addressInput.sendKeys("456 Đường XYZ");
                submitButton.click();
                wait.until(ExpectedConditions.urlContains("/user/userinfo"));
                WebElement successMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
                assertTrue(successMessage.getText().contains("Cập nhập thông tin thành công"),
                                "Thông báo cập nhật thành công không hiển thị.");
                driver.get(BASE_URL + "/user/userinfo");
                nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                phoneInput = driver.findElement(By.id("sdt"));
                addressInput = driver.findElement(By.id("dia_chi"));
                assertEquals("Updated User", nameInput.getAttribute("value"),
                                "Họ và tên không được cập nhật đúng.");
                assertEquals("0123456789", phoneInput.getAttribute("value"),
                                "Số điện thoại không được cập nhật đúng.");
                assertEquals("456 Đường XYZ", addressInput.getAttribute("value"),
                                "Địa chỉ không được cập nhật đúng.");
        }

        @Test
        public void testUpdateUserInfoWithEmptyAddress() {
                login();
                driver.get(BASE_URL + "/user/userinfo");
                WebElement nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                WebElement phoneInput = driver.findElement(By.id("sdt"));
                WebElement addressInput = driver.findElement(By.id("dia_chi"));
                WebElement submitButton = driver.findElement(By.cssSelector(".btn.btn-primary"));

                nameInput.clear();
                nameInput.sendKeys("Test User");
                phoneInput.clear();
                phoneInput.sendKeys("0123456789");
                addressInput.clear();
                submitButton.click();

                wait.until(ExpectedConditions.urlContains("/user/userinfo"));
                WebElement successMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success")));
                assertTrue(successMessage.getText().contains("Cập nhập thông tin thành công"),
                                "Thông báo thành công không hiển thị khi để trống địa chỉ.");
                driver.get(BASE_URL + "/user/userinfo");
                nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                phoneInput = driver.findElement(By.id("sdt"));
                addressInput = driver.findElement(By.id("dia_chi"));
                assertEquals("Test User", nameInput.getAttribute("value"),
                                "Họ và tên không được cập nhật đúng.");
                assertEquals("0123456789", phoneInput.getAttribute("value"),
                                "Số điện thoại không được cập nhật đúng.");
                String addressValue = addressInput.getAttribute("value");
                assertTrue(addressValue == null || addressValue.isEmpty(),
                                "Địa chỉ không được cập nhật thành trống hoặc null. Giá trị thực tế: " + addressValue);
        }

        @Test
        public void testUpdateUserInfoWithEmptyPhone() {
                login();
                driver.get(BASE_URL + "/user/userinfo");
                WebElement nameInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("ho_va_ten")));
                WebElement phoneInput = driver.findElement(By.id("sdt"));
                WebElement addressInput = driver.findElement(By.id("dia_chi"));
                WebElement submitButton = driver.findElement(By.cssSelector(".btn.btn-primary"));

                jsExecutor.executeScript("arguments[0].removeAttribute('required')", phoneInput);

                nameInput.clear();
                nameInput.sendKeys("Test User");
                phoneInput.clear();
                addressInput.clear();
                addressInput.sendKeys("123 Đường ABC");
                submitButton.click();

                wait.until(ExpectedConditions.urlContains("/user/userinfo"));
                WebElement errorMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));
                assertTrue(errorMessage.getText().contains("Số điện thoại không được để trống"),
                                "Thông báo lỗi không hiển thị khi để trống số điện thoại.");
        }

        @AfterEach
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }
}