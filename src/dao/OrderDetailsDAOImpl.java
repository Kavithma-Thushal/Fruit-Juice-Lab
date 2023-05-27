package dao;

import db.DBConnection;
import model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDetailsDAOImpl {

    public int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO OrderDetails (orderId, itemCode, qty, unitPrice) VALUES (?,?,?,?)");
        preparedStatement.setString(1, orderId);
        preparedStatement.setString(2, detail.getItemCode());
        preparedStatement.setInt(3, detail.getQty());
        preparedStatement.setBigDecimal(4, detail.getUnitPrice());
        return preparedStatement.executeUpdate();
    }
}
