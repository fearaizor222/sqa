# TEST CASES CHO CHỨC NĂNG ADMIN

## QUẢN LÝ THUỐC (MEDICINE MANAGEMENT)

### 1. Thêm Thuốc (Add Medicine)

#### TC_ADD_001: Thêm thuốc thành công với đầy đủ thông tin hợp lệ
- **Mô tả**: Kiểm tra chức năng thêm thuốc mới với tất cả các trường thông tin hợp lệ
- **Điều kiện tiên quyết**: Đăng nhập với tư cách admin
- **Bước thực hiện**:
  - Truy cập trang "Thêm thuốc mới"
  - Điền đầy đủ và hợp lệ tất cả các trường bắt buộc (tên thuốc, giá, số lượng, tuổi sử dụng)
  - Điền thông tin vào các trường không bắt buộc (thành phần, mô tả, lưu trữ, nhà sản xuất...)
  - Nhập URL hình ảnh hợp lệ
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**: 
  - Thuốc được thêm vào cơ sở dữ liệu
  - Hiển thị thông báo thành công
  - Chuyển hướng về trang danh sách thuốc
  - Thuốc mới xuất hiện trong danh sách
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo thành công sau khi thêm
  - Thuốc mới xuất hiện trong danh sách với thông tin đúng
  - Chuyển hướng về trang danh sách thuốc thành công

#### TC_ADD_002: Thêm thuốc với các trường bắt buộc để trống
- **Mô tả**: Kiểm tra xử lý khi người dùng không nhập các trường bắt buộc
- **Bước thực hiện**:
  - Truy cập trang "Thêm thuốc mới"
  - Để trống một hoặc nhiều trường bắt buộc (tên thuốc, giá, số lượng, tuổi sử dụng)
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**: 
  - Hiển thị thông báo lỗi
  - Không thêm thuốc vào hệ thống
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo lỗi cụ thể về trường thông tin bị thiếu
  - Hệ thống không tạo bản ghi thuốc mới
  - Vẫn ở trang thêm thuốc, không chuyển hướng về trang danh sách

#### TC_ADD_003: Thêm thuốc với giá trị âm cho giá tiền và số lượng
- **Mô tả**: Kiểm tra xử lý khi người dùng nhập giá tiền và số lượng là số âm
- **Bước thực hiện**:
  - Truy cập trang "Thêm thuốc mới"
  - Điền đầy đủ thông tin hợp lệ
  - Nhập giá tiền và số lượng là số âm
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**:
  - Hiển thị thông báo lỗi
  - Không thêm thuốc vào hệ thống
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo lỗi về giá trị âm không hợp lệ
  - Không cho phép tạo thuốc mới với giá trị âm
  - Không chuyển hướng về trang danh sách thuốc

### 2. Chỉnh Sửa Thuốc (Edit Medicine)

#### TC_EDIT_001: Chỉnh sửa thông tin thuốc thành công
- **Mô tả**: Kiểm tra chức năng chỉnh sửa thông tin thuốc
- **Điều kiện tiên quyết**: Đăng nhập với tư cách admin, có thuốc trong hệ thống
- **Bước thực hiện**:
  - Truy cập trang danh sách thuốc
  - Nhấn nút "Sửa" của một thuốc
  - Thay đổi thông tin (tên, giá, số lượng...)
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**:
  - Thông tin thuốc được cập nhật
  - Hiển thị thông báo thành công
  - Chuyển hướng về trang danh sách thuốc
  - Thông tin mới của thuốc được hiển thị
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Thông tin mới được cập nhật chính xác
  - Hiển thị thông báo thành công sau khi lưu
  - Sau khi chỉnh sửa thành công, chuyển hướng về trang danh sách

