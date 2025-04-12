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

public class TestMedicine {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:2593";

    @BeforeEach
    public void setUp() {
        // Cấu hình EdgeDriver sử dụng WebDriverManager
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        // Không cần thiết lập headless mode vì yêu cầu là hiển thị
        
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
     * TC_ADD_001: Thêm thuốc thành công với đầy đủ thông tin hợp lệ
     * Mô tả: Kiểm tra chức năng thêm thuốc mới với tất cả các trường thông tin hợp lệ
     */
    @Test
    public void testAddMedicineWithValidInfo() {
        // Truy cập trang "Thêm thuốc mới"
        driver.get(BASE_URL + "/admin/medicines/add");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Điền đầy đủ thông tin hợp lệ
        String tenThuoc = "Test Thuốc " + System.currentTimeMillis();
        
        driver.findElement(By.id("tenThuoc")).sendKeys(tenThuoc);
        driver.findElement(By.id("gia")).sendKeys("150000");
        driver.findElement(By.id("soLuong")).sendKeys("100");
        driver.findElement(By.id("tuoiSuDung")).sendKeys("36 tháng");
        
        // Điền thông tin không bắt buộc
        driver.findElement(By.id("thanhPhan")).sendKeys("Thành phần test");
        driver.findElement(By.id("moTa")).sendKeys("Mô tả test");
        driver.findElement(By.id("luuTru")).sendKeys("Bảo quản nơi khô ráo");
        driver.findElement(By.id("nhaSanXuat")).sendKeys("Nhà sản xuất test");
        driver.findElement(By.id("tacDungPhu")).sendKeys("Không có tác dụng phụ");
        driver.findElement(By.id("quyCach")).sendKeys("Hộp 10 viên");
        driver.findElement(By.id("hinhAnh")).sendKeys("https://example.com/image.jpg");
        
        // Tạm dừng để người dùng xem thông tin đã điền
        sleep(2000);
        
        // Lăn chuột đến nút "Lưu thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Lưu thuốc"
        saveButton.click();
        
        // Kiểm tra kết quả - chờ đến khi chuyển hướng về trang danh sách thuốc
        wait.until(ExpectedConditions.urlContains("/admin/medicine"));
        
        // Đợi để trang tải hoàn tất
        sleep(1000);
        
        // Kiểm tra xem có hiển thị thông báo thành công không
        // Theo HTML của medicine.html, thông báo thành công là <div class="alert alert-success">
        try {
            WebElement successAlert = driver.findElement(By.className("alert-success"));
            assertTrue(successAlert.isDisplayed() && successAlert.getText().contains("thành công"), 
                    "Thông báo thành công không hiển thị hoặc không chứa từ 'thành công'");
            System.out.println("Thông báo thành công: " + successAlert.getText());
        } catch (Exception e) {
            System.out.println("Không tìm thấy thông báo thành công với class 'alert-success', sẽ kiểm tra thuốc có trong danh sách không");
        }
        
        // Kiểm tra xem thuốc mới có xuất hiện trong danh sách không
        boolean foundMedicine = driver.findElements(By.className("medicine-title")).stream()
                .anyMatch(element -> element.getText().contains(tenThuoc));
        
        assertTrue(foundMedicine, "Không tìm thấy thuốc vừa thêm trong danh sách");
    }

    /**
     * TC_ADD_002: Thêm thuốc với các trường bắt buộc để trống
     * Mô tả: Kiểm tra xử lý khi người dùng không nhập các trường bắt buộc
     */
    @Test
    public void testAddMedicineWithEmptyRequiredFields() {
        // Truy cập trang "Thêm thuốc mới"
        driver.get(BASE_URL + "/admin/medicines/add");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Để trống các trường bắt buộc
        // Click nút "Lưu thuốc" ngay mà không điền thông tin
        sleep(1000); // Tạm dừng để quan sát form trống
        
        // Lăn chuột đến nút "Lưu thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Lưu thuốc"
        saveButton.click();
        
        // Kiểm tra hiển thị lỗi
        sleep(1000); // Đợi để thông báo lỗi hiển thị
        
        // Kiểm tra URL vẫn ở trang thêm thuốc (không chuyển hướng có nghĩa là thêm thất bại)
        assertTrue(driver.getCurrentUrl().contains("/medicines/add"), 
                "Đã chuyển hướng khỏi trang thêm thuốc khi có lỗi");
    }

    /**
     * TC_ADD_003: Thêm thuốc với giá trị âm cho giá tiền và số lượng
     * Mô tả: Kiểm tra xử lý khi người dùng nhập giá tiền và số lượng là số âm
     */
    @Test
    public void testAddMedicineWithNegativeValues() {
        // Truy cập trang "Thêm thuốc mới"
        driver.get(BASE_URL + "/admin/medicines/add");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Điền đầy đủ thông tin nhưng với giá trị âm cho giá và số lượng
        driver.findElement(By.id("tenThuoc")).sendKeys("Test Thuốc Giá Âm");
        driver.findElement(By.id("gia")).sendKeys("-50000"); // Giá âm
        driver.findElement(By.id("soLuong")).sendKeys("-10"); // Số lượng âm
        driver.findElement(By.id("tuoiSuDung")).sendKeys("24 tháng");
        
        // Tạm dừng để người dùng xem thông tin đã điền
        sleep(2000);
        
        // Lăn chuột đến nút "Lưu thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Lưu thuốc"
        saveButton.click();
        
        // Kiểm tra hiển thị lỗi
        sleep(1000); // Đợi để thông báo lỗi hiển thị
        
        // Kiểm tra URL vẫn ở trang thêm thuốc (không chuyển hướng)
        assertTrue(driver.getCurrentUrl().contains("/medicines/add"), 
                "Đã chuyển hướng khỏi trang thêm thuốc khi có lỗi");
    }
    
    /**
     * TC_EDIT_001: Chỉnh sửa thông tin thuốc thành công
     * Mô tả: Kiểm tra chức năng chỉnh sửa thông tin thuốc
     */
    @Test
    public void testEditMedicineSuccessful() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Tìm nút "Sửa" của thuốc đầu tiên và nhấn vào
        WebElement editButton = driver.findElement(By.cssSelector(".btn-edit"));
        scrollToElement(editButton);
        editButton.click();
        
        // Đợi cho trang chỉnh sửa thuốc tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Lưu lại thông tin cũ để kiểm tra sau khi cập nhật
        String originalName = driver.findElement(By.id("tenThuoc")).getAttribute("value");
        
        // Thay đổi thông tin của thuốc
        String newName = "Đã sửa " + System.currentTimeMillis();
        
        // Xóa tên cũ và nhập tên mới
        WebElement nameInput = driver.findElement(By.id("tenThuoc"));
        nameInput.clear();
        nameInput.sendKeys(newName);
        
        // Thay đổi giá
        WebElement priceInput = driver.findElement(By.id("gia"));
        priceInput.clear();
        priceInput.sendKeys("200000");
        
        // Thay đổi số lượng
        WebElement quantityInput = driver.findElement(By.id("soLuong"));
        quantityInput.clear();
        quantityInput.sendKeys("50");
        
        // Thay đổi mô tả
        WebElement descriptionInput = driver.findElement(By.id("moTa"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Mô tả đã được cập nhật");
        
        // Tạm dừng để người dùng xem thông tin đã điền
        sleep(2000);
        
        // Lăn chuột đến nút "Cập nhật thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Cập nhật thuốc"
        saveButton.click();
        
        // Kiểm tra kết quả - chờ đến khi chuyển hướng về trang danh sách thuốc
        wait.until(ExpectedConditions.urlContains("/admin/medicine"));
        
        // Đợi để trang tải hoàn tất
        sleep(1000);
        
        // Kiểm tra xem có hiển thị thông báo thành công không
        try {
            WebElement successAlert = driver.findElement(By.className("alert-success"));
            assertTrue(successAlert.isDisplayed() && successAlert.getText().contains("thành công"), 
                    "Thông báo thành công không hiển thị hoặc không chứa từ 'thành công'");
            System.out.println("Thông báo thành công: " + successAlert.getText());
        } catch (Exception e) {
            System.out.println("Không tìm thấy thông báo thành công với class 'alert-success', sẽ kiểm tra thuốc có trong danh sách không");
        }
        
        // Kiểm tra xem thuốc đã được cập nhật chưa
        boolean foundUpdatedMedicine = driver.findElements(By.className("medicine-title")).stream()
                .anyMatch(element -> element.getText().contains(newName));
        
        assertTrue(foundUpdatedMedicine, "Không tìm thấy thuốc sau khi cập nhật trong danh sách");
    }
    
    /**
     * TC_EDIT_002: Chỉnh sửa thuốc với các trường bắt buộc để trống
     * Mô tả: Kiểm tra xử lý khi người dùng xóa nội dung của các trường bắt buộc
     */
    @Test
    public void testEditMedicineWithEmptyRequiredFields() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Tìm nút "Sửa" của thuốc đầu tiên và nhấn vào
        WebElement editButton = driver.findElement(By.cssSelector(".btn-edit"));
        scrollToElement(editButton);
        editButton.click();
        
        // Đợi cho trang chỉnh sửa thuốc tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Xóa nội dung của các trường bắt buộc
        driver.findElement(By.id("tenThuoc")).clear();
        driver.findElement(By.id("gia")).clear();
        driver.findElement(By.id("soLuong")).clear();
        driver.findElement(By.id("tuoiSuDung")).clear();
        
        // Tạm dừng để người dùng xem form đã xóa nội dung
        sleep(1000);
        
        // Lăn chuột đến nút "Cập nhật thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Cập nhật thuốc"
        saveButton.click();
        
        // Kiểm tra hiển thị lỗi
        sleep(1000); // Đợi để thông báo lỗi hiển thị
        
        // Kiểm tra URL vẫn ở trang chỉnh sửa thuốc (không chuyển hướng)
        assertTrue(driver.getCurrentUrl().contains("/medicines/edit/"), 
                "Đã chuyển hướng khỏi trang chỉnh sửa thuốc khi có lỗi");
        
        // Kiểm tra các thông báo lỗi có hiển thị không
        assertTrue(driver.findElement(By.id("tenThuoc")).getAttribute("class").contains("is-invalid"), 
                "Lỗi không hiển thị cho trường tên thuốc");
        assertTrue(driver.findElement(By.id("gia")).getAttribute("class").contains("is-invalid"), 
                "Lỗi không hiển thị cho trường giá");
        assertTrue(driver.findElement(By.id("soLuong")).getAttribute("class").contains("is-invalid"), 
                "Lỗi không hiển thị cho trường số lượng");
        assertTrue(driver.findElement(By.id("tuoiSuDung")).getAttribute("class").contains("is-invalid"), 
                "Lỗi không hiển thị cho trường tuổi sử dụng");
    }
    
    /**
     * TC_EDIT_003: Chỉnh sửa giá thuốc thành giá trị âm
     * Mô tả: Kiểm tra xử lý khi người dùng nhập giá tiền là số âm
     */
    @Test
    public void testEditMedicineWithNegativePrice() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Tìm nút "Sửa" của thuốc đầu tiên và nhấn vào
        WebElement editButton = driver.findElement(By.cssSelector(".btn-edit"));
        scrollToElement(editButton);
        editButton.click();
        
        // Đợi cho trang chỉnh sửa thuốc tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Thay đổi giá thành số âm
        WebElement priceInput = driver.findElement(By.id("gia"));
        priceInput.clear();
        priceInput.sendKeys("-50000");
        
        // Tạm dừng để người dùng xem thông tin đã nhập
        sleep(1000);
        
        // Lăn chuột đến nút "Cập nhật thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Cập nhật thuốc"
        saveButton.click();
        
        // Kiểm tra hiển thị lỗi
        sleep(1000); // Đợi để thông báo lỗi hiển thị
        
        // Kiểm tra URL vẫn ở trang chỉnh sửa thuốc (không chuyển hướng)
        assertTrue(driver.getCurrentUrl().contains("/medicines/edit/"), 
                "Đã chuyển hướng khỏi trang chỉnh sửa thuốc khi có lỗi");
    }
    
