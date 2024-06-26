package controller;

import dao.CategoryDao;
import model.Category;
import view.QuanLyDanhMucView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import view.TrangChuAdminView;

public class QuanLyDanhMucController {

    private QuanLyDanhMucView view;
    private CategoryDao categoryDao;

    public QuanLyDanhMucController(QuanLyDanhMucView view) {
        this.view = view;
        this.categoryDao = new CategoryDao();
        loadData();
        initEvent();
    }

    private void initEvent() {
        view.getBtnThemDM().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themDanhMuc();
            }
        });

        view.getBtnSuaDM().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaDanhMuc();
            }
        });

        view.getBtnXoaDM().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaDanhMuc();
            }
        });

        view.getTblDanhMuc().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && view.getTblDanhMuc().getSelectedRow() != -1) {
                    hienThiChiTietDanhMuc();
                }
            }
        });
        
        view.getBtnTroVe().addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    TrangChuAdminView view = new TrangChuAdminView();
                    view.setLocationRelativeTo(null);
                    view.setVisible(true);
                    disposeCurrentView();
                }
        });
    }

    private void loadData() {
        List<Category> categories = categoryDao.getAllCategories();
        DefaultTableModel model = (DefaultTableModel) view.getTblDanhMuc().getModel();
        model.setRowCount(0); // Clear existing data

        for (Category category : categories) {
            model.addRow(new Object[]{category.getCategoryID(), category.getCategoryName()});
        }
    }

    private void hienThiChiTietDanhMuc() {
        int selectedRow = view.getTblDanhMuc().getSelectedRow();
        if (selectedRow != -1) {
            int maDM = (int) view.getTblDanhMuc().getValueAt(selectedRow, 0);
            String tenDM = (String) view.getTblDanhMuc().getValueAt(selectedRow, 1);

            view.getTxtMaDM().setText(String.valueOf(maDM));
            view.getTxtTenDM().setText(tenDM);
        }
    }

    private void themDanhMuc() {
        String tenDM = view.getTxtTenDM().getText();
        if (tenDM.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên danh mục không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Category category = new Category(tenDM);
        boolean success = categoryDao.addCategory(category);

        if (success) {
            JOptionPane.showMessageDialog(view, "Thêm danh mục thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // Reload data
            view.getTxtTenDM().setText(""); // Clear input field
        } else {
            JOptionPane.showMessageDialog(view, "Thêm danh mục thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaDanhMuc() {
        int selectedRow = view.getTblDanhMuc().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn danh mục cần sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maDM = Integer.parseInt(view.getTxtMaDM().getText());
        String tenDM = view.getTxtTenDM().getText();
        if (tenDM.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên danh mục không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Category category = new Category(maDM, tenDM);
        boolean success = categoryDao.updateCategory(category);

        if (success) {
            JOptionPane.showMessageDialog(view, "Sửa danh mục thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // Reload data
            view.getTxtMaDM().setText(""); // Clear input fields
            view.getTxtTenDM().setText("");
        } else {
            JOptionPane.showMessageDialog(view, "Sửa danh mục thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaDanhMuc() {
        int selectedRow = view.getTblDanhMuc().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn danh mục cần xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maDM = Integer.parseInt(view.getTxtMaDM().getText());
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa danh mục này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = categoryDao.deleteCategory(maDM);

            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa danh mục thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadData(); // Reload data
                view.getTxtMaDM().setText(""); // Clear input fields
                view.getTxtTenDM().setText("");
            } else {
                JOptionPane.showMessageDialog(view, "Xóa danh mục thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void disposeCurrentView() {
        if (this.view != null) {
            this.view.dispose();
        }
    }
}
