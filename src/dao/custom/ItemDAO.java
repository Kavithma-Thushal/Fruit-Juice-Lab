package dao.custom;

import dao.CrudDAO;
import model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<ItemDTO,String> {

    /*ArrayList<ItemDTO> loadAll() throws SQLException, ClassNotFoundException;

    boolean save(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean delete(String code) throws SQLException, ClassNotFoundException;

    boolean exist(String code) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;*/

    ItemDTO search(String newItemCode) throws SQLException, ClassNotFoundException;

    int updateItem(ItemDTO item) throws SQLException, ClassNotFoundException;
}
