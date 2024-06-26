package controller;

import javax.swing.JOptionPane;
import view.DangNhapView;
import view.QuanLyDanhMucView;
import view.QuanLyNguoiDungView;
import view.QuanLySanPhamView;
import view.ThongKeView;
import view.TrangChuAdminView;

public class TrangChuAdminController {
    
    private TrangChuAdminView view;
    
    public TrangChuAdminController (TrangChuAdminView view) {
        this.view = view;
        
        initEvent();
    }

    private void initEvent() {
        this.view.getBtnQLDanhMuc().addActionListener(e -> moManHinhQuanLyDanhMuc());
        this.view.getBtnQLSanPham().addActionListener(e -> moManHinhQuanLySanPham());
        this.view.getBtnQLNguoiDung().addActionListener(e -> moManHinhQuanLyNguoiDung());
        this.view.getBtnThongKe().addActionListener(e -> moManHinhThongKe());
        this.view.getBtnDangXuat().addActionListener(e -> xuLyDangXuat());
    }

    private void moManHinhQuanLyDanhMuc() {
        QuanLyDanhMucView view = new QuanLyDanhMucView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        disposeCurrentView();
    }

    private void moManHinhQuanLySanPham() {
        QuanLySanPhamView view = new QuanLySanPhamView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        disposeCurrentView();
    }

    private void moManHinhQuanLyNguoiDung() {
        QuanLyNguoiDungView view = new QuanLyNguoiDungView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        disposeCurrentView();
    }

    private void moManHinhThongKe() {
        ThongKeView view = new ThongKeView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        disposeCurrentView();
    }

    private void xuLyDangXuat() {
        int option = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            DangNhapView loginView = new DangNhapView();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
            view.dispose();
        }
    }

    private void disposeCurrentView() {
        if (this.view != null) {
            this.view.dispose();
        }
    }
}