#### TC_EDIT_002: Chỉnh sửa thuốc với các trường bắt buộc để trống
- **Mô tả**: Kiểm tra xử lý khi người dùng xóa nội dung của các trường bắt buộc
- **Bước thực hiện**:
  - Truy cập trang chỉnh sửa thuốc
  - Xóa nội dung của một hoặc nhiều trường bắt buộc
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**:
  - Hiển thị thông báo lỗi
  - Không cập nhật thông tin thuốc
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo lỗi về các trường bắt buộc bị trống
  - Không lưu thay đổi và dữ liệu gốc vẫn được giữ nguyên
  - Vẫn ở trang chỉnh sửa, không chuyển hướng về danh sách

#### TC_EDIT_003: Chỉnh sửa giá thuốc thành giá trị âm
- **Mô tả**: Kiểm tra xử lý khi người dùng nhập giá tiền là số âm
- **Bước thực hiện**:
  - Truy cập trang chỉnh sửa thuốc
  - Thay đổi giá tiền thành số âm
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**:
  - Hiển thị thông báo lỗi
  - Không cập nhật thông tin thuốc
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo lỗi về giá tiền không được là số âm
  - Dữ liệu giá tiền không được cập nhật với giá trị âm
  - Dữ liệu gốc của thuốc không bị thay đổi

#### TC_EDIT_004: Chỉnh sửa số lượng thuốc thành số âm
- **Mô tả**: Kiểm tra xử lý khi người dùng nhập số lượng là số âm
- **Bước thực hiện**:
  - Truy cập trang chỉnh sửa thuốc
  - Thay đổi số lượng thành số âm
  - Nhấn nút "Lưu thuốc"
- **Kết quả mong đợi**:
  - Hiển thị thông báo lỗi
  - Không cập nhật thông tin thuốc
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo lỗi về số lượng không được là số âm
  - Dữ liệu số lượng không được cập nhật với giá trị âm
  - Dữ liệu gốc của thuốc không bị thay đổi

### 3. Xóa Thuốc (Delete Medicine)

#### TC_DEL_001: Xóa thuốc thành công
- **Mô tả**: Kiểm tra chức năng xóa thuốc
- **Điều kiện tiên quyết**: Đăng nhập với tư cách admin, có thuốc trong hệ thống
- **Bước thực hiện**:
  - Truy cập trang danh sách thuốc
  - Nhấn nút "Xóa" của một thuốc
  - Xác nhận xóa trong hộp thoại
- **Kết quả mong đợi**:
  - Thuốc được xóa khỏi hệ thống
  - Hiển thị thông báo thành công
  - Thuốc không còn xuất hiện trong danh sách
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị hộp thoại xác nhận trước khi xóa
  - Sau khi xóa, thuốc không còn trong danh sách
  - Hiển thị thông báo xóa thành công

### 4. Hiển Thị Danh Sách Thuốc (Medicine List)

#### TC_LIST_001: Phân trang danh sách thuốc
- **Mô tả**: Kiểm tra chức năng phân trang khi hiển thị danh sách thuốc
- **Bước thực hiện**:
  - Truy cập trang danh sách thuốc
  - Chuyển đến các trang khác nhau
- **Kết quả mong đợi**:
  - Hiển thị đúng số lượng thuốc trên mỗi trang
  - Nút phân trang hoạt động chính xác
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Kiểm tra hiển thị đúng thuốc trên mỗi trang
  - Xác nhận URL thay đổi khi chuyển trang (page=2, page=3,...)
  - Các nút phân trang hoạt động đúng và hiển thị đúng trạng thái (active, disabled)

#### TC_LIST_002: Hiển thị chi tiết thông tin thuốc
- **Mô tả**: Kiểm tra việc hiển thị thông tin chi tiết của thuốc trong danh sách
- **Bước thực hiện**:
  - Truy cập trang danh sách thuốc
- **Kết quả mong đợi**:
  - Hiển thị đúng thông tin: tên, mô tả, giá, hình ảnh
  - Định dạng giá tiền theo đúng định dạng tiền tệ
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Thông tin thuốc hiển thị đầy đủ và chính xác
  - Giá tiền định dạng đúng với đơn vị VNĐ
  - Hình ảnh thuốc hiển thị đúng kích thước và tỷ lệ


