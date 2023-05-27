package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.ItemDAO;
import dao.ItemDAOImpl;
import db.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ItemDTO;
import view.tdm.ItemTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class ManageitemsFormController {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtCode;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtQtyOnHand;
    @FXML
    private JFXTextField txtUnitPrice;
    @FXML
    private JFXButton btnAddNewItem;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private TableView<ItemTM> tblItems;

    ItemDAO itemDAO = new ItemDAOImpl();

    public void initialize() {
        initUI();
        loadAllItems();
        setToTable();
        selectRow();
    }

    private void initUI() {
        txtCode.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtCode.setDisable(true);
        txtDescription.setDisable(true);
        txtQtyOnHand.setDisable(true);
        txtUnitPrice.setDisable(true);
        txtCode.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    private void btnAddNewItemOnAction(ActionEvent actionEvent) {
        txtCode.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtCode.clear();
        txtCode.setText(generateNextItemCode());
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtDescription.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblItems.getSelectionModel().clearSelection();
    }

    private void setToTable() {
        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    private void selectRow() {
        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtCode.setText(newValue.getCode());
                txtDescription.setText(newValue.getDescription());
                txtQtyOnHand.setText(newValue.getQtyOnHand() + "");
                txtUnitPrice.setText(newValue.getUnitPrice().setScale(2).toString());

                txtCode.setDisable(false);
                txtDescription.setDisable(false);
                txtQtyOnHand.setDisable(false);
                txtUnitPrice.setDisable(false);
            }
        });
        txtDescription.setOnAction(event -> btnSave.fire());
    }

    private String generateNextItemCode() {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            String sql = "SELECT itemCode FROM item ORDER BY itemCode DESC LIMIT 1;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                String id = resultSet.getString("itemCode");
                int newCustomerId = Integer.parseInt(id.replace("I00-", "")) + 1;
                return String.format("I00-%03d", newCustomerId);
            } else {
                return "I00-001";
            }*/

            //ItemDAO itemDAO = new ItemDAOImpl();
            return itemDAO.generateNextId();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new code " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "I00-001";
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemCode FROM item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeQuery().next();*/

        //ItemDAO itemDAO = new ItemDAOImpl();
        return itemDAO.exist(code);
    }

    private void loadAllItems() {
        tblItems.getItems().clear();
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            String sql = "SELECT * FROM item";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                ItemTM itemTM = new ItemTM(resultSet.getString("itemCode"), resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));
                tblItems.getItems().add(itemTM);
            }*/

            //ItemDAO itemDAO = new ItemDAOImpl();
            ArrayList<ItemDTO> allItems = itemDAO.loadAll();
            for (ItemDTO item : allItems) {
                ItemTM itemTM = new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice());
                tblItems.getItems().add(itemTM);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    private void btnSaveOnAction(ActionEvent actionEvent) {
        String itemCode = txtCode.getText();
        String description = txtDescription.getText();

        /*Validation*/
        if (!description.matches("[A-Z a-z 0-9]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Description").show();
            txtDescription.requestFocus();
            return;
        } else if (!txtQtyOnHand.getText().matches("^\\d+$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity On Hand").show();
            txtQtyOnHand.requestFocus();
            return;
        } else if (!txtUnitPrice.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Unit Price").show();
            txtUnitPrice.requestFocus();
            return;
        }

        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);

        /*Save Item*/
        if (btnSave.getText().equalsIgnoreCase("Save")) {
            try {
                if (existItem(itemCode)) {
                    new Alert(Alert.AlertType.ERROR, itemCode + " already exists").show();
                }
                /*Connection connection = DBConnection.getDbConnection().getConnection();
                String sql = "INSERT INTO item (itemCode, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, itemCode);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, qtyOnHand);
                preparedStatement.setBigDecimal(4, unitPrice);
                preparedStatement.executeUpdate();*/

                ItemDTO itemDTO = new ItemDTO(itemCode, description, qtyOnHand, unitPrice);
                //ItemDAO itemDAO = new ItemDAOImpl();
                itemDAO.save(itemDTO);

                tblItems.getItems().add(new ItemTM(itemCode, description, qtyOnHand, unitPrice));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the item " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            /*Update Item*/
            try {
                if (!existItem(itemCode)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such item associated with the code " + itemCode).show();
                }
                /*Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE itemCode=?");
                preparedStatement.setString(1, description);
                preparedStatement.setInt(2, qtyOnHand);
                preparedStatement.setBigDecimal(3, unitPrice);
                preparedStatement.setString(4, itemCode);
                preparedStatement.executeUpdate();*/

                ItemDTO itemDTO = new ItemDTO(itemCode, description, qtyOnHand, unitPrice);
                //ItemDAO itemDAO = new ItemDAOImpl();
                itemDAO.update(itemDTO);

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the item " + itemCode + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();
            selectedItem.setDescription(description);
            selectedItem.setQtyOnHand(qtyOnHand);
            selectedItem.setUnitPrice(unitPrice);
            tblItems.refresh();
        }
        btnAddNewItem.fire();
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent actionEvent) {
        String code = tblItems.getSelectionModel().getSelectedItem().getCode();
        try {
            if (!existItem(code)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the code " + code).show();
            }
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM item WHERE itemCode=?");
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();*/

            //ItemDAO itemDAO = new ItemDAOImpl();
            itemDAO.delete(code);

            tblItems.getItems().remove(tblItems.getSelectionModel().getSelectedItem());
            tblItems.getSelectionModel().clearSelection();
            initUI();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the item " + code).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard_form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(scene);
        Platform.runLater(() -> stage.sizeToScene());
    }
}
