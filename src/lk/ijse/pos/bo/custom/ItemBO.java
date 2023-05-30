package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;
import lk.ijse.pos.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {

    ArrayList<ItemDTO> loadAll() throws SQLException, ClassNotFoundException;

    boolean save(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean exist(String id) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;
}
