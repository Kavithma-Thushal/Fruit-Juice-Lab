package dao.custom.impl;

import dao.custom.CustomerDAO;
import dao.custom.impl.util.SQLUtil;
import model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<CustomerDTO> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        ResultSet resultSet = SQLUtil.execute(sql);
        while (resultSet.next()) {
            CustomerDTO customerDTO = new CustomerDTO(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            allCustomers.add(customerDTO);
        }
        return allCustomers;
    }

    @Override
    public boolean save(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO customer (customerId, name, address) VALUES (?,?,?)";
        return SQLUtil.execute(sql, customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress());
    }

    @Override
    public boolean update(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customer SET name=?, address=? WHERE customerId=?";
        return SQLUtil.execute(sql, customerDTO.getName(), customerDTO.getAddress(), customerDTO.getId());
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
    public CustomerDTO search(String newValue) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE customerId=?";
        ResultSet resultSet = SQLUtil.execute(sql, newValue + "");
        if (resultSet.next()) {
            CustomerDTO customerDTO = new CustomerDTO(newValue + "", resultSet.getString("name"), resultSet.getString("address"));
            return customerDTO;
        }
        return null;
    }
}
