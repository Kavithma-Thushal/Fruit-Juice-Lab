package lk.ijse.pos.controller;

import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.view.tdm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private TableView<CustomerTM> tblCustomers;

    CustomerBO customerBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    public void initialize() {
        initUI();
        loadAllCustomers();
        setToTable();
        selectRow();
    }

    private void initUI() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerId.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        txtCustomerId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void setToTable() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void selectRow() {
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtCustomerId.setText(newValue.getId());
                txtCustomerName.setText(newValue.getName());
                txtCustomerAddress.setText(newValue.getAddress());

                txtCustomerId.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
            }
        });
        txtCustomerAddress.setOnAction(event -> btnSave.fire());
    }

    @FXML
    private void btnAddNewCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerId.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerId.clear();
        txtCustomerId.setText(generateNextCustomerId());
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblCustomers.getSelectionModel().clearSelection();
    }

    private String getLastCustomerId() {
        List<CustomerTM> tempCustomersList = new ArrayList<>(tblCustomers.getItems());
        Collections.sort(tempCustomersList);
        return tempCustomersList.get(tempCustomersList.size() - 1).getId();
    }

    private void loadAllCustomers() {
        tblCustomers.getItems().clear();
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.loadAll();
            for (CustomerDTO customers : allCustomers) {
                tblCustomers.getItems().add(new CustomerTM(customers.getId(), customers.getName(), customers.getAddress()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    private void btnSaveOnAction(ActionEvent actionEvent) {
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String customerAddress = txtCustomerAddress.getText();

        /*Validation*/
        if (!customerName.matches("[A-Z a-z]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            txtCustomerName.requestFocus();
            return;
        } else if (!customerAddress.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();
            txtCustomerAddress.requestFocus();
            return;
        }

        /*Save Customer*/
        if (btnSave.getText().equalsIgnoreCase("Save")) {
            try {
                if (existCustomer(customerId)) {
                    new Alert(Alert.AlertType.ERROR, customerId + " already exists").show();
                }

                customerBO.save(new CustomerDTO(customerId, customerName, customerAddress));
                tblCustomers.getItems().add(new CustomerTM(customerId, customerName, customerAddress));

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            /*Update customer*/
            try {
                if (!existCustomer(customerId)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + customerId).show();
                }

                customerBO.update(new CustomerDTO(customerId, customerName, customerAddress));

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + customerId + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            selectedCustomer.setName(customerName);
            selectedCustomer.setAddress(customerAddress);
            tblCustomers.refresh();
        }
        btnAddNewCustomer.fire();
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = tblCustomers.getSelectionModel().getSelectedItem().getId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }

            customerBO.delete(id);
            tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
            tblCustomers.getSelectionModel().clearSelection();
            initUI();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.exist(id);
    }

    private String generateNextCustomerId() {
        try {
            return customerBO.generateNextId();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (tblCustomers.getItems().isEmpty()) {
            return "C00-001";
        } else {
            String id = getLastCustomerId();
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        }
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();
        }
    }

    @FXML
    private void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/pos/view/dashboard_form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(scene);
        Platform.runLater(() -> stage.sizeToScene());
    }
}