## QUẢN LÝ NHÂN VIÊN (EMPLOYEE MANAGEMENT)

### 1. Hiển Thị Danh Sách Nhân Viên

#### TC_EMP_LIST_001: Xem danh sách nhân viên
- **Mô tả**: Kiểm tra chức năng hiển thị danh sách nhân viên
- **Điều kiện tiên quyết**: Đăng nhập với tư cách admin
- **Bước thực hiện**:
  - Truy cập trang "Nhân viên"
- **Kết quả mong đợi**:
  - Hiển thị danh sách tất cả nhân viên
  - Hiển thị đúng thông tin: tên, số điện thoại, địa chỉ, vai trò
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Danh sách nhân viên hiển thị đầy đủ trong các thẻ employee-card
  - Thông tin nhân viên (tên, số điện thoại, địa chỉ) hiển thị chính xác
  - Vai trò nhân viên hiển thị rõ ràng với định dạng phù hợp
  - Các nút chức năng (sửa, xóa) hiển thị đúng cho từng nhân viên

#### TC_EMP_LIST_002: Phân trang danh sách nhân viên
- **Mô tả**: Kiểm tra chức năng phân trang khi hiển thị danh sách nhân viên
- **Bước thực hiện**:
  - Truy cập trang danh sách nhân viên
  - Chuyển đến các trang khác nhau
- **Kết quả mong đợi**:
  - Hiển thị đúng số lượng nhân viên trên mỗi trang
  - Nút phân trang hoạt động chính xác
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Nhân viên ở trang 1 và trang 2 khác nhau, không bị trùng lặp
  - URL thay đổi đúng (chứa tham số page=2) khi chuyển trang
  - Nút phân trang hiển thị đúng trạng thái (active, disabled) theo trang hiện tại

### 2. Thêm Nhân Viên

#### TC_EMP_ADD_001: Thêm nhân viên thành công
- **Mô tả**: Kiểm tra chức năng thêm nhân viên mới
- **Bước thực hiện**:
  - Truy cập trang "Thêm nhân viên"
  - Điền đầy đủ thông tin hợp lệ (tên, số điện thoại, địa chỉ, mật khẩu, vai trò)
  - Nhấn nút lưu
- **Kết quả mong đợi**:
  - Nhân viên được thêm vào hệ thống
  - Hiển thị thông báo thành công
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị thông báo thành công sau khi thêm
  - Nhân viên mới xuất hiện trong danh sách với thông tin chính xác
  - Số lượng nhân viên tăng lên sau khi thêm mới

### 3. Chỉnh Sửa Thông Tin Nhân Viên

#### TC_EMP_EDIT_001: Chỉnh sửa thông tin nhân viên thành công
- **Mô tả**: Kiểm tra chức năng chỉnh sửa thông tin nhân viên
- **Bước thực hiện**:
  - Truy cập trang danh sách nhân viên
  - Chọn chỉnh sửa một nhân viên
  - Thay đổi thông tin (tên, số điện thoại)
  - Nhấn nút lưu
- **Kết quả mong đợi**:
  - Thông tin nhân viên được cập nhật
  - Hiển thị thông báo thành công
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Thông tin mới được cập nhật đúng trong cơ sở dữ liệu
  - Hiển thị thông báo cập nhật thành công
  - Tên mới hiển thị trong danh sách nhân viên
  - Tên cũ không còn xuất hiện

### 4. Xóa Nhân Viên

#### TC_EMP_DEL_001: Xóa nhân viên thành công
- **Mô tả**: Kiểm tra chức năng xóa nhân viên
- **Bước thực hiện**:
  - Truy cập trang danh sách nhân viên
  - Chọn xóa một nhân viên
  - Xác nhận xóa
- **Kết quả mong đợi**:
  - Nhân viên bị xóa khỏi hệ thống
  - Hiển thị thông báo thành công
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Hiển thị hộp thoại xác nhận với tên nhân viên đúng
  - Số lượng nhân viên giảm đi sau khi xóa
  - Nhân viên đã xóa không còn hiển thị trong danh sách
  - Hiển thị thông báo xóa thành công

