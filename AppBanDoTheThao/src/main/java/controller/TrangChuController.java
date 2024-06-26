package controller;

import dao.CategoryDao;
import dao.InvoiceDao;
import dao.ProductDao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.BillDetail;
import model.Category;
import model.Invoice;
import model.Product;
import view.DangNhapView;
import view.TrangChuView;

public class TrangChuController {
    
    private TrangChuView view;
    private ProductDao productDao;
    private JPanel productListPanel;
    private List<BillDetail> billDetails;
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private InvoiceDao invoiceDao;
    
    public TrangChuController(TrangChuView view) {
        this.view = view;
        
        productDao = new ProductDao();
        invoiceDao = new InvoiceDao();
        billDetails = new ArrayList<>();
        
        taoPanleChuaDSSanPham();
        
        taoPanelGioHang();
        
        loadData();
        initEvent();
    }
    
    private void taoPanleChuaDSSanPham() {
        productListPanel = new JPanel();
        JScrollPane scrollPaneProductList = new JScrollPane(productListPanel);
        view.getPnDSSanPham().setLayout(new BorderLayout());
        view.getPnDSSanPham().setPreferredSize(new Dimension(600, 600));
        view.getPnDSSanPham().add(scrollPaneProductList, BorderLayout.CENTER);
    }

    private void loadData() {
        loadDanhMuc();
    }

    private void initEvent() {
        view.getBtnGioHang().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCartDialog();
            }
        });
        
        view.getBtnDangXuat().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    handleLogout();
            }
        });
    }

    private void loadDanhMuc() {
        List<JButton> categoryButtons = new ArrayList<>();
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllCategories();
        for (Category category : categories) {
            JButton categoryButton = new JButton(category.getCategoryName()); // Tạo nút danh mục
            categoryButton.putClientProperty("category", category); // Lưu thông tin danh mục vào nút
            categoryButtons.add(categoryButton); // Thêm nút vào danh sách
        }
        
        
        
        view.getPnDanhMuc().setLayout(new FlowLayout());
        for (JButton button : categoryButtons) {
            view.getPnDanhMuc().add(button);
        }
        
        // Thêm action listener cho nút danh mục
        for (JButton button : categoryButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Xử lý sự kiện khi nút danh mục được nhấn
                    Category selectedCategory = (Category) button.getClientProperty("category");
                    if (selectedCategory != null) {
                        loadCosmeticsByCategory(selectedCategory.getCategoryID());
                    }
                }
            });
        }
    }

    private void loadCosmeticsByCategory(int categoryId) {
        productListPanel.removeAll();
        List<Product> products = productDao.getProductsByCategoryId(categoryId);
     // Thiết lập layout cho productListPanel
        GridBagLayout gridBagLayout = new GridBagLayout();
        productListPanel.setLayout(gridBagLayout);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(20, 20, 20, 20);
        for (Product product : products) {
            JPanel productPanel = createProductPanel(product);
            productListPanel.add(productPanel, constraints);
            constraints.gridx++; // Di chuyển sang cột tiếp theo
            if (constraints.gridx == 3) { // Nếu đã đến cột thứ 3, chuyển sang hàng mới
                constraints.gridx = 0;
                constraints.gridy++;
            }
        }
        view.revalidate();
        view.repaint();
    }

    private JPanel createProductPanel(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(250, 300));

        JLabel imageLabel = new JLabel();
        String base64Image = product.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(base64Image);
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
                if (bufferedImage != null) {
                    ImageIcon imageIcon = new ImageIcon(bufferedImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(imageIcon);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout());
        imagePanel.add(imageLabel);
        panel.add(imagePanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel("Tên: " + product.getProductName()));
        detailsPanel.add(new JLabel("Kích thước: " + product.getSize()));
        detailsPanel.add(new JLabel("Màu: " + product.getColor()));
        detailsPanel.add(new JLabel("Giá: " + product.getPrice()));
        panel.add(detailsPanel, BorderLayout.CENTER);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedQuantity = showQuantityDialog();
                if (selectedQuantity > 0) {
                    BillDetail billDetail = new BillDetail(product.getProductID(), selectedQuantity, product.getPrice());
                    billDetails.add(billDetail);
                    updateCartTable();
                    updateTotalPrice();
                }
            }
        });
        panel.add(addToCartButton, BorderLayout.SOUTH);

        return panel;
    }
    
    private int showQuantityDialog() {
        JTextField quantityField = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Số lượng:"));
        myPanel.add(quantityField);
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Chọn số lượng", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                return Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                return -1; // Invalid quantity
            }
        }
        return -1; // Cancelled
    }
    
    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        for (BillDetail billDetail : billDetails) {
            Object[] row = {billDetail.getProductID(), billDetail.getQuantity(), billDetail.getPrice(), billDetail.getQuantity() * billDetail.getPrice()}; // Calculate total price
            cartTableModel.addRow(row);
        }
    }

    private void taoPanelGioHang() {
        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());

        // Cart table
        cartTableModel = new DefaultTableModel();
        cartTableModel.addColumn("Mã sản phẩm");
        cartTableModel.addColumn("Số lượng");
        cartTableModel.addColumn("Giá");
        cartTableModel.addColumn("Thành tiền"); // New column for total price of each item
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        // Buy button
        JButton buyButton = new JButton("Đặt hàng"); // Change button text
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder(); // Renamed method
            }
        });
        cartPanel.add(buyButton, BorderLayout.SOUTH);
    }
    
    private void placeOrder() { // Renamed method
    	if(DangNhapController.getLoggedInUser() == null) {
    		JOptionPane.showMessageDialog(view, "Bạn cần đăng nhập để đặt hàng!");
    		return;
    	}
        if (billDetails.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng trống!");
            return;
        }

        Date currentDate = new Date();
        int userID = DangNhapController.getLoggedInUser().getUserID();
        float totalPrice = 0;
        for (BillDetail billDetail : billDetails) {
            totalPrice += billDetail.getPrice();
        }
        Invoice invoice = new Invoice(userID, currentDate, totalPrice);
        boolean success = invoiceDao.saveInvoice(invoice, billDetails);
        if (success) {
            JOptionPane.showMessageDialog(view, "Đặt hàng thành công!");
            billDetails.clear();
            updateCartTable();
            updateTotalPrice();
        } else {
            JOptionPane.showMessageDialog(view, "Đặt hàng thất bại! Vui lòng thử lại sau.");
        }
    }
    
    private void updateTotalPrice() {
        float totalPrice = 0;
        for (BillDetail billDetail : billDetails) {
            totalPrice += billDetail.getPrice();
        }
        // Update total price label or text field in your UI
    }
    
    private void showCartDialog() {
        // Create a dialog to display the cart
        JDialog cartDialog = new JDialog(view, "Giỏ hàng", true);
        cartDialog.setSize(400, 300);
        cartDialog.setLocationRelativeTo(view);
        
        // Cart table for the dialog
        JTable cartDialogTable = new JTable(cartTableModel);
        JScrollPane cartDialogScrollPane = new JScrollPane(cartDialogTable);
        cartDialog.add(cartDialogScrollPane, BorderLayout.CENTER);
        
        // Order button
        JButton orderButton = new JButton("Đặt hàng");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder(); // Call method to place order
                cartDialog.dispose(); // Close dialog after placing order
            }
        });
        
        // Close button
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartDialog.dispose();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(orderButton);
        buttonPanel.add(closeButton);
        cartDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        cartDialog.setVisible(true);
    }
    
    private void handleLogout() {
    	int option = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            DangNhapView loginView = new DangNhapView();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
            view.dispose();
        }
    }
}
