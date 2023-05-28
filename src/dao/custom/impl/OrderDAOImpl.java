package dao.custom.impl;

import dao.custom.OrderDAO;
import dao.custom.impl.util.SQLUtil;
import model.OrderDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public ArrayList<OrderDTO> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT orderId FROM orders WHERE orderId=?";
        ResultSet resultSet=SQLUtil.execute(sql, orderId);
        return resultSet.next();
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";
        ResultSet resultSet = SQLUtil.execute(sql);
        return resultSet.next() ? String.format("OID-%03d", (Integer.parseInt(resultSet.getString("orderId").replace("OID-", "")) + 1)) : "OID-001";
    }

    @Override
    public int saveOrders(String orderId, String customerId, LocalDate orderDate) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO orders (orderId, customerID, date) VALUES (?,?,?)";
        Boolean bool = SQLUtil.execute(sql, orderId, customerId, Date.valueOf(orderDate));
        int intValue = bool ? 1 : 0;
        return intValue;
    }
}