## QUẢN LÝ HÓA ĐƠN (RECEIPT MANAGEMENT)

### 1. Xem Danh Sách Hóa Đơn

#### TC_REC_LIST_001: Xem danh sách hóa đơn
- **Mô tả**: Kiểm tra chức năng hiển thị danh sách hóa đơn
- **Điều kiện tiên quyết**: Đăng nhập với tư cách admin
- **Bước thực hiện**:
  - Truy cập trang "Hóa đơn"
- **Kết quả mong đợi**:
  - Hiển thị danh sách tất cả hóa đơn
  - Hiển thị đúng thông tin: mã hóa đơn, khách hàng, ngày tạo, tổng tiền, trạng thái
  - Hiển thị nút xem chi tiết cho mỗi hóa đơn
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Kiểm tra đúng số lượng và tên các cột
  - Xác nhận hiển thị thông tin và định dạng ngày tháng, tiền tệ phù hợp
  - Kiểm tra hiển thị badge trạng thái
  - Kiểm tra hiển thị nút "Xem" cho mỗi hóa đơn

#### TC_REC_LIST_002: Phân trang danh sách hóa đơn
- **Mô tả**: Kiểm tra chức năng phân trang khi hiển thị danh sách hóa đơn
- **Bước thực hiện**:
  - Truy cập trang danh sách hóa đơn
  - Chuyển đến các trang khác nhau
- **Kết quả mong đợi**:
  - Hiển thị đúng số lượng hóa đơn trên mỗi trang
  - Nút phân trang hoạt động chính xác
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Kiểm tra mã hóa đơn ở trang 1 và trang 2 khác nhau
  - Xác nhận URL thay đổi sau khi chuyển trang (chứa tham số page=2)
  - Xử lý trường hợp không đủ dữ liệu để phân trang

### 2. Xem Chi Tiết Hóa Đơn

#### TC_REC_DETAIL_001: Xem chi tiết hóa đơn
- **Mô tả**: Kiểm tra chức năng hiển thị chi tiết hóa đơn
- **Bước thực hiện**:
  - Truy cập trang danh sách hóa đơn
  - Nhấn vào nút "Xem" của một hóa đơn
- **Kết quả mong đợi**:
  - Hiển thị đầy đủ thông tin hóa đơn: mã, ngày tạo, thông tin khách hàng
  - Hiển thị danh sách các thuốc trong hóa đơn: tên, số lượng, đơn giá, thành tiền
  - Hiển thị tổng tiền hóa đơn
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Xác nhận thông tin hóa đơn (mã hóa đơn, ngày lập, trạng thái) hiển thị đúng
  - Kiểm tra thông tin khách hàng (tên, số điện thoại, địa chỉ) đầy đủ
  - Xác nhận bảng chi tiết hóa đơn hiển thị các sản phẩm và thông tin của chúng
  - Kiểm tra dòng tổng tiền ở cuối bảng chi tiết

#### TC_REC_DETAIL_002: Kiểm tra tính toán giá trị hóa đơn
- **Mô tả**: Kiểm tra tính chính xác của các giá trị tính toán trong hóa đơn
- **Bước thực hiện**:
  - Truy cập chi tiết một hóa đơn
  - Kiểm tra giá trị thành tiền của từng mặt hàng (đơn giá x số lượng)
  - Kiểm tra tổng tiền hóa đơn (tổng thành tiền các mặt hàng)
- **Kết quả mong đợi**:
  - Giá trị thành tiền của từng mặt hàng được tính đúng
  - Tổng tiền hóa đơn được tính đúng
- **Kết quả test**: ✅ Pass
- **Chi tiết**:
  - Tính lại thành tiền từng sản phẩm (đơn giá × số lượng) và so sánh với giá trị hiển thị
  - Tính tổng thành tiền tất cả sản phẩm và so sánh với tổng tiền hiển thị
  - Cho phép sai số 1.0 để tránh lỗi làm tròn số