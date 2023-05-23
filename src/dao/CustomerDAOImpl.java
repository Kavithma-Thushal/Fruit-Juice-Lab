package dao;

import db.DBConnection;
import model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "SELECT * FROM Customer";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            CustomerDTO customerDTO = new CustomerDTO(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            allCustomers.add(customerDTO);
        }
        return allCustomers;
    }

    @Override
    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "INSERT INTO customer (customerId, name, address) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customerDTO.getId());
        preparedStatement.setString(2, customerDTO.getName());
        preparedStatement.setString(3, customerDTO.getAddress());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE customerId=?");
        preparedStatement.setString(1, customerDTO.getId());
        preparedStatement.setString(2, customerDTO.getName());
        preparedStatement.setString(3, customerDTO.getAddress());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE customerId=?");
        preparedStatement.setString(1, id);
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT customerId FROM Customer WHERE customerId=?");
        preparedStatement.setString(1, id);
        return preparedStatement.executeQuery().next();
    }

    @Override
    public ResultSet generateNextCustomerId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }
}
