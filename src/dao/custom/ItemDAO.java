package dao.custom;

import dao.CrudDAO;
import model.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<ItemDTO,String> {

    //int updateItem(ItemDTO item) throws SQLException, ClassNotFoundException;
}
