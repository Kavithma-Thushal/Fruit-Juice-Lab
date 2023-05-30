package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dao.custom.impl.util.SQLUtil;
import lk.ijse.pos.entity.Customer;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> allCustomers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        ResultSet resultSet = SQLUtil.execute(sql);
        while (resultSet.next()) {
            Customer customer = new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            allCustomers.add(customer);
        }
        return allCustomers;
    }

    @Override
    public boolean save(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO customer (customerId, name, address) VALUES (?,?,?)";
        return SQLUtil.execute(sql, customer.getCustomerId(), customer.getName(), customer.getAddress());
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customer SET name=?, address=? WHERE customerId=?";
        return SQLUtil.execute(sql, customer.getName(), customer.getAddress(), customer.getCustomerId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM customer WHERE customerId=?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT customerId FROM Customer WHERE customerId=?";
        ResultSet resultSet = SQLUtil.execute(sql, id);
        return resultSet.next();
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1;";
        ResultSet resultSet = SQLUtil.execute(sql);
        if (resultSet.next()) {
            String id = resultSet.getString("customerId");
            int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        } else {
            return "C00-001";
        }
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE customerId=?";
        ResultSet resultSet = SQLUtil.execute(sql, id);
        if (resultSet.next()) {
            Customer customer = new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            return customer;
        }
        return null;
    }
}
