package bo;

import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import model.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl {

    ItemDAO itemDAO = new ItemDAOImpl();

    public ArrayList<ItemDTO> loadAll() throws SQLException, ClassNotFoundException {
        return itemDAO.loadAll();
    }

    public boolean save(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.save(itemDTO);
    }

    public boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.update(itemDTO);
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(id);
    }

    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(id);
    }

    public String generateNextId() throws SQLException, ClassNotFoundException {
        return itemDAO.generateNextId();
    }
}
