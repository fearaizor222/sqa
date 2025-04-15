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

public class OrderDetailsTest {

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
    public void testDisplayOrderDetails() {
        login();
        driver.get(BASE_URL + "/paid-orders");
        try {
            WebElement ordersTable = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-bordered")));
            WebElement firstOrderRow = ordersTable.findElement(By.cssSelector("tbody tr"));
            WebElement chiTietButton = firstOrderRow.findElement(By.cssSelector("a.btn.btn-info.btn-sm"));

            List<WebElement> cells = firstOrderRow.findElements(By.tagName("td"));
            String stt = cells.get(0).getText();
            String tongTien = cells.get(1).getText();
            String trangThai = cells.get(2).getText();
            String chiTietLink = chiTietButton.getAttribute("href");

            System.out.println("Hóa đơn được chọn để xem chi tiết:");
            System.out.println("----------------------------------------");
            System.out.printf("STT: %s%n", stt);
            System.out.printf("Tổng tiền: %s%n", tongTien);
            System.out.printf("Trạng thái: %s%n", trangThai);
            System.out.printf("Link chi tiết: %s%n", chiTietLink);
            System.out.println("----------------------------------------");
            chiTietButton.click();
            wait.until(ExpectedConditions.urlContains("/paid-orders/details/"));
            try {
                WebElement detailsTable = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-bordered")));
                List<WebElement> detailRows = detailsTable.findElements(By.cssSelector("tbody tr"));

                if (detailRows.isEmpty()) {
                    System.out.println("Không có chi tiết hóa đơn nào được hiển thị trong bảng.");
                } else {
                    System.out.println("Chi tiết hóa đơn:");
                    System.out.println("----------------------------------------");
                    int rowIndex = 1;
                    for (WebElement row : detailRows) {
                        List<WebElement> detailCells = row.findElements(By.tagName("td"));
                        assertEquals(5, detailCells.size(),
                                "Mỗi dòng phải có 5 cột: Hình ảnh, Tên sản phẩm, Số lượng, Giá tiền, Thành tiền");

                        WebElement imgElement = detailCells.get(0).findElement(By.tagName("img"));
                        String tenSanPham = detailCells.get(1).getText();
                        String soLuong = detailCells.get(2).getText();
                        String giaTien = detailCells.get(3).getText();
                        String thanhTien = detailCells.get(4).getText();

                        System.out.printf("Mục %d:%n", rowIndex);
                        System.out.printf("  Hình ảnh: %s (src=%s)%n", imgElement.getAttribute("alt"),
                                imgElement.getAttribute("src"));
                        System.out.printf("  Tên sản phẩm: %s%n", tenSanPham);
                        System.out.printf("  Số lượng: %s%n", soLuong);
                        System.out.printf("  Giá tiền: %s%n", giaTien);
                        System.out.printf("  Thành tiền: %s%n", thanhTien);
                        System.out.println("----------------------------------------");
                        assertTrue(imgElement.isDisplayed(), "Hình ảnh sản phẩm không hiển thị");
                        assertFalse(imgElement.getAttribute("src").isEmpty(), "Hình ảnh phải có đường dẫn hợp lệ");
                        assertFalse(tenSanPham.isEmpty(), "Tên sản phẩm không được rỗng");
                        assertFalse(soLuong.isEmpty(), "Số lượng không được rỗng");
                        assertFalse(giaTien.isEmpty(), "Giá tiền không được rỗng");
                        assertFalse(thanhTien.isEmpty(), "Thành tiền không được rỗng");

                        rowIndex++;
                    }
                    WebElement totalValueElement = driver.findElement(By.cssSelector("h4 span"));
                    String totalValueText = totalValueElement.getText();
                    System.out.println("Tổng giá trị hiển thị: " + totalValueText);
                    assertTrue(totalValueText.matches("\\d{1,3}(,\\d{3})* đ"),
                            "Tổng giá trị không đúng định dạng (VD: 1,000,000 đ)");
                }
            } catch (org.openqa.selenium.TimeoutException e) {
                try {
                    WebElement noDetailsMessage = driver.findElement(By.tagName("p"));
                    String messageText = noDetailsMessage.getText();
                    System.out.println("Thông báo: " + messageText);
                    assertEquals("Không có chi tiết hóa đơn.", messageText,
                            "Thông báo khi không có chi tiết hóa đơn không đúng.");
                } catch (Exception ex) {
                    System.out.println("Nguồn trang: " + driver.getPageSource());
                    throw e;
                }
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            WebElement noOrdersMessage = driver.findElement(By.tagName("p"));
            String messageText = noOrdersMessage.getText();
            System.out.println("Thông báo: " + messageText);
            assertEquals("Không có hóa đơn đã thanh toán.", messageText,
                    "Thông báo khi không có hóa đơn không đúng.");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}