package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ManagecustomerFormController {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtCustomerAddress;
    @FXML
    private JFXButton btnAddNewCustomer;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private TableView tblCustomers;

    @FXML
    private void btnAddNewOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void btnSaveOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    @FXML
    private void navigateToHome(MouseEvent event) {

    }
}
