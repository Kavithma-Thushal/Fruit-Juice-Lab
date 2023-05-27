package dao;

import db.DBConnection;
import model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface OrderDetailsDAO {

    int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException;
}
