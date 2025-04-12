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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Test Selenium cho chức năng quản lý nhân viên
 */
public class TestEmployeeManagement {

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
     * TC_EMP_LIST_001: Xem danh sách nhân viên
     * Mô tả: Kiểm tra chức năng hiển thị danh sách nhân viên
     */
    @Test
    public void testViewEmployeeList() {
        // Truy cập trang nhân viên
        driver.get(BASE_URL + "/admin/employees");
        
        // Đợi cho trang tải xong, kiểm tra tiêu đề trang
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("navbar-brand")));
        WebElement pageTitle = driver.findElement(By.className("navbar-brand"));
        assertTrue(pageTitle.getText().contains("Quản lý nhân viên"), "Tiêu đề trang không đúng");
        
        // Kiểm tra xem có danh sách nhân viên không
        List<WebElement> employeeCards = driver.findElements(By.className("employee-card"));
        assertTrue(employeeCards.size() > 0, "Không có nhân viên nào hiển thị");
        
        // Kiểm tra hiển thị thông tin nhân viên
        WebElement firstEmployee = employeeCards.get(0);
        assertTrue(firstEmployee.findElement(By.className("employee-title")).isDisplayed(), "Tên nhân viên không hiển thị");
        assertTrue(firstEmployee.findElement(By.className("employee-info")).isDisplayed(), "Thông tin nhân viên không hiển thị");
        assertTrue(firstEmployee.findElement(By.className("employee-role")).isDisplayed(), "Vai trò nhân viên không hiển thị");
        
        // Kiểm tra hiển thị các nút chức năng
        assertTrue(firstEmployee.findElement(By.className("btn-edit")).isDisplayed(), "Nút sửa không hiển thị");
        assertTrue(firstEmployee.findElement(By.className("btn-danger")).isDisplayed(), "Nút xóa không hiển thị");
        
        // Kiểm tra nút thêm nhân viên mới
        WebElement addButton = driver.findElement(By.className("add-employee-btn"));
        assertTrue(addButton.isDisplayed(), "Nút thêm nhân viên không hiển thị");
        assertTrue(addButton.getText().contains("Thêm nhân viên mới"), "Nội dung nút thêm nhân viên không đúng");
    }

    /**
     * TC_EMP_LIST_002: Phân trang danh sách nhân viên
     * Mô tả: Kiểm tra chức năng phân trang khi hiển thị danh sách nhân viên
     */
    @Test
    public void testEmployeePagination() {
        // Truy cập trang nhân viên
        driver.get(BASE_URL + "/admin/employees");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("employee-card")));
        
        // Kiểm tra xem có phân trang không
        List<WebElement> paginationElements = driver.findElements(By.cssSelector(".pagination .page-item"));
        
        // Nếu không có phân trang hoặc chỉ có một trang, bỏ qua test
        if (paginationElements.size() <= 3) { // Thường sẽ có nút Previous, 1, Next
            System.out.println("Không đủ dữ liệu để phân trang, bỏ qua test");
            return;
        }
        
        // Lấy danh sách nhân viên ở trang đầu
        List<WebElement> employeesOnFirstPage = driver.findElements(By.className("employee-title"));
        String firstPageFirstEmployee = employeesOnFirstPage.get(0).getText();
        
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
        
        // Lấy danh sách nhân viên ở trang thứ 2
        List<WebElement> employeesOnSecondPage = driver.findElements(By.className("employee-title"));
        
        // Nếu không có nhân viên ở trang 2, bỏ qua test
        if (employeesOnSecondPage.isEmpty()) {
            System.out.println("Không có nhân viên ở trang 2, bỏ qua test");
            return;
        }
        
        String secondPageFirstEmployee = employeesOnSecondPage.get(0).getText();
        
        // So sánh nhân viên đầu tiên ở trang 1 và trang 2, phải khác nhau
        assertFalse(firstPageFirstEmployee.equals(secondPageFirstEmployee), 
                "Nhân viên đầu tiên ở trang 1 và trang 2 giống nhau - phân trang không hoạt động");
    }

    /**
     * TC_EMP_ADD_001: Thêm nhân viên thành công
     * Mô tả: Kiểm tra chức năng thêm nhân viên mới
     */
    @Test
    public void testAddEmployee() {
        // Truy cập trang nhân viên
        driver.get(BASE_URL + "/admin/employees");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("add-employee-btn")));
        
        // Lấy số lượng nhân viên trước khi thêm để so sánh sau
        int employeeCountBefore = driver.findElements(By.className("employee-card")).size();
        
        // Click vào nút "Thêm nhân viên mới"
        WebElement addButton = driver.findElement(By.className("add-employee-btn"));
        addButton.click();
        
        // Đợi cho modal hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addEmployeeModal")));
        
        // Tạo tên nhân viên với timestamp để đảm bảo tính duy nhất
        String newEmployeeName = "Test Nhân Viên " + System.currentTimeMillis();
        String newEmployeePhone = "09" + System.currentTimeMillis() % 100000000;
        
        // Điền thông tin nhân viên mới
        driver.findElement(By.id("tenNguoiDung")).sendKeys(newEmployeeName);
        driver.findElement(By.id("soDienThoai")).sendKeys(newEmployeePhone);
        driver.findElement(By.id("diaChi")).sendKeys("123 Test Address");
        driver.findElement(By.id("matKhau")).sendKeys("password123");
        
        // Chọn vai trò
        Select roleSelect = new Select(driver.findElement(By.id("vaiTro")));
        roleSelect.selectByValue("nvgh"); // Nhân viên giao hàng
        
        // Tạm dừng để người dùng xem thông tin đã điền
        sleep(2000);
        
        // Click nút "Thêm nhân viên"
        WebElement submitButton = driver.findElement(By.cssSelector("#addEmployeeForm button[type='submit']"));
        submitButton.click();
        
        // Đợi cho modal đóng và trang reload
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("addEmployeeModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("employee-card")));
        
        // Kiểm tra thông báo thành công
        try {
            WebElement successAlert = driver.findElement(By.className("alert-success"));
            assertTrue(successAlert.isDisplayed(), "Thông báo thành công không hiển thị");
            assertTrue(successAlert.getText().contains("thành công"), 
                    "Thông báo không chứa từ 'thành công'");
        } catch (Exception e) {
            System.out.println("Không tìm thấy thông báo thành công, sẽ kiểm tra nhân viên có trong danh sách không");
        }
        
        // Đợi trang tải lại
        sleep(1000);
        
        // Kiểm tra số lượng nhân viên sau khi thêm
        int employeeCountAfter = driver.findElements(By.className("employee-card")).size();
        
        // So sánh số lượng trước và sau
        assertTrue(employeeCountAfter > employeeCountBefore, 
                "Số lượng nhân viên không tăng sau khi thêm");
        
        // Kiểm tra nhân viên mới có trong danh sách không
        boolean foundNewEmployee = driver.findElements(By.className("employee-title")).stream()
                .anyMatch(element -> element.getText().equals(newEmployeeName));
        
        assertTrue(foundNewEmployee, "Không tìm thấy nhân viên vừa thêm trong danh sách");
    }

    /**
     * TC_EMP_EDIT_001: Chỉnh sửa thông tin nhân viên thành công
     * Mô tả: Kiểm tra chức năng chỉnh sửa thông tin nhân viên
     */
    @Test
    public void testEditEmployee() {
        // Truy cập trang nhân viên
        driver.get(BASE_URL + "/admin/employees");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("employee-card")));
        
        // Tìm nút "Sửa" của nhân viên đầu tiên và nhấn vào
        WebElement editButton = driver.findElement(By.className("edit-employee-btn"));
        editButton.click();
        
        // Đợi cho modal hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editEmployeeModal")));
        
        // Lấy thông tin hiện tại
        String currentName = driver.findElement(By.id("editTenNguoiDung")).getAttribute("value");
        String currentPhone = driver.findElement(By.id("editSoDienThoai")).getAttribute("value");
        
        // Tạo thông tin mới
        String newName = "Đã Sửa " + System.currentTimeMillis();
        
        // Xóa thông tin cũ và nhập thông tin mới
        WebElement nameInput = driver.findElement(By.id("editTenNguoiDung"));
        nameInput.clear();
        nameInput.sendKeys(newName);
        
        // Tạm dừng để người dùng xem thông tin đã điền
        sleep(2000);
        
        // Click nút "Lưu thay đổi"
        WebElement submitButton = driver.findElement(By.cssSelector("#editEmployeeForm button[type='submit']"));
        submitButton.click();
        
        // Đợi cho modal đóng và trang reload
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editEmployeeModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("employee-card")));
        
        // Kiểm tra thông báo thành công
        try {
            WebElement successAlert = driver.findElement(By.className("alert-success"));
            assertTrue(successAlert.isDisplayed(), "Thông báo thành công không hiển thị");
            assertTrue(successAlert.getText().contains("thành công"), 
                    "Thông báo không chứa từ 'thành công'");
        } catch (Exception e) {
            System.out.println("Không tìm thấy thông báo thành công, sẽ kiểm tra nhân viên có được cập nhật không");
        }
        
        // Đợi trang tải lại
        sleep(1000);
        
        // Kiểm tra nhân viên đã được cập nhật chưa
        boolean foundUpdatedEmployee = driver.findElements(By.className("employee-title")).stream()
                .anyMatch(element -> element.getText().equals(newName));
        
        assertTrue(foundUpdatedEmployee, "Không tìm thấy nhân viên sau khi cập nhật");
        
        // Kiểm tra tên cũ không còn tồn tại (nếu tên đã thay đổi)
        if (!currentName.equals(newName)) {
            boolean oldNameStillExists = driver.findElements(By.className("employee-title")).stream()
                    .anyMatch(element -> element.getText().equals(currentName));
            
            assertFalse(oldNameStillExists, "Tên cũ vẫn còn tồn tại sau khi cập nhật");
        }
    }

    /**
     * TC_EMP_DEL_001: Xóa nhân viên thành công
     * Mô tả: Kiểm tra chức năng xóa nhân viên
     */
    @Test
    public void testDeleteEmployee() {
        // Truy cập trang nhân viên
        driver.get(BASE_URL + "/admin/employees");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("employee-card")));
        
        // Lấy số lượng nhân viên trước khi xóa để so sánh sau
        List<WebElement> employeeBeforeDelete = driver.findElements(By.className("employee-card"));
        int countBefore = employeeBeforeDelete.size();
        
        // Kiểm tra có nhân viên để xóa không
        if (countBefore == 0) {
            System.out.println("Không có nhân viên để xóa, bỏ qua test");
            return;
        }
        
        // Lấy tên nhân viên sẽ xóa để kiểm tra sau
        WebElement firstEmployeeTitle = driver.findElement(By.className("employee-title"));
        String employeeTitleToDelete = firstEmployeeTitle.getText();
        
        // Tìm nút "Xóa" của nhân viên đầu tiên
        WebElement deleteButton = driver.findElement(By.className("delete-employee-btn"));
        deleteButton.click();
        
        // Đợi cho modal hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteEmployeeModal")));
        
        // Kiểm tra xem tên nhân viên có hiển thị trong thông báo xác nhận không
        WebElement confirmMessage = driver.findElement(By.id("deleteEmployeeName"));
        assertEquals(employeeTitleToDelete, confirmMessage.getText(), 
                "Tên nhân viên trong thông báo xác nhận không khớp");
        
        // Tạm dừng để người dùng xem xác nhận xóa
        sleep(2000);
        
        // Click nút "Xóa nhân viên" trong modal
        WebElement confirmDeleteButton = driver.findElement(By.cssSelector("#deleteEmployeeForm button[type='submit']"));
        confirmDeleteButton.click();
        
        // Đợi cho modal đóng và trang reload
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("deleteEmployeeModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        
        // Kiểm tra thông báo thành công
        try {
            WebElement successAlert = driver.findElement(By.className("alert-success"));
            assertTrue(successAlert.isDisplayed(), "Thông báo thành công không hiển thị");
            assertTrue(successAlert.getText().contains("thành công"), 
                    "Thông báo không chứa từ 'thành công'");
        } catch (Exception e) {
            System.out.println("Không tìm thấy thông báo thành công, sẽ kiểm tra nhân viên đã bị xóa chưa");
        }
        
        // Đợi trang tải lại
        sleep(1000);
        
        // Kiểm tra số lượng nhân viên sau khi xóa
        List<WebElement> employeeAfterDelete = driver.findElements(By.className("employee-card"));
        int countAfter = employeeAfterDelete.size();
        
        // So sánh số lượng trước và sau
        assertEquals(countBefore - 1, countAfter, "Số lượng nhân viên không giảm sau khi xóa");
        
        // Kiểm tra nhân viên đã bị xóa không còn trong danh sách
        boolean employeeStillExists = driver.findElements(By.className("employee-title")).stream()
                .anyMatch(element -> element.getText().equals(employeeTitleToDelete));
        
        assertFalse(employeeStillExists, "Nhân viên vẫn còn tồn tại sau khi xóa");
    }
} 