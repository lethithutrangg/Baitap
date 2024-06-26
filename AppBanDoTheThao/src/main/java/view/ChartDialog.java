package view;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDialog extends JFrame {
    public ChartDialog(DefaultCategoryDataset dataset) {
        // Tạo biểu đồ từ dataset
        JFreeChart chart = ChartFactory.createBarChart(
            "Biểu đồ số lượng sản phẩm bán được", // Tiêu đề biểu đồ
            "Sản phẩm",                          // Tên trục x
            "Số lượng",                          // Tên trục y
            dataset                               // Dữ liệu
        );

        // Tạo panel để chứa biểu đồ
        JPanel chartPanel = new ChartPanel(chart);

        // Đặt panel vào cửa sổ hộp thoại
        getContentPane().add(chartPanel);
        
        // Cài đặt kích thước cửa sổ
        setSize(800, 600);
        
        // Hiển thị cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);
    }
}

