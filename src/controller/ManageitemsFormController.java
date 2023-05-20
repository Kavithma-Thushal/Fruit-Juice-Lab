package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.tdm.ItemTM;

import java.math.BigDecimal;
import java.sql.*;

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
        //txtCode.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void loadAllItems() {
        tblItems.getItems().clear();
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            String sql = "SELECT * FROM item";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                ItemTM itemTM = new ItemTM(resultSet.getString("itemCode"), resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));
                tblItems.getItems().add(itemTM);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
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

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemCode FROM item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeQuery().next();
    }

    @FXML
    private void btnAddNewItemOnAction(ActionEvent actionEvent) {
        txtCode.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtCode.clear();
        //txtCode.setText(generateNextCustomerId());
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtDescription.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblItems.getSelectionModel().clearSelection();
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
                Connection connection = DBConnection.getDbConnection().getConnection();
                String sql = "INSERT INTO item (itemCode, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, itemCode);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, qtyOnHand);
                preparedStatement.setBigDecimal(4, unitPrice);
                preparedStatement.executeUpdate();

                tblItems.getItems().add(new ItemTM(itemCode, description, qtyOnHand, unitPrice));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            /*Update Item*/
            try {
                if (!existItem(itemCode)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + itemCode).show();
                }
                Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE itemCode=?");
                preparedStatement.setString(1, description);
                preparedStatement.setInt(2, qtyOnHand);
                preparedStatement.setBigDecimal(3, unitPrice);
                preparedStatement.setString(4, itemCode);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + itemCode + e.getMessage()).show();
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

    }

    @FXML
    private void navigateToHome(MouseEvent event) {

    }
}
