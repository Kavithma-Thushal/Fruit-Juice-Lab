package dao.custom.impl;

import dao.custom.ItemDAO;
import db.DBConnection;
import model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public ArrayList<ItemDTO> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "SELECT * FROM item";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ItemDTO itemDTO = new ItemDTO(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBigDecimal(4));
            allItems.add(itemDTO);
        }
        return allItems;
    }

    @Override
    public boolean save(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "INSERT INTO item (itemCode, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, itemDTO.getCode());
        preparedStatement.setString(2, itemDTO.getDescription());
        preparedStatement.setInt(3, itemDTO.getQtyOnHand());
        preparedStatement.setBigDecimal(4, itemDTO.getUnitPrice());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE itemCode=?");
        preparedStatement.setString(1, itemDTO.getDescription());
        preparedStatement.setInt(2, itemDTO.getQtyOnHand());
        preparedStatement.setBigDecimal(3, itemDTO.getUnitPrice());
        preparedStatement.setString(4, itemDTO.getCode());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemCode FROM item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeQuery().next();
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        String sql = "SELECT itemCode FROM item ORDER BY itemCode DESC LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            String id = resultSet.getString("itemCode");
            int newCustomerId = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newCustomerId);
        } else {
            return "I00-001";
        }
    }

    @Override
    public ItemDTO find(String newItemCode) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item WHERE itemCode=?");
        preparedStatement.setString(1, newItemCode + "");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        ItemDTO itemDTO = new ItemDTO(newItemCode + "", resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));
        return itemDTO;
    }

    @Override
    public int updateItem(ItemDTO item) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE Item SET description=?, qtyOnHand=? , unitPrice=? WHERE itemCode=?");
        preparedStatement1.setString(1, item.getDescription());
        preparedStatement1.setInt(2, item.getQtyOnHand());
        preparedStatement1.setBigDecimal(3, item.getUnitPrice());
        preparedStatement1.setString(4, item.getCode());
        return preparedStatement1.executeUpdate();
    }
}
