package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.dao.custom.impl.util.SQLUtil;
import lk.ijse.pos.entity.Orders;

import java.sql.*;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public ArrayList<Orders> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Orders orders) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO orders (orderId,customerID,date) VALUES (?,?,?)", orders.getOrderId(), orders.getCustomerId(), Date.valueOf(orders.getDate()));
    }

    @Override
    public boolean update(Orders orders) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT orderId FROM orders WHERE orderId=?";
        ResultSet resultSet = SQLUtil.execute(sql, orderId);
        return resultSet.next();
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";
        ResultSet resultSet = SQLUtil.execute(sql);
        return resultSet.next() ? String.format("OID-%03d", (Integer.parseInt(resultSet.getString("orderId").replace("OID-", "")) + 1)) : "OID-001";
    }

    @Override
    public Orders search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM orders WHERE orderId=?", id);
        if (rst.next()) {
            return new Orders(rst.getString(1), rst.getString(2), rst.getDate(3).toLocalDate());
        }
        return null;
    }
}
