package dao;

import database.SQLServerConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Phương thức để đăng ký người dùng mới và thêm vào CSDL
    public boolean registerUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để chèn dữ liệu người dùng mới vào CSDL
                String query = "INSERT INTO [User] (Email, Password, Fullname, Phone, Gender, Age, Address, RoleID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                statement = connection.prepareStatement(query);
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getFullname());
                statement.setString(4, user.getPhone());
                statement.setString(5, user.getGender());
                statement.setInt(6, user.getAge());
                statement.setString(7, user.getAddress());
                statement.setInt(8, user.getRoleID());

                // Thực hiện câu truy vấn
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Đăng ký thành công!");
                    success = true;
                } else {
                    System.out.println("Đăng ký thất bại!");
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
    
    public User loginUser(String email, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để kiểm tra thông tin đăng nhập của người dùng
                String query = "SELECT * FROM [User] WHERE Email = ? AND Password = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Nếu tồn tại người dùng có thông tin đăng nhập như vậy, tạo đối tượng User từ dữ liệu trong ResultSet
                    user = new User();
                    user.setUserID(resultSet.getInt("UserID"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setFullname(resultSet.getString("Fullname"));
                    user.setPhone(resultSet.getString("Phone"));
                    user.setGender(resultSet.getString("Gender"));
                    user.setAge(resultSet.getInt("Age"));
                    user.setAddress(resultSet.getString("Address"));
                    user.setRoleID(resultSet.getInt("RoleID"));
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
        return user;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = SQLServerConnection.getConnection();
            if (connection != null) {
                // Câu truy vấn SQL để lấy toàn bộ danh sách người dùng từ CSDL
                String query = "SELECT * FROM [User]";
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();

                // Duyệt qua các dòng kết quả và tạo danh sách người dùng
                while (resultSet.next()) {
                    User user = new User();
                    user.setUserID(resultSet.getInt("UserID"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setFullname(resultSet.getString("Fullname"));
                    user.setPhone(resultSet.getString("Phone"));
                    user.setGender(resultSet.getString("Gender"));
                    user.setAge(resultSet.getInt("Age"));
                    user.setAddress(resultSet.getString("Address"));
                    user.setRoleID(resultSet.getInt("RoleID"));
                    users.add(user);
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
        return users;
    }
}
