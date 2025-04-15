package com.ptithcm.sqa.Customer;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchDrugTest {

        private WebDriver driver;
        private WebDriverWait wait;
        private static final String BASE_URL = "http://localhost:2593";
        private static final String USER_PHONE = "000000000";
        private static final String USER_PASSWORD = "123";

        @BeforeEach
        public void setUp() {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(options);
                wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                login();
        }

        private void login() {
                driver.get(BASE_URL + "/authen/login");
                WebElement phoneInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("username")));
                phoneInput.sendKeys(USER_PHONE);
                WebElement passwordInput = driver.findElement(By.id("password"));
                passwordInput.sendKeys(USER_PASSWORD);
                WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
                loginButton.click();
                wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        }

        @Test
        public void testSearchWithValidKeyword() {
                WebElement searchInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.name("search")));
                searchInput.sendKeys("Paracetamol");
                WebElement searchButton = driver.findElement(By.className("function-box"));
                searchButton.click();
                WebElement productContainer = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.className("product-box-container")));
                List<WebElement> products = productContainer.findElements(By.className("product-card"));
                assertTrue(products.size() > 0, "Không tìm thấy sản phẩm nào cho từ khóa 'Paracetamol'.");
                WebElement firstProductTitle = products.get(0).findElement(By.className("product-title"));
                assertTrue(firstProductTitle.getText().toLowerCase().contains("paracetamol"),
                                "Sản phẩm đầu tiên không chứa từ khóa 'Paracetamol'.");
        }

        @Test
        public void testSearchWithNonExistentKeyword() {
                WebElement searchInput = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.name("search")));
                searchInput.sendKeys("NonExistentDrug");
                WebElement searchButton = driver.findElement(By.className("function-box"));
                searchButton.click();
                WebElement noProductsMessage = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.className("no-products-message")));
                assertTrue(noProductsMessage.getText().contains("Không tìm thấy sản phẩm phù hợp"),
                                "Thông báo 'Không tìm thấy sản phẩm' không hiển thị đúng.");
                boolean isProductContainerHidden = driver
                                .findElement(By.className("product-box-container"))
                                .getAttribute("style")
                                .contains("display: none");
                assertTrue(isProductContainerHidden, "Container sản phẩm không bị ẩn khi không có kết quả.");
        }

        @Test
        public void testSearchWithAgeGroupFilter() {
                WebElement ageGroupSelect = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("group")));
                Select ageGroupDropdown = new Select(ageGroupSelect);
                ageGroupDropdown.selectByValue("over18");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-box-container")));
                List<WebElement> products = driver.findElements(By.className("product-card"));
                assertTrue(products.size() > 0, "Không tìm thấy sản phẩm nào cho nhóm tuổi 'Trên 18 tuổi'.");
        }

        @Test
        public void testSearchWithKeywordAndAgeGroup() {
                WebElement ageGroupSelect = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.id("group")));
                Select ageGroupDropdown = new Select(ageGroupSelect);
                ageGroupDropdown.selectByValue("over18");
                WebElement searchInput = driver.findElement(By.name("search"));
                searchInput.sendKeys("Paracetamol");
                WebElement searchButton = driver.findElement(By.className("function-box"));
                searchButton.click();
                WebElement productContainer = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.className("product-box-container")));
                List<WebElement> products = productContainer.findElements(By.className("product-card"));
                assertTrue(products.size() > 0,
                                "Không tìm thấy sản phẩm nào cho từ khóa 'Paracetamol' và nhóm tuổi 'Trên 18 tuổi'.");
                WebElement firstProductTitle = products.get(0).findElement(By.className("product-title"));
                assertTrue(firstProductTitle.getText().toLowerCase().contains("paracetamol"),
                                "Sản phẩm đầu tiên không chứa từ khóa 'Paracetamol'.");
        }

        @AfterEach
        public void tearDown() {

                if (driver != null) {
                        driver.quit();
                }
        }
}