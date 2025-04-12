package com.ptithcm.sqa.selenium.admin;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Test Selenium cho chức năng quản lý hóa đơn
 */
public class TestReceiptManagement {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:2593";

    @BeforeEach
    public void setUp() {
        // Cấu hình EdgeDriver sử dụng WebDriverManager
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        // Không sử dụng headless để có thể quan sát kết quả
        
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        
        // Thiết lập timeout và wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Đăng nhập trước khi thực hiện test
        login();
    }

    @AfterEach
    public void tearDown() {
        // Dừng lại 3 giây để người dùng có thể xem kết quả
        sleep(3000);
        if (driver != null) {
            driver.quit();
        }
    }

    private void login() {
        driver.get(BASE_URL + "/login");
        
        // Chờ đến khi form đăng nhập hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        
        // Điền thông tin đăng nhập
        driver.findElement(By.id("username")).sendKeys("0987654321");
        driver.findElement(By.id("password")).sendKeys("123");
        
        // Click vào nút đăng nhập
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        // Chờ đến khi đăng nhập thành công và chuyển hướng
        wait.until(ExpectedConditions.urlContains("/admin"));
        
        // Thêm thời gian để đảm bảo trang đã tải hoàn toàn
        sleep(1000);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức lăn chuột đến một element
     * @param element Element cần lăn chuột đến
     */
    private void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        sleep(1000); // Chờ để hoàn thành hiệu ứng lăn chuột
    }