    /**
     * TC_EDIT_004: Chỉnh sửa số lượng thuốc thành số âm
     * Mô tả: Kiểm tra xử lý khi người dùng nhập số lượng là số âm
     */
    @Test
    public void testEditMedicineWithNegativeQuantity() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Tìm nút "Sửa" của thuốc đầu tiên và nhấn vào
        WebElement editButton = driver.findElement(By.cssSelector(".btn-edit"));
        scrollToElement(editButton);
        editButton.click();
        
        // Đợi cho trang chỉnh sửa thuốc tải xong
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenThuoc")));
        
        // Thay đổi số lượng thành số âm
        WebElement quantityInput = driver.findElement(By.id("soLuong"));
        quantityInput.clear();
        quantityInput.sendKeys("-10");
        
        // Tạm dừng để người dùng xem thông tin đã nhập
        sleep(1000);
        
        // Lăn chuột đến nút "Cập nhật thuốc" trước khi click
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        scrollToElement(saveButton);
        
        // Click nút "Cập nhật thuốc"
        saveButton.click();
        
        // Kiểm tra hiển thị lỗi
        sleep(1000); // Đợi để thông báo lỗi hiển thị
        
        // Kiểm tra URL vẫn ở trang chỉnh sửa thuốc (không chuyển hướng)
        assertTrue(driver.getCurrentUrl().contains("/medicines/edit/"), 
                "Đã chuyển hướng khỏi trang chỉnh sửa thuốc khi có lỗi");
    }
    
    /**
     * TC_DEL_001: Xóa thuốc thành công
     * Mô tả: Kiểm tra chức năng xóa thuốc
     */
    @Test
    public void testDeleteMedicineSuccessful() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Lấy danh sách thuốc trước khi xóa để so sánh
        List<WebElement> medicineBeforeDelete = driver.findElements(By.className("medicine-card"));
        int countBefore = medicineBeforeDelete.size();
        
        // Lấy tên thuốc sẽ xóa để kiểm tra sau
        WebElement firstMedicineTitle = driver.findElement(By.className("medicine-title"));
        String medicineTitleToDelete = firstMedicineTitle.getText();
        
        // Tìm nút "Xóa" của thuốc đầu tiên
        WebElement deleteButton = driver.findElement(By.cssSelector(".btn-danger"));
        scrollToElement(deleteButton);
        
        // Lưu URL hiện tại để sau khi xóa xong quay lại
        String currentUrl = driver.getCurrentUrl();
        
        // Overriding default confirm dialog
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "window.confirm = function() { return true; }");
        
        // Click nút "Xóa"
        deleteButton.click();
        
        // Đợi để xác nhận xóa được xử lý và trang được tải lại
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        sleep(1000);
        
        // Kiểm tra thông báo thành công
        WebElement successAlert = driver.findElement(By.className("alert-success"));
        assertTrue(successAlert.isDisplayed() && successAlert.getText().contains("xóa"), 
                "Thông báo xóa thành công không hiển thị");
        
        // Kiểm tra số lượng thuốc sau khi xóa
        List<WebElement> medicineAfterDelete = driver.findElements(By.className("medicine-card"));
        int countAfter = medicineAfterDelete.size();
        
        assertEquals(countBefore - 1, countAfter, "Số lượng thuốc không giảm sau khi xóa");
        
        // Kiểm tra xem thuốc đã bị xóa không còn trong danh sách
        boolean medicineStillExists = driver.findElements(By.className("medicine-title")).stream()
                .anyMatch(element -> element.getText().equals(medicineTitleToDelete));
        
        assertFalse(medicineStillExists, "Thuốc vẫn còn tồn tại sau khi xóa");
    }
    
    /**
     * TC_LIST_001: Phân trang danh sách thuốc
     * Mô tả: Kiểm tra chức năng phân trang khi hiển thị danh sách thuốc
     */
    @Test
    public void testMedicinePagination() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Kiểm tra xem có phân trang không
        List<WebElement> paginationElements = driver.findElements(By.cssSelector(".pagination .page-item"));
        
        // Nếu không có phân trang hoặc chỉ có một trang, bỏ qua test
        if (paginationElements.size() <= 3) { // Thường sẽ có nút Previous, 1, Next
            System.out.println("Không đủ dữ liệu để phân trang, bỏ qua test");
            return;
        }
        
        // Lấy danh sách thuốc ở trang đầu
        List<WebElement> medicinesOnFirstPage = driver.findElements(By.className("medicine-title"));
        String firstPageFirstMedicine = medicinesOnFirstPage.get(0).getText();
        
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
        
        // Lăn chuột đến nút phân trang
        scrollToElement(page2Button);
        
        // Nhấn vào trang 2
        page2Button.click();
        
        // Đợi để trang tải lại
        sleep(2000);
        
        // Kiểm tra URL đã thay đổi
        assertTrue(driver.getCurrentUrl().contains("page=2"), 
                "URL không chứa tham số page=2 sau khi nhấn vào trang 2");
        
        // Lấy danh sách thuốc ở trang thứ 2
        List<WebElement> medicinesOnSecondPage = driver.findElements(By.className("medicine-title"));
        
        // Nếu không có thuốc ở trang 2, bỏ qua test
        if (medicinesOnSecondPage.isEmpty()) {
            System.out.println("Không có thuốc ở trang 2, bỏ qua test");
            return;
        }
        
        String secondPageFirstMedicine = medicinesOnSecondPage.get(0).getText();
        
        // So sánh thuốc đầu tiên ở trang 1 và trang 2, phải khác nhau
        assertFalse(firstPageFirstMedicine.equals(secondPageFirstMedicine), 
                "Thuốc đầu tiên ở trang 1 và trang 2 giống nhau - phân trang không hoạt động");
    }
    
    /**
     * TC_LIST_002: Hiển thị chi tiết thông tin thuốc
     * Mô tả: Kiểm tra việc hiển thị thông tin chi tiết của thuốc trong danh sách
     */
    @Test
    public void testMedicineDetailDisplay() {
        // Truy cập trang danh sách thuốc
        driver.get(BASE_URL + "/admin/medicine");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("medicine-card")));
        
        // Kiểm tra các thành phần hiển thị thông tin của thuốc
        List<WebElement> medicineCards = driver.findElements(By.className("medicine-card"));
        
        assertTrue(medicineCards.size() > 0, "Không có thuốc nào hiển thị trong danh sách");
        
        // Kiểm tra thuốc đầu tiên
        WebElement firstMedicineCard = medicineCards.get(0);
        
        // Kiểm tra hiển thị hình ảnh
        assertTrue(firstMedicineCard.findElement(By.className("medicine-image")).isDisplayed(), 
                "Hình ảnh thuốc không hiển thị");
        
        // Kiểm tra hiển thị tên thuốc
        assertTrue(firstMedicineCard.findElement(By.className("medicine-title")).isDisplayed(), 
                "Tên thuốc không hiển thị");
        
        // Kiểm tra hiển thị mô tả (có thể không có)
        try {
            WebElement description = firstMedicineCard.findElement(By.className("medicine-description"));
            assertTrue(description.isDisplayed(), "Mô tả thuốc không hiển thị");
        } catch (Exception e) {
            System.out.println("Thuốc không có mô tả");
        }
        
        // Kiểm tra hiển thị giá
        assertTrue(firstMedicineCard.findElement(By.className("price")).isDisplayed(), 
                "Giá thuốc không hiển thị");
        
        // Kiểm tra hiển thị nút Sửa và Xóa
        assertTrue(firstMedicineCard.findElement(By.cssSelector(".btn-edit")).isDisplayed(), 
                "Nút Sửa không hiển thị");
        assertTrue(firstMedicineCard.findElement(By.cssSelector(".btn-danger")).isDisplayed(), 
                "Nút Xóa không hiển thị");
    }
} 