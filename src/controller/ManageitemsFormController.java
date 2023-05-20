package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.tdm.ItemTM;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemCode FROM item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeQuery().next();
    }

    @FXML
    private void btnAddNewItemOnAction(ActionEvent actionEvent) {

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
