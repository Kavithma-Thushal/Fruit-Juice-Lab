package dao.custom;

import dao.SuperDAO;
import entity.QueryEntity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<QueryEntity> searchOrderByOID(String orderId) throws SQLException, ClassNotFoundException;
}
