package dao;

import database.SQLServerConnection;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    // Thêm một danh mục mới vào CSDL
    public boolean addCategory(Category category) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để chèn dữ liệu danh mục mới vào CSDL
                String query = "INSERT INTO Category (CategoryName) VALUES (?)";

                statement = connection.prepareStatement(query);
                statement.setString(1, category.getCategoryName());

                // Thực hiện câu truy vấn
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Thêm danh mục thành công!");
                    success = true;
                } else {
                    System.out.println("Thêm danh mục thất bại!");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Sửa thông tin của một danh mục trong CSDL
    public boolean updateCategory(Category category) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để cập nhật thông tin của danh mục trong CSDL
                String query = "UPDATE Category SET CategoryName = ? WHERE CategoryID = ?";

                statement = connection.prepareStatement(query);
                statement.setString(1, category.getCategoryName());
                statement.setInt(2, category.getCategoryID());

                // Thực hiện câu truy vấn
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật danh mục thành công!");
                    success = true;
                } else {
                    System.out.println("Cập nhật danh mục thất bại!");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Xóa một danh mục khỏi CSDL dựa trên ID
    public boolean deleteCategory(int categoryId) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để xóa danh mục khỏi CSDL
                String query = "DELETE FROM Category WHERE CategoryID = ?";

                statement = connection.prepareStatement(query);
                statement.setInt(1, categoryId);

                // Thực hiện câu truy vấn
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Xóa danh mục thành công!");
                    success = true;
                } else {
                    System.out.println("Xóa danh mục thất bại!");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Lấy danh sách các danh mục từ CSDL
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để lấy danh sách các danh mục từ CSDL
                String query = "SELECT * FROM Category";

                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();

                // Duyệt qua các dòng kết quả và tạo danh sách danh mục
                while (resultSet.next()) {
                    Category category = new Category();
                    category.setCategoryID(resultSet.getInt("CategoryID"));
                    category.setCategoryName(resultSet.getString("CategoryName"));
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và tuyên bố
            SQLServerConnection.closeConnection();
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }
}
