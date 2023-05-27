package dao;

import db.DBConnection;
import model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public interface ItemDAO {

    ArrayList<ItemDTO> loadAll() throws SQLException, ClassNotFoundException;

    boolean save(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean delete(String code) throws SQLException, ClassNotFoundException;

    boolean exist(String code) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;

    ItemDTO findItem(String newItemCode) throws SQLException, ClassNotFoundException;

    int updateItem(ItemDTO item) throws SQLException, ClassNotFoundException;
}
