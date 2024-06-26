package dao;

import database.SQLServerConnection;
import model.Invoice;
import model.BillDetail;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao {

    // Phương thức để lưu hóa đơn và chi tiết hóa đơn vào CSDL
	public boolean saveInvoice(Invoice invoice, List<BillDetail> billDetails) {
	    Connection connection = null;
	    PreparedStatement invoiceStatement = null;
	    PreparedStatement billDetailStatement = null;
	    boolean success = false;

	    try {
	        connection = SQLServerConnection.getConnection();
	        if (connection != null) {
	            // Bắt đầu transaction
	            connection.setAutoCommit(false);

	            // Thêm hóa đơn vào CSDL
	            String insertInvoiceQuery = "INSERT INTO Invoice (UserID, CreatedDate, TotalPrice) VALUES (?, ?, ?)";
	            invoiceStatement = connection.prepareStatement(insertInvoiceQuery, PreparedStatement.RETURN_GENERATED_KEYS);
	            invoiceStatement.setInt(1, invoice.getUserID());
	            invoiceStatement.setDate(2, new java.sql.Date(invoice.getCreatedDate().getTime())); // Chuyển đổi từ java.util.Date sang java.sql.Date
	            invoiceStatement.setFloat(3, invoice.getTotalPrice());

	            int rowsInserted = invoiceStatement.executeUpdate();
	            if (rowsInserted > 0) {
	                // Lấy ID của hóa đơn mới được thêm vào
	                ResultSet generatedKeys = invoiceStatement.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int invoiceID = generatedKeys.getInt(1);

	                    // Thêm các chi tiết hóa đơn vào CSDL
	                    String insertBillDetailQuery = "INSERT INTO BillDetail (InvoiceID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
	                    billDetailStatement = connection.prepareStatement(insertBillDetailQuery);
	                    for (BillDetail billDetail : billDetails) {
	                        billDetailStatement.setInt(1, invoiceID);
	                        billDetailStatement.setInt(2, billDetail.getProductID());
	                        billDetailStatement.setInt(3, billDetail.getQuantity());
	                        billDetailStatement.setFloat(4, billDetail.getPrice());
	                        billDetailStatement.addBatch();
	                    }

	                    // Thực hiện thêm các chi tiết hóa đơn
	                    int[] detailRowsInserted = billDetailStatement.executeBatch();

	                    // Kiểm tra xem tất cả các chi tiết hóa đơn đã được thêm thành công hay không
	                    for (int rows : detailRowsInserted) {
	                        if (rows <= 0) {
	                            // Nếu có ít nhất một chi tiết hóa đơn không được thêm thành công, rollback transaction và thoát
	                            connection.rollback();
	                            return false;
	                        }
	                    }

	                    // Nếu tất cả các chi tiết hóa đơn đều được thêm thành công, commit transaction
	                    connection.commit();
	                    success = true;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            if (connection != null) {
	                connection.rollback(); // Rollback transaction nếu có lỗi xảy ra
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    } finally {
	        // Đặt lại auto-commit sau khi kết thúc transaction
	        try {
	            if (connection != null) {
	                connection.setAutoCommit(true);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Đóng kết nối và tuyên bố
	        SQLServerConnection.closeConnection();
	        try {
	            if (invoiceStatement != null) {
	                invoiceStatement.close();
	            }
	            if (billDetailStatement != null) {
	                billDetailStatement.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return success;
	}
	
	public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM Invoice";
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int invoiceID = resultSet.getInt("InvoiceID");
                    int userID = resultSet.getInt("UserID");
                    java.util.Date createdDate = resultSet.getDate("CreatedDate");
                    float totalPrice = resultSet.getFloat("TotalPrice");

                    // Tạo đối tượng Invoice từ dữ liệu truy vấn và thêm vào danh sách
                    Invoice invoice = new Invoice(invoiceID, userID, createdDate, totalPrice);
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và tuyên bố
            SQLServerConnection.closeConnection();
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return invoices;
    }
	
	public List<Invoice> getAllInvoicesByDate(Date fromDate, Date toDate) {
	    List<Invoice> invoices = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = SQLServerConnection.getConnection();
	        if (connection != null) {
	            String query = "SELECT * FROM Invoice WHERE CreatedDate BETWEEN ? AND ?";
	            statement = connection.prepareStatement(query);
	            // Set the parameters for the query
	            statement.setDate(1, new java.sql.Date(fromDate.getTime()));
	            statement.setDate(2, new java.sql.Date(toDate.getTime()));
	            resultSet = statement.executeQuery();
	            while (resultSet.next()) {
	                int invoiceID = resultSet.getInt("InvoiceID");
	                int userID = resultSet.getInt("UserID");
	                java.util.Date createdDate = resultSet.getDate("CreatedDate");
	                float totalPrice = resultSet.getFloat("TotalPrice");

	                // Tạo đối tượng Invoice từ dữ liệu truy vấn và thêm vào danh sách
	                Invoice invoice = new Invoice(invoiceID, userID, createdDate, totalPrice);
	                invoices.add(invoice);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Đóng kết nối và tuyên bố
	        SQLServerConnection.closeConnection();
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            if (resultSet != null) {
	                resultSet.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return invoices;
	}
	
	public List<BillDetail> getAllBillDetailsByDate(Date fromDate, Date toDate) {
	    List<BillDetail> billDetails = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = SQLServerConnection.getConnection();
	        if (connection != null) {
	            String query = "SELECT bd.ProductID, bd.Quantity, bd.Price " +
	                           "FROM Invoice i " +
	                           "INNER JOIN BillDetail bd ON i.InvoiceID = bd.InvoiceID " +
	                           "WHERE i.CreatedDate BETWEEN ? AND ?";
	            statement = connection.prepareStatement(query);
	            // Set the parameters for the query
	            statement.setDate(1, new java.sql.Date(fromDate.getTime()));
	            statement.setDate(2, new java.sql.Date(toDate.getTime()));
	            resultSet = statement.executeQuery();
	            while (resultSet.next()) {
	                int productID = resultSet.getInt("ProductID");
	                int quantity = resultSet.getInt("Quantity");
	                float price = resultSet.getFloat("Price");

	                // Tạo đối tượng BillDetail từ dữ liệu truy vấn và thêm vào danh sách
	                BillDetail billDetail = new BillDetail(productID, quantity, price);
	                billDetails.add(billDetail);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Đóng kết nối và tuyên bố
	        SQLServerConnection.closeConnection();
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            if (resultSet != null) {
	                resultSet.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return billDetails;
	}


}
