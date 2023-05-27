package dao;

import model.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<DTO,ID> {

    ArrayList<DTO> loadAll() throws SQLException, ClassNotFoundException;

    boolean save(DTO dto) throws SQLException, ClassNotFoundException;

    boolean update(DTO dto) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    boolean exist(ID id) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;
}