    /**
     * TC_REC_LIST_001: Xem danh sách hóa đơn
     * Mô tả: Kiểm tra chức năng hiển thị danh sách hóa đơn
     * Điều kiện tiên quyết: Đăng nhập với tư cách admin
     */
    @Test
    public void testViewReceiptList() {
        // Truy cập trang hóa đơn
        driver.get(BASE_URL + "/admin/receipts");
        
        // Đợi cho trang tải xong, kiểm tra tiêu đề trang
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("navbar-brand")));
        WebElement pageTitle = driver.findElement(By.className("navbar-brand"));
        assertTrue(pageTitle.getText().contains("Quản lý hóa đơn"), "Tiêu đề trang không đúng");
        
        // Kiểm tra hiển thị bảng danh sách hóa đơn
        WebElement receiptTable = driver.findElement(By.className("table"));
        assertTrue(receiptTable.isDisplayed(), "Bảng danh sách hóa đơn không hiển thị");
        
        // Kiểm tra hiển thị các cột trong bảng
        List<WebElement> tableHeaders = driver.findElements(By.cssSelector("table thead th"));
        assertTrue(tableHeaders.size() >= 5, "Không đủ cột trong bảng hóa đơn");
        
        // Kiểm tra tên các cột
        String[] expectedColumns = {"Mã HD", "Khách hàng", "Ngày lập", "Tổng tiền", "Trạng thái", "Thao tác"};
        for (int i = 0; i < expectedColumns.length; i++) {
            assertTrue(tableHeaders.get(i).getText().contains(expectedColumns[i]), 
                    "Cột " + expectedColumns[i] + " không hiển thị đúng");
        }
        
        // Kiểm tra nếu có hóa đơn hoặc thông báo không có hóa đơn
        List<WebElement> receiptRows = driver.findElements(By.cssSelector("table tbody tr"));
        assertTrue(receiptRows.size() > 0, "Không hiển thị dòng nào trong bảng hóa đơn");
        
        // Nếu có ít nhất một hóa đơn, kiểm tra hiển thị thông tin
        if (!driver.findElements(By.cssSelector("table tbody tr td[colspan='6']")).isEmpty()) {
            // Trường hợp không có hóa đơn
            WebElement noDataMessage = driver.findElement(By.cssSelector("table tbody tr td[colspan='6']"));
            assertTrue(noDataMessage.getText().contains("Chưa có hóa đơn nào"), 
                    "Thông báo không có hóa đơn không hiển thị đúng");
        } else {
            // Trường hợp có hóa đơn, kiểm tra hóa đơn đầu tiên
            WebElement firstReceipt = receiptRows.get(0);
            
            // Kiểm tra các cột thông tin
            List<WebElement> receiptColumns = firstReceipt.findElements(By.tagName("td"));
            assertTrue(receiptColumns.size() >= 5, "Không đủ thông tin cho hóa đơn");
            
            // Kiểm tra mã hóa đơn
            assertFalse(receiptColumns.get(0).getText().isEmpty(), "Mã hóa đơn trống");
            
            // Kiểm tra tên khách hàng
            assertFalse(receiptColumns.get(1).getText().isEmpty(), "Tên khách hàng trống");
            
            // Kiểm tra ngày lập
            String dateText = receiptColumns.get(2).getText();
            assertTrue(dateText.matches("\\d{2}/\\d{2}/\\d{4}"), "Định dạng ngày không đúng");
            
            // Kiểm tra tổng tiền
            String totalText = receiptColumns.get(3).getText();
            assertTrue(totalText.contains("VNĐ"), "Đơn vị tiền tệ không hiển thị");
            
            // Kiểm tra trạng thái
            WebElement statusBadge = receiptColumns.get(4).findElement(By.tagName("span"));
            assertTrue(statusBadge.isDisplayed(), "Badge trạng thái không hiển thị");
            
            // Kiểm tra nút xem chi tiết
            WebElement viewButton = receiptColumns.get(5).findElement(By.className("btn-view"));
            assertTrue(viewButton.isDisplayed(), "Nút xem chi tiết không hiển thị");
            assertTrue(viewButton.getText().contains("Xem"), "Nội dung nút xem không đúng");
        }
    }

    /**
     * TC_REC_LIST_002: Phân trang danh sách hóa đơn
     * Mô tả: Kiểm tra chức năng phân trang khi hiển thị danh sách hóa đơn
     */
    @Test
    public void testReceiptPagination() {
        // Truy cập trang hóa đơn
        driver.get(BASE_URL + "/admin/receipts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        
        // Kiểm tra xem có phân trang không
        List<WebElement> paginationElements = driver.findElements(By.cssSelector(".pagination .page-item"));
        
        // Nếu không có phân trang hoặc chỉ có một trang, bỏ qua test
        if (paginationElements.size() <= 3) { // Thường sẽ có nút Previous, 1, Next
            System.out.println("Không đủ dữ liệu để phân trang, bỏ qua test");
            return;
        }
        
        // Lấy ID hóa đơn đầu tiên ở trang 1
        List<WebElement> receiptsOnFirstPage = driver.findElements(By.cssSelector("table tbody tr"));
        String firstPageFirstReceipt = "";
        
        // Chỉ lấy ID nếu có hóa đơn
        if (!receiptsOnFirstPage.isEmpty() && receiptsOnFirstPage.get(0).findElements(By.tagName("td")).size() > 1) {
            firstPageFirstReceipt = receiptsOnFirstPage.get(0).findElements(By.tagName("td")).get(0).getText();
        } else {
            System.out.println("Không có hóa đơn nào trên trang 1, bỏ qua test");
            return;
        }
        
        // Tìm nút trang thứ 2 và nhấn vào
        WebElement page2Button = null;
        for (WebElement pageItem : paginationElements) {
            if (pageItem.getText().equals("2")) {
                page2Button = pageItem.findElement(By.tagName("a"));
                break;
            }
        }
        
        if (page2Button == null) {
            System.out.println("Không tìm thấy nút trang 2, bỏ qua test");
            return;
        }
        
        // Lăn chuột đến nút phân trang và nhấn vào trang 2
        scrollToElement(page2Button);
        page2Button.click();
        
        // Đợi để trang tải lại
        sleep(2000);
        
        // Kiểm tra URL đã thay đổi
        assertTrue(driver.getCurrentUrl().contains("page=2"), 
                "URL không chứa tham số page=2 sau khi nhấn vào trang 2");
        
        // Lấy danh sách hóa đơn ở trang thứ 2
        List<WebElement> receiptsOnSecondPage = driver.findElements(By.cssSelector("table tbody tr"));
        
        // Nếu không có hóa đơn ở trang 2, bỏ qua test
        if (receiptsOnSecondPage.isEmpty() || receiptsOnSecondPage.get(0).findElements(By.tagName("td")).size() <= 1) {
            System.out.println("Không có hóa đơn ở trang 2, bỏ qua test");
            return;
        }
        
        String secondPageFirstReceipt = receiptsOnSecondPage.get(0).findElements(By.tagName("td")).get(0).getText();
        
        // So sánh hóa đơn đầu tiên ở trang 1 và trang 2, phải khác nhau
        assertFalse(firstPageFirstReceipt.equals(secondPageFirstReceipt), 
                "Hóa đơn đầu tiên ở trang 1 và trang 2 giống nhau - phân trang không hoạt động");
    }

    /**
     * TC_REC_DETAIL_001: Xem chi tiết hóa đơn
     * Mô tả: Kiểm tra chức năng hiển thị chi tiết hóa đơn
     */
    @Test
    public void testViewReceiptDetail() {
        // Truy cập trang hóa đơn
        driver.get(BASE_URL + "/admin/receipts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        
        // Kiểm tra xem có hóa đơn không
        List<WebElement> receiptRows = driver.findElements(By.cssSelector("table tbody tr"));
        
        // Nếu không có hóa đơn, bỏ qua test
        if (receiptRows.isEmpty() || receiptRows.get(0).findElements(By.tagName("td")).size() <= 1) {
            System.out.println("Không có hóa đơn nào để xem chi tiết, bỏ qua test");
            return;
        }
        
        // Lấy mã hóa đơn đầu tiên để so sánh sau
        WebElement firstReceipt = receiptRows.get(0);
        String receiptId = firstReceipt.findElements(By.tagName("td")).get(0).getText();
        
        // Nhấn vào nút "Xem" của hóa đơn đầu tiên
        WebElement viewButton = firstReceipt.findElement(By.className("btn-view"));
        scrollToElement(viewButton);
        viewButton.click();
        
        // Đợi trang chi tiết hóa đơn tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("receipt-header")));
        
        // Kiểm tra tiêu đề trang
        WebElement pageTitle = driver.findElement(By.className("navbar-brand"));
        assertTrue(pageTitle.getText().contains("Chi tiết hóa đơn"), "Tiêu đề trang chi tiết không đúng");
        
        // Kiểm tra mã hóa đơn trên trang chi tiết
        WebElement receiptTitle = driver.findElement(By.className("receipt-title"));
        assertTrue(receiptTitle.getText().contains(receiptId), 
                "Mã hóa đơn trên trang chi tiết không khớp với mã hóa đơn đã chọn");
        
        // Kiểm tra hiển thị thông tin chung
        WebElement receiptInfo = driver.findElement(By.className("receipt-info"));
        assertTrue(receiptInfo.isDisplayed(), "Thông tin hóa đơn không hiển thị");
        
        // Kiểm tra hiển thị thông tin khách hàng
        List<WebElement> receiptFields = receiptInfo.findElements(By.className("receipt-field"));
        assertTrue(receiptFields.size() >= 2, "Không đủ thông tin khách hàng");
        
        // Kiểm tra hiển thị bảng chi tiết
        WebElement detailTable = driver.findElement(By.cssSelector(".card-body .table"));
        assertTrue(detailTable.isDisplayed(), "Bảng chi tiết hóa đơn không hiển thị");
        
        // Kiểm tra hiển thị các cột trong bảng chi tiết
        List<WebElement> detailHeaders = detailTable.findElements(By.cssSelector("thead th"));
        assertTrue(detailHeaders.size() >= 5, "Không đủ cột trong bảng chi tiết");
        
        // Kiểm tra nếu có chi tiết hoặc thông báo không có chi tiết
        List<WebElement> detailRows = detailTable.findElements(By.cssSelector("tbody tr"));
        assertTrue(detailRows.size() > 0, "Không hiển thị dòng nào trong bảng chi tiết");
        
        // Kiểm tra hiển thị tổng tiền
        WebElement totalRow = detailTable.findElement(By.cssSelector("tfoot .total-row"));
        assertTrue(totalRow.isDisplayed(), "Dòng tổng tiền không hiển thị");
        assertTrue(totalRow.getText().contains("Tổng cộng"), "Nội dung dòng tổng tiền không đúng");
    }

    /**
     * TC_REC_DETAIL_002: Kiểm tra tính toán giá trị hóa đơn
     * Mô tả: Kiểm tra tính chính xác của các giá trị tính toán trong hóa đơn
     */
    @Test
    public void testReceiptCalculation() {
        // Truy cập trang hóa đơn
        driver.get(BASE_URL + "/admin/receipts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        
        // Kiểm tra xem có hóa đơn không
        List<WebElement> receiptRows = driver.findElements(By.cssSelector("table tbody tr"));
        
        // Nếu không có hóa đơn, bỏ qua test
        if (receiptRows.isEmpty() || receiptRows.get(0).findElements(By.tagName("td")).size() <= 1) {
            System.out.println("Không có hóa đơn nào để kiểm tra tính toán, bỏ qua test");
            return;
        }
        
        // Nhấn vào nút "Xem" của hóa đơn đầu tiên
        WebElement viewButton = receiptRows.get(0).findElement(By.className("btn-view"));
        scrollToElement(viewButton);
        viewButton.click();
        
        // Đợi trang chi tiết hóa đơn tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("receipt-header")));
        
        // Lấy bảng chi tiết hóa đơn
        WebElement detailTable = driver.findElement(By.cssSelector(".card-body .table"));
        
        // Lấy danh sách các dòng chi tiết
        List<WebElement> detailRows = detailTable.findElements(By.cssSelector("tbody tr"));
        
        // Nếu không có chi tiết, bỏ qua test
        if (detailRows.isEmpty() || detailRows.get(0).findElements(By.tagName("td")).size() <= 1) {
            System.out.println("Không có chi tiết hóa đơn để kiểm tra tính toán, bỏ qua test");
            return;
        }
        
        // Tính tổng thành tiền từ các dòng chi tiết
        double calculatedTotal = 0.0;
        
        for (WebElement row : detailRows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            
            // Nếu là dòng "Chưa có chi tiết hóa đơn", bỏ qua
            if (columns.size() < 5) continue;
            
            // Lấy đơn giá từ cột thứ 3
            String priceText = columns.get(2).getText().replace("VNĐ", "").replace(",", "").trim();
            double price = Double.parseDouble(priceText);
            
            // Lấy số lượng từ cột thứ 4
            String quantityText = columns.get(3).getText().trim();
            int quantity = Integer.parseInt(quantityText);
            
            // Lấy thành tiền từ cột thứ 5
            String subtotalText = columns.get(4).getText().replace("VNĐ", "").replace(",", "").trim();
            double subtotal = Double.parseDouble(subtotalText);
            
            // Kiểm tra tính đúng đắn của thành tiền (đơn giá * số lượng)
            double expectedSubtotal = price * quantity;
            assertEquals(expectedSubtotal, subtotal, 1.0, 
                    "Thành tiền không bằng đơn giá * số lượng cho dòng " + columns.get(1).getText());
            
            // Cộng vào tổng
            calculatedTotal += subtotal;
        }
        
        // Lấy tổng tiền từ footer
        WebElement totalElement = detailTable.findElement(By.cssSelector("tfoot .total-row td:last-child"));
        String totalText = totalElement.getText().replace("VNĐ", "").replace(",", "").trim();
        double displayedTotal = Double.parseDouble(totalText);
        
        // So sánh tổng tiền hiển thị với tổng tiền tính toán
        assertEquals(calculatedTotal, displayedTotal, 1.0, 
                "Tổng tiền hiển thị không bằng tổng các thành tiền");
    }
} 