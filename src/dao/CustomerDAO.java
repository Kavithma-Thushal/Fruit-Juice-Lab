package dao;

import model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;

public interface CustomerDAO {
    public ArrayList<CustomerDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean save(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    public boolean update(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean exist(String id) throws SQLException, ClassNotFoundException;

    public ResultSet generateNextId() throws SQLException, ClassNotFoundException;
}
