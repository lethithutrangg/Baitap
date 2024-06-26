package controller;

import dao.UserDao;
import javax.swing.JOptionPane;
import model.User;
import view.DangKyView;
import view.DangNhapView;


public class DangKyController {
    private DangKyView view;
    private UserDao userDao;
    
    public DangKyController(DangKyView view) {
        this.view = view;
        userDao = new UserDao();
        initEvent();
    }

    private void initEvent() {
        
        view.getBtnDangKy().addActionListener(e -> xuLyDangKy());
        
        view.getBtnDangNhap().addActionListener(e -> moManHinhDangNhap());
    }

    private void moManHinhDangNhap() {
        DangNhapView loginView = new DangNhapView();
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
        view.dispose();
    }

    private void xuLyDangKy() {
        // Lấy thông tin từ các trường nhập liệu
        String email = view.getTxtEmail().getText();
        String password = view.getTxtMatKhau().getText();
        String fullName = view.getTxtHoTen().getText();
        String phone = view.getTxtDienThoai().getText();
        String gender = view.getTxtGioiTinh().getText();
        int age;
        try {
            age = Integer.parseInt(view.getTxtTuoi().getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Sai định dạng tuổi. vd: 2003", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String address = view.getTxtDiaChi().getText();

        // Kiểm tra xem các trường nhập liệu có được điền đầy đủ không
        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty() || gender.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng điền đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo đối tượng User từ thông tin nhập liệu
        User newUser = new User(email, password, fullName, phone, gender, age, address, 2); // Giả sử roleID của người dùng mới là 2

        // Gọi phương thức đăng ký người dùng từ service và kiểm tra kết quả
        if (userDao.registerUser(newUser)) {
            JOptionPane.showMessageDialog(view, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            DangNhapView loginView = new DangNhapView();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
            // Đóng màn hình đăng ký
            view.dispose(); // Đóng frame hiện tại
        } else {
            JOptionPane.showMessageDialog(view, "Đăng ký thất bại. Vui lòng thử lại sau.", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
