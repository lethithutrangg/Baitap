package controller;

import dao.InvoiceDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.BillDetail;
import model.Invoice;
import org.jfree.data.category.DefaultCategoryDataset;
import view.ChartDialog;
import view.ThongKeView;
import view.TrangChuAdminView;

public class ThongKeController {
    private ThongKeView view;
    private InvoiceDao invoiceDao;
    
    public ThongKeController(ThongKeView view) {
        this.view = view;
        invoiceDao = new InvoiceDao();
        
        view.getTxtStartDate().setText("20-05-2024");
        view.getTxtEndDate().setText("20-05-2024");
        
        initEvent();
    } 

    private void initEvent() {
        view.getBtnXemTatCa().addActionListener(e -> xemTatCaHoaDon());
        
        view.getBtnTimHoaDon().addActionListener(e -> timHoaDonTheoNgay());
        
        view.getBtnBieuDo().addActionListener(e -> moBieuDoThongKe());
        
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

    private void xemTatCaHoaDon() {
        // Gọi hàm lấy tất cả hóa đơn từ service
        List<Invoice> invoices = invoiceDao.getAllInvoices();

        // Cập nhật bảng JTable với danh sách hóa đơn mới
        updateInvoiceTable(createTableModelFromInvoices(invoices));

        // Tính tổng doanh thu của các hóa đơn và cập nhật label tương ứng
        float totalRevenue = calculateTotalRevenue(invoices);
        updateTotalRevenue(totalRevenue);
    }
    
    private DefaultTableModel createTableModelFromInvoices(List<Invoice> invoices) {
        // Tạo một đối tượng DefaultTableModel với các cột tương ứng với thuộc tính của hóa đơn
        String[] columnNames = {"Mã hóa đơn", "Mã người dùng", "Ngày tạo", "Tổng giá"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        // Thêm dữ liệu từ danh sách hóa đơn vào model
        for (Invoice invoice : invoices) {
            Object[] rowData = {invoice.getInvoiceID(), invoice.getUserID(), invoice.getCreatedDate(), invoice.getTotalPrice()};
            model.addRow(rowData);
        }
        
        return model;
    }

    private void updateInvoiceTable(DefaultTableModel model) {
        view.getTblThongKe().setModel(model);
    }
    
    private float calculateTotalRevenue(List<Invoice> invoices) {
        float totalRevenue = 0;
        for (Invoice invoice : invoices) {
            totalRevenue += invoice.getTotalPrice();
        }
        return totalRevenue;
    }

    private void updateTotalRevenue(float totalRevenue) {
        view.getLblDoanhThu().setText( totalRevenue + "");
    }
    
    private void disposeCurrentView() {
        if (this.view != null) {
            this.view.dispose();
        }
    }

    private void timHoaDonTheoNgay() {
        // Kiểm tra xem đã chọn ngày chưa
        String startDateStr = view.getTxtStartDate().getText();
        String endDateStr = view.getTxtEndDate().getText();
        
        if (startDateStr.equals("") || endDateStr.equals("")) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngày trước khi tìm kiếm hóa đơn.");
            return;
        }
        
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
        
        Date startDate;
        Date endDate;
        
        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);

            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Cần nhập đúng định dạng ngày: dd-MM-yyyy");
            return;
        }
        
        // Gọi hàm tìm kiếm hóa đơn theo ngày từ service

        List<Invoice> invoices = invoiceDao.getAllInvoicesByDate(startDate, endDate);

        // Cập nhật bảng JTable với danh sách hóa đơn mới
        updateInvoiceTable(createTableModelFromInvoices(invoices));

        // Tính tổng doanh thu của các hóa đơn và cập nhật label tương ứng
        float totalRevenue = calculateTotalRevenue(invoices);
        updateTotalRevenue(totalRevenue);
    }

    private void moBieuDoThongKe() {
        // Kiểm tra xem đã chọn ngày chưa
        String startDateStr = view.getTxtStartDate().getText();
        String endDateStr = view.getTxtEndDate().getText();
        
        if (startDateStr.equals("") || endDateStr.equals("")) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngày trước khi tìm kiếm hóa đơn.");
            return;
        }
        
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
        
        Date startDate;
        Date endDate;
        
        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);

            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Cần nhập đúng định dạng ngày: dd-MM-yyyy");
            return;
        }
        

        // Gọi phương thức để lấy dữ liệu số lượng sản phẩm bán được trong khoảng thời gian đã chọn
        List<BillDetail> billDetails = invoiceDao.getAllBillDetailsByDate(startDate, endDate);

        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = createDataset(billDetails);

        // Tạo hộp thoại biểu đồ và hiển thị
        ChartDialog chartDialog = new ChartDialog(dataset);
        chartDialog.setVisible(true);
    }
    
    private DefaultCategoryDataset createDataset(List<BillDetail> billDetails) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Thêm dữ liệu vào dataset
        for (BillDetail billDetail : billDetails) {
            dataset.addValue(billDetail.getQuantity(), "Sản phẩm", billDetail.getProductID()+"");
        }

        return dataset;
    }
}
