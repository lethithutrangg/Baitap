package controller;

import view.QuanLySanPhamView;
import dao.ProductDao;
import dao.CategoryDao;
import model.Category;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.TrangChuAdminView;

public class QuanLySanPhamController {

    private QuanLySanPhamView view;
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private List<Category> categories;

    public QuanLySanPhamController(QuanLySanPhamView view) {
        this.view = view;
        this.productDao = new ProductDao();
        this.categoryDao = new CategoryDao();

        loadData();
        initEvent();
    }

    private void initEvent() {
        view.getBtnThemSP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCosmetic();
            }
        });

        view.getBtnSuaSP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        view.getBtnXoaSP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCosmetic();
            }
        });

        view.getBtnChonAnh().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });
        
        view.getTblSanPham().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && view.getTblSanPham().getSelectedRow() != -1) {
                    int selectedRow = view.getTblSanPham().getSelectedRow();
                    int productId = (int) view.getTblSanPham().getValueAt(selectedRow, 0);
                    loadProductDetails(productId);
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
        // Load danh sách danh mục vào combobox
        categories = categoryDao.getAllCategories();
        view.getCbbDanhMuc().removeAllItems();
        for (Category category : categories) {
            view.getCbbDanhMuc().addItem(category);
        }

        // Load danh sách sản phẩm vào bảng
        List<Product> products = productDao.getAllProducts();
        DefaultTableModel model = (DefaultTableModel) view.getTblSanPham().getModel();
        model.setRowCount(0);
        for (Product product : products) {
            model.addRow(new Object[]{
                product.getProductID(),
                product.getProductName(),
                product.getDescription(),
                product.getCategoryID(),
                product.getSize(),
                product.getColor(),
                product.getPrice(),
                product.getQuantity()
            });
        }
    }

    private void addCosmetic() {
        try {
            String name = view.getTxtTenSP().getText();
            Category category = (Category) view.getCbbDanhMuc().getSelectedItem();
            String description = view.getTxtMoTa().getText();
            String size = view.getTxtKichThuoc().getText();
            String color = view.getTxtMauSac().getText();
            float price = Float.parseFloat(view.getTxtGia().getText());
            int quantity = Integer.parseInt(view.getTxtSoLuong().getText());
            String image = view.getLblLuuAnh().getText(); // Assuming the image is stored as Base64 string in lblLuuAnh

            Product product = new Product(name, category.getCategoryID(), description, size, color, price, quantity, image);
            if (productDao.addProduct(product)) {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Có lỗi xảy ra. Vui lòng kiểm tra lại thông tin!");
        }
    }

    private void updateProduct() {
        int selectedRow = view.getTblSanPham().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn Sản phẩm cần sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(view.getTxtMaSP().getText());
            String name = view.getTxtTenSP().getText();
            Category category = (Category) view.getCbbDanhMuc().getSelectedItem();
            String description = view.getTxtMoTa().getText();
            String size = view.getTxtKichThuoc().getText();
            String color = view.getTxtMauSac().getText();
            float price = Float.parseFloat(view.getTxtGia().getText());
            int quantity = Integer.parseInt(view.getTxtSoLuong().getText());
            String image = view.getLblLuuAnh().getText(); // Assuming the image is stored as Base64 string in lblLuuAnh

            Product product = new Product(name, category.getCategoryID(), description, size, color, price, quantity, image);
            if (productDao.updateProduct(product)) {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Có lỗi xảy ra. Vui lòng kiểm tra lại thông tin!");
        }
    }

    private void deleteCosmetic() {
        int selectedRow = view.getTblSanPham().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn Sản phẩm cần xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(view.getTxtMaSP().getText());
            if (productDao.deleteProduct(id)) {
                JOptionPane.showMessageDialog(view, "Xóa sản phẩm thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Có lỗi xảy ra. Vui lòng kiểm tra lại thông tin!");
        }
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                view.getLblLuuAnh().setText(imageBase64);
                ImageIcon icon = new ImageIcon(image.getScaledInstance(view.getLblLuuAnh().getWidth(), view.getLblLuuAnh().getHeight(), Image.SCALE_SMOOTH));
                view.getLblLuuAnh().setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Có lỗi xảy ra khi chọn ảnh!");
            }
        }
    }
    
    private void loadProductDetails(int productId) {
        Product product = productDao.getProductById(productId);
        if (product != null) {
            view.getTxtMaSP().setText(String.valueOf(product.getProductID()));
            view.getTxtTenSP().setText(product.getProductName());
            for (int i = 0; i < view.getCbbDanhMuc().getItemCount(); i++) {
                if (view.getCbbDanhMuc().getItemAt(i).getCategoryID() == product.getCategoryID()) {
                    view.getCbbDanhMuc().setSelectedIndex(i);
                    break;
                }
            }
            view.getTxtTenSP().setText(product.getProductName());
            view.getTxtMoTa().setText(product.getDescription());
            view.getTxtMauSac().setText(product.getColor());
            view.getTxtKichThuoc().setText(product.getSize());
            view.getTxtGia().setText(String.valueOf(product.getPrice()));
            view.getTxtSoLuong().setText(String.valueOf(product.getQuantity()));

            if (product.getImage() != null && !product.getImage().isEmpty()) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(product.getImage());
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(view.getLblLuuAnh().getWidth(), view.getLblLuuAnh().getHeight(), Image.SCALE_SMOOTH));
                    view.getLblLuuAnh().setIcon(icon);
                    view.getLblLuuAnh().setText(product.getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                view.getLblLuuAnh().setIcon(null);
                view.getLblLuuAnh().setText("");
            }
        }
    }
    
    private void disposeCurrentView() {
        if (this.view != null) {
            this.view.dispose();
        }
    }
}
