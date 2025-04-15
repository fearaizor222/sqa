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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaidOrdersTest {

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
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        WebElement userDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("user-dropdown-toggle")));
        assertTrue(userDropdown.isDisplayed(), "Đăng nhập thất bại.");
    }

    @Test
    public void testDisplayPaidOrders() {
        login();
        driver.get(BASE_URL + "/paid-orders");
        try {
            WebElement ordersTable = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-bordered")));
            List<WebElement> orderRows = ordersTable.findElements(By.cssSelector("tbody tr"));

            if (orderRows.isEmpty()) {
                System.out.println("Không có hóa đơn nào được hiển thị trong bảng.");
            } else {
                System.out.println("Danh sách hóa đơn đã thanh toán:");
                System.out.println("----------------------------------------");
                int rowIndex = 1;
                for (WebElement row : orderRows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    assertEquals(4, cells.size(), "Mỗi dòng phải có 4 cột: STT, Tổng tiền, Trạng thái, Chi tiết");

                    String stt = cells.get(0).getText();
                    String tongTien = cells.get(1).getText();
                    String trangThai = cells.get(2).getText();
                    WebElement chiTietButton = cells.get(3).findElement(By.cssSelector("a.btn.btn-info.btn-sm"));

                    System.out.printf("Hóa đơn %d:%n", rowIndex);
                    System.out.printf("  STT: %s%n", stt);
                    System.out.printf("  Tổng tiền: %s%n", tongTien);
                    System.out.printf("  Trạng thái: %s%n", trangThai);
                    System.out.printf("  Nút Chi tiết: %s (href=%s)%n",
                            chiTietButton.getText(), chiTietButton.getAttribute("href"));
                    System.out.println("----------------------------------------");
                    assertTrue(chiTietButton.isDisplayed(), "Nút 'Xem chi tiết' không hiển thị");
                    assertTrue(chiTietButton.getAttribute("href").contains("/paid-orders/details/"),
                            "Link 'Xem chi tiết' không hợp lệ");

                    rowIndex++;
                }
                assertFalse(orderRows.isEmpty(), "Danh sách hóa đơn không được rỗng");
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            try {
                WebElement noOrdersMessage = driver.findElement(By.tagName("p"));
                String messageText = noOrdersMessage.getText();
                System.out.println("Thông báo: " + messageText);
                assertEquals("Không có hóa đơn đã thanh toán.", messageText,
                        "Thông báo khi không có hóa đơn không đúng.");
            } catch (Exception ex) {
                System.out.println("Nguồn trang: " + driver.getPageSource());
                throw e;
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}