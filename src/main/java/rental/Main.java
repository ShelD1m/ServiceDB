package rental;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import rental.Address.Address;
import rental.Address.AddressDAO;
import rental.Address.AddressDAOImpl;
import rental.Apartment.Apartment;
import rental.Apartment.ApartmentDAO;
import rental.Apartment.ApartmentDAOImpl;
import rental.Landlord.Landlord;
import rental.Landlord.LandlordDAO;
import rental.Landlord.LandlordDAOImpl;
import rental.LeaseAgreement.LeaseAgreement;
import rental.LeaseAgreement.LeaseAgreementDAO;
import rental.LeaseAgreement.LeaseAgreementDAOImpl;
import rental.LeaseTermination.LeaseTermination;
import rental.LeaseTermination.LeaseTerminationDAO;
import rental.LeaseTermination.LeaseTerminationDAOImpl;
import rental.Organization.Organization;
import rental.Organization.OrganizationDAO;
import rental.Organization.OrganizationDAOImpl;
import rental.Payment.Payment;
import rental.Payment.PaymentDAO;
import rental.Payment.PaymentDAOImpl;
import rental.Service.Service;
import rental.Service.ServiceDAO;
import rental.Service.ServiceDAOImpl;
import rental.Tenant.Tenant;
import rental.Tenant.TenantDAO;
import rental.Tenant.TenantDAOImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Optional;

public class Main extends Application {

    private Connection connection;
    private ApartmentDAO apartmentDao;
    private LandlordDAO landlordDao;
    private TenantDAO tenantDao;
    private LeaseAgreementDAO leaseAgreementDao;
    private PaymentDAO paymentDao;
    private ServiceDAO serviceDao;
    private OrganizationDAO organizationDao;
    private AddressDAO addressDao;
    private LeaseTerminationDAO leaseTerminationDao;

    private ObservableList<Apartment> apartmentList;
    private ObservableList<Landlord> landlordList;
    private ObservableList<Tenant> tenantList;
    private ObservableList<LeaseAgreement> leaseAgreementList;
    private ObservableList<Payment> paymentList;
    private ObservableList<Service> serviceList;
    private ObservableList<Organization> organizationList;
    private ObservableList<Address> addressList;
    private ObservableList<LeaseTermination> leaseTerminationList;

    public static void main(String[] args) {
        DBConnectionManager.checkDriver();
        DBConnectionManager.checkDB();
        System.out.println("Подключение к базе данных | " + DBConnectionManager.DATABASE_URL + "\n");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            connection = DBConnectionManager.getConnection();

            apartmentDao = new ApartmentDAOImpl(connection);
            landlordDao = new LandlordDAOImpl(connection);
            tenantDao = new TenantDAOImpl(connection);
            leaseAgreementDao = new LeaseAgreementDAOImpl(connection);
            paymentDao = new PaymentDAOImpl(connection);
            serviceDao = new ServiceDAOImpl(connection);
            organizationDao = new OrganizationDAOImpl(connection);
            addressDao = new AddressDAOImpl(connection);
            leaseTerminationDao = new LeaseTerminationDAOImpl(connection);

            TabPane tabPane = new TabPane();

            Tab apartmentTab = createApartmentTab();
            Tab landlordTab = createLandlordTab();
            Tab tenantTab = createTenantTab();
            Tab serviceTab = createServiceTab();
            Tab organizationTab = createOrganizationTab();
            Tab agreementTab = createLeaseAgreementTab();
            Tab paymentTab = createPaymentTab();
            Tab addressTab = createAddressTab();
            Tab terminationTab = createLeaseTerminationTab();

            tabPane.getTabs().addAll(
                    apartmentTab, landlordTab, tenantTab, serviceTab, organizationTab,
                    agreementTab, paymentTab, addressTab, terminationTab
            );
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            Scene scene = new Scene(tabPane, 900, 700);
            stage.setScene(scene);
            stage.setTitle("Rental Management System - Simple UI");
            stage.show();

        } catch (SQLException e) {
            showErrorAlert("Database Connection Error", "Could not connect to the database: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        } catch (Exception e) {
            showErrorAlert("Application Error", "An unexpected error occurred during startup: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    private Tab createApartmentTab() {
        TableView<Apartment> table = new TableView<>();
        setupApartmentTableColumns(table);
        apartmentList = FXCollections.observableArrayList(apartmentDao.findAll());
        table.setItems(apartmentList);
        TextField landlordIdField = createNumericTextField("Landlord ID (FK)");
        TextField roomCountField = createNumericTextField("Rooms");
        TextField squareMetersField = createDecimalTextField("Square Meters");
        Button addBtn = new Button("Добавить");
        Button updateBtn = new Button("Обновить");
        Button deleteBtn = new Button("Удалить");
        Button clearBtn = new Button("Очистить форму");
        TextField[] fields = {landlordIdField, roomCountField, squareMetersField};
        addBtn.setOnAction(e -> addApartment(fields));
        updateBtn.setOnAction(e -> updateApartment(table, fields));
        deleteBtn.setOnAction(e -> deleteApartment(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(fields);
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                landlordIdField.setText(String.valueOf(newSelection.getLandlordId()));
                roomCountField.setText(String.valueOf(newSelection.getRoomCount()));
                squareMetersField.setText(String.valueOf(newSelection.getSquareMeters()));
            } else {
                clearFields(fields);
            }
        });

        HBox formBox = new HBox(10, landlordIdField, roomCountField, squareMetersField);
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formBox, buttonBox);
        contentBox.setPadding(new Insets(10));

        Tab tab = new Tab("Apartments");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupApartmentTableColumns(TableView<Apartment> table) {
        TableColumn<Apartment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        TableColumn<Apartment, Integer> landlordCol = new TableColumn<>("Landlord ID");
        landlordCol.setCellValueFactory(new PropertyValueFactory<>("landlordId"));
        TableColumn<Apartment, Integer> roomsCol = new TableColumn<>("Rooms");
        roomsCol.setCellValueFactory(new PropertyValueFactory<>("roomCount"));
        TableColumn<Apartment, Double> metersCol = new TableColumn<>("Square");
        metersCol.setCellValueFactory(new PropertyValueFactory<>("squareMeters"));
        table.getColumns().setAll(idCol, landlordCol, roomsCol, metersCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addApartment(TextField[] fields) {
        try {
            int landlordId = Integer.parseInt(fields[0].getText());
            int roomCount = Integer.parseInt(fields[1].getText());
            double squareMeters = Double.parseDouble(fields[2].getText().replace(',', '.'));
            Apartment a = new Apartment(0, landlordId, roomCount, squareMeters);
            apartmentDao.add(a);
            refreshApartmentTable();
            clearFields(fields);
        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers.");
        } catch (Exception ex) {
            showDbErrorAlert("add apartment", ex);
        }
    }

    private void updateApartment(TableView<Apartment> table, TextField[] fields) {
        Apartment selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("apartment");
            return;
        }
        try {
            selected.setLandlordId(Integer.parseInt(fields[0].getText()));
            selected.setRoomCount(Integer.parseInt(fields[1].getText()));
            selected.setSquareMeters(Double.parseDouble(fields[2].getText().replace(',', '.')));
            apartmentDao.update(selected);
            refreshApartmentTable();
            table.getSelectionModel().clearSelection(); // Очищаем выбор и поля
        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers.");
        } catch (Exception ex) {
            showDbErrorAlert("update apartment", ex);
        }
    }

    private void deleteApartment(TableView<Apartment> table) {
        Apartment selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("apartment");
            return;
        }
        if (!showConfirmationAlert("Delete Apartment", "Are you sure you want to delete apartment ID: " + selected.getApartmentId() + "?")) {
            return;
        }
        try {
            apartmentDao.delete(selected.getApartmentId());
            refreshApartmentTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete apartment", ex, "It might be referenced elsewhere.");
        }
    }

    private void refreshApartmentTable() {
        try {
            apartmentList.setAll(apartmentDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh apartment data", e);
        }
    }

    private Tab createLandlordTab() {
        TableView<Landlord> table = new TableView<>();
        setupLandlordTableColumns(table);
        landlordList = FXCollections.observableArrayList(landlordDao.findAll());
        table.setItems(landlordList);

        TextField passportField = new TextField();
        passportField.setPromptText("Passport Series/Number");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        TextField[] fields = {passportField, nameField, phoneField};

        addBtn.setOnAction(e -> addLandlord(fields));
        updateBtn.setOnAction(e -> updateLandlord(table, fields));
        deleteBtn.setOnAction(e -> deleteLandlord(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(fields);
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fields[0].setText(newSelection.getPassportSeriesNumber());
                fields[1].setText(newSelection.getFullName());
                fields[2].setText(newSelection.getPhoneNumber());
            } else {
                clearFields(fields);
            }
        });

        HBox formBox = new HBox(10, fields);
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formBox, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Landlords");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupLandlordTableColumns(TableView<Landlord> table) {
        TableColumn<Landlord, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("landlordId"));
        TableColumn<Landlord, String> passportCol = new TableColumn<>("Passport");
        passportCol.setCellValueFactory(new PropertyValueFactory<>("passportSeriesNumber"));
        TableColumn<Landlord, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Landlord, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        table.getColumns().setAll(idCol, passportCol, nameCol, phoneCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addLandlord(TextField[] fields) {
        try {
            String passport = fields[0].getText();
            String name = fields[1].getText();
            String phone = fields[2].getText();
            if (passport.isEmpty() || name.isEmpty()) {
                showErrorAlert("Input Error", "Passport and Name cannot be empty.");
                return;
            }
            Landlord l = new Landlord(0, passport, name, phone);
            landlordDao.add(l);
            refreshLandlordTable();
            clearFields(fields);
        } catch (Exception ex) {
            showDbErrorAlert("add landlord", ex);
        }
    }

    private void updateLandlord(TableView<Landlord> table, TextField[] fields) {
        Landlord selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("landlord");
            return;
        }
        try {
            String passport = fields[0].getText();
            String name = fields[1].getText();
            String phone = fields[2].getText();
            if (passport.isEmpty() || name.isEmpty()) {
                showErrorAlert("Input Error", "Passport and Name cannot be empty.");
                return;
            }
            selected.setPassportSeriesNumber(passport);
            selected.setFullName(name);
            selected.setPhoneNumber(phone);
            landlordDao.update(selected);
            refreshLandlordTable();
            table.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            showDbErrorAlert("update landlord", ex);
        }
    }

    private void deleteLandlord(TableView<Landlord> table) {
        Landlord selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("landlord");
            return;
        }
        if (!showConfirmationAlert("Delete Landlord", "Are you sure you want to delete landlord ID: " + selected.getLandlordId() + "?"))
            return;
        try {
            landlordDao.delete(selected.getLandlordId());
            refreshLandlordTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete landlord", ex, "They might be referenced elsewhere.");
        }
    }

    private void refreshLandlordTable() {
        try {
            landlordList.setAll(landlordDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh landlord data", e);
        }
    }

    private Tab createTenantTab() {
        TableView<Tenant> table = new TableView<>();
        setupTenantTableColumns(table);
        tenantList = FXCollections.observableArrayList(tenantDao.findAll());
        table.setItems(tenantList);

        TextField passportField = new TextField();
        passportField.setPromptText("Passport Series/Number");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        TextField[] fields = {passportField, nameField, phoneField};

        addBtn.setOnAction(e -> addTenant(fields));
        updateBtn.setOnAction(e -> updateTenant(table, fields));
        deleteBtn.setOnAction(e -> deleteTenant(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(fields);
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fields[0].setText(newSelection.getPassportSeriesNumber());
                fields[1].setText(newSelection.getFullName());
                fields[2].setText(newSelection.getPhoneNumber());
            } else {
                clearFields(fields);
            }
        });

        HBox formBox = new HBox(10, fields);
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formBox, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Tenants");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupTenantTableColumns(TableView<Tenant> table) {
        TableColumn<Tenant, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        TableColumn<Tenant, String> passportCol = new TableColumn<>("Passport");
        passportCol.setCellValueFactory(new PropertyValueFactory<>("passportSeriesNumber"));
        TableColumn<Tenant, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Tenant, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        table.getColumns().setAll(idCol, passportCol, nameCol, phoneCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addTenant(TextField[] fields) {
        try {
            String passport = fields[0].getText();
            String name = fields[1].getText();
            String phone = fields[2].getText();
            if (passport.isEmpty() || name.isEmpty()) {
                showErrorAlert("Input Error", "Passport and Name cannot be empty.");
                return;
            }
            Tenant t = new Tenant(0, passport, phone, name);
            tenantDao.add(t);
            refreshTenantTable();
            clearFields(fields);
        } catch (Exception ex) {
            showDbErrorAlert("add tenant", ex);
        }
    }

    private void updateTenant(TableView<Tenant> table, TextField[] fields) {
        Tenant selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("tenant");
            return;
        }
        try {
            String passport = fields[0].getText();
            String name = fields[1].getText();
            String phone = fields[2].getText();
            if (passport.isEmpty() || name.isEmpty()) {
                showErrorAlert("Input Error", "Passport and Name cannot be empty.");
                return;
            }
            selected.setPassportSeriesNumber(passport);
            selected.setFullName(name);
            selected.setPhoneNumber(phone);
            tenantDao.update(selected);
            refreshTenantTable();
            table.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            showDbErrorAlert("update tenant", ex);
        }
    }

    private void deleteTenant(TableView<Tenant> table) {
        Tenant selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("tenant");
            return;
        }
        if (!showConfirmationAlert("Delete Tenant", "Are you sure you want to delete tenant ID: " + selected.getTenantId() + "?"))
            return;
        try {
            tenantDao.delete(selected.getTenantId());
            refreshTenantTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete tenant", ex, "They might be referenced elsewhere.");
        }
    }

    private void refreshTenantTable() {
        try {
            tenantList.setAll(tenantDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh tenant data", e);
        }
    }


    private Tab createServiceTab() {
        TableView<Service> table = new TableView<>();
        setupServiceTableColumns(table);
        serviceList = FXCollections.observableArrayList(serviceDao.findAll());
        table.setItems(serviceList);

        TextField codeField = createNumericTextField("Service Code (PK)");
        TextField nameField = new TextField();
        nameField.setPromptText("Service Name");
        TextField costField = createDecimalTextField("Cost (e.g., 123.45)");
        TextField typeField = new TextField();
        typeField.setPromptText("Service Type");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        TextField[] fields = {codeField, nameField, costField, typeField};

        addBtn.setOnAction(e -> addService(fields));
        updateBtn.setOnAction(e -> updateService(table, fields));
        deleteBtn.setOnAction(e -> deleteService(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fields[0].setText(String.valueOf(newVal.getServiceCode()));
                fields[1].setText(newVal.getName());
                fields[2].setText(newVal.getCost().toPlainString());
                fields[3].setText(newVal.getServiceType());
                fields[0].setDisable(true);
            } else {
                clearFields(fields);
                fields[0].setDisable(false);
            }
        });

        HBox formBox = new HBox(10, fields);
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formBox, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Services");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupServiceTableColumns(TableView<Service> table) {
        TableColumn<Service, Integer> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("serviceCode"));
        TableColumn<Service, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Service, BigDecimal> costCol = new TableColumn<>("Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        TableColumn<Service, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        table.getColumns().setAll(codeCol, nameCol, costCol, typeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addService(TextField[] fields) {
        try {
            int code = Integer.parseInt(fields[0].getText());
            String name = fields[1].getText();
            BigDecimal cost = new BigDecimal(fields[2].getText().replace(',', '.'));
            String type = fields[3].getText();
            if (name.isEmpty()) {
                showErrorAlert("Input Error", "Service name cannot be empty.");
                return;
            }

            Service s = new Service(code, name, cost, type);
            serviceDao.add(s);
            refreshServiceTable();
            clearFields(fields);

        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid number for Service Code and Cost.");
        } catch (Exception ex) {
            showDbErrorAlert("add service", ex, "Service code might already exist.");
        }
    }

    private void updateService(TableView<Service> table, TextField[] fields) {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("service");
            return;
        }
        try {
            String name = fields[1].getText();
            BigDecimal cost = new BigDecimal(fields[2].getText().replace(',', '.'));
            String type = fields[3].getText();
            if (name.isEmpty()) {
                showErrorAlert("Input Error", "Service name cannot be empty.");
                return;
            }

            selected.setName(name);
            selected.setCost(cost);
            selected.setServiceType(type);
            serviceDao.update(selected);
            refreshServiceTable();
            table.getSelectionModel().clearSelection();

        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter a valid number for Cost.");
        } catch (Exception ex) {
            showDbErrorAlert("update service", ex);
        }
    }

    private void deleteService(TableView<Service> table) {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("service");
            return;
        }
        if (!showConfirmationAlert("Delete Service", "Are you sure you want to delete service code: " + selected.getServiceCode() + "?"))
            return;
        try {
            serviceDao.delete(selected.getServiceCode());
            refreshServiceTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete service", ex, "It might be referenced elsewhere.");
        }
    }

    private void refreshServiceTable() {
        try {
            serviceList.setAll(serviceDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh service data", e);
        }
    }


    private Tab createOrganizationTab() {
        TableView<Organization> table = new TableView<>();
        setupOrganizationTableColumns(table);
        organizationList = FXCollections.observableArrayList(organizationDao.findAll());
        table.setItems(organizationList);

        TextField websiteField = new TextField();
        websiteField.setPromptText("Website");
        TextField nameField = new TextField();
        nameField.setPromptText("Org Name");
        TextField innField = new TextField();
        innField.setPromptText("INN");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        TextField[] fields = {websiteField, nameField, innField};

        addBtn.setOnAction(e -> addOrganization(fields));
        updateBtn.setOnAction(e -> updateOrganization(table, fields));
        deleteBtn.setOnAction(e -> deleteOrganization(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(fields);
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fields[0].setText(newSelection.getWebsite());
                fields[1].setText(newSelection.getName());
                fields[2].setText(newSelection.getInn());
            } else {
                clearFields(fields);
            }
        });

        HBox formBox = new HBox(10, fields);
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formBox, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Organizations");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupOrganizationTableColumns(TableView<Organization> table) {
        TableColumn<Organization, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("organizationId"));
        TableColumn<Organization, String> websiteCol = new TableColumn<>("Website");
        websiteCol.setCellValueFactory(new PropertyValueFactory<>("website"));
        TableColumn<Organization, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Organization, String> innCol = new TableColumn<>("INN");
        innCol.setCellValueFactory(new PropertyValueFactory<>("inn"));
        table.getColumns().setAll(idCol, websiteCol, nameCol, innCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addOrganization(TextField[] fields) {
        try {
            String website = fields[0].getText();
            String name = fields[1].getText();
            String inn = fields[2].getText();
            if (name.isEmpty() || inn.isEmpty()) {
                showErrorAlert("Input Error", "Org Name and INN cannot be empty.");
                return;
            }
            Organization o = new Organization(0, website, name, inn);
            organizationDao.add(o);
            refreshOrganizationTable();
            clearFields(fields);
        } catch (Exception ex) {
            showDbErrorAlert("add organization", ex);
        }
    }

    private void updateOrganization(TableView<Organization> table, TextField[] fields) {
        Organization selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("organization");
            return;
        }
        try {
            String website = fields[0].getText();
            String name = fields[1].getText();
            String inn = fields[2].getText();
            if (name.isEmpty() || inn.isEmpty()) {
                showErrorAlert("Input Error", "Org Name and INN cannot be empty.");
                return;
            }
            selected.setWebsite(website);
            selected.setName(name);
            selected.setInn(inn);
            organizationDao.update(selected);
            refreshOrganizationTable();
            table.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            showDbErrorAlert("update organization", ex);
        }
    }

    private void deleteOrganization(TableView<Organization> table) {
        Organization selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("organization");
            return;
        }
        if (!showConfirmationAlert("Delete Organization", "Are you sure you want to delete org ID: " + selected.getOrganizationId() + "?"))
            return;
        try {
            organizationDao.delete(selected.getOrganizationId());
            refreshOrganizationTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete organization", ex, "It might be referenced elsewhere.");
        }
    }

    private void refreshOrganizationTable() {
        try {
            organizationList.setAll(organizationDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh organization data", e);
        }
    }

    private Tab createLeaseAgreementTab() {
        TableView<LeaseAgreement> table = new TableView<>();
        setupLeaseAgreementTableColumns(table);
        leaseAgreementList = FXCollections.observableArrayList(leaseAgreementDao.findAll());
        table.setItems(leaseAgreementList);
        TextField numberField = new TextField();
        numberField.setPromptText("Agreement Number (PK)");
        TextField landlordIdField = createNumericTextField("Landlord ID (FK)");
        TextField tenantIdField = createNumericTextField("Tenant ID (FK)");
        TextField apartmentIdField = createNumericTextField("Apartment ID (FK)");
        DatePicker signingDatePicker = new DatePicker();
        signingDatePicker.setPromptText("Signing Date");
        TextField termField = createNumericTextField("Term (months)");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        Control[] fields = {numberField, landlordIdField, tenantIdField, apartmentIdField, signingDatePicker, termField};

        addBtn.setOnAction(e -> addLeaseAgreement(fields));
        updateBtn.setOnAction(e -> updateLeaseAgreement(table, fields));
        deleteBtn.setOnAction(e -> deleteLeaseAgreement(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(numberField, landlordIdField, tenantIdField, apartmentIdField, termField);
            signingDatePicker.setValue(null);
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                numberField.setText(newVal.getAgreementNumber());
                landlordIdField.setText(String.valueOf(newVal.getLandlordId()));
                tenantIdField.setText(String.valueOf(newVal.getTenantId()));
                apartmentIdField.setText(String.valueOf(newVal.getApartmentId()));
                signingDatePicker.setValue(newVal.getSigningDate() != null ? newVal.getSigningDate().toLocalDate() : null);
                termField.setText(String.valueOf(newVal.getTermMonths()));
                numberField.setDisable(true);
            } else {
                clearFields(numberField, landlordIdField, tenantIdField, apartmentIdField, termField);
                signingDatePicker.setValue(null);
                numberField.setDisable(false);
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(5);
        formGrid.add(new Label("Number (PK):"), 0, 0);
        formGrid.add(numberField, 1, 0);
        formGrid.add(new Label("Landlord ID:"), 0, 1);
        formGrid.add(landlordIdField, 1, 1);
        formGrid.add(new Label("Tenant ID:"), 0, 2);
        formGrid.add(tenantIdField, 1, 2);
        formGrid.add(new Label("Apartment ID:"), 2, 0);
        formGrid.add(apartmentIdField, 3, 0);
        formGrid.add(new Label("Sign Date:"), 2, 1);
        formGrid.add(signingDatePicker, 3, 1);
        formGrid.add(new Label("Term (months):"), 2, 2);
        formGrid.add(termField, 3, 2);

        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formGrid, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Lease Agreements");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupLeaseAgreementTableColumns(TableView<LeaseAgreement> table) {
        TableColumn<LeaseAgreement, String> numberCol = new TableColumn<>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("agreementNumber"));
        TableColumn<LeaseAgreement, Integer> landlordCol = new TableColumn<>("Landlord ID");
        landlordCol.setCellValueFactory(new PropertyValueFactory<>("landlordId"));
        TableColumn<LeaseAgreement, Integer> tenantCol = new TableColumn<>("Tenant ID");
        tenantCol.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        TableColumn<LeaseAgreement, Integer> apartmentCol = new TableColumn<>("Apartment ID");
        apartmentCol.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        TableColumn<LeaseAgreement, Date> dateCol = new TableColumn<>("Sign Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("signingDate"));
        TableColumn<LeaseAgreement, Integer> termCol = new TableColumn<>("Term (months)");
        termCol.setCellValueFactory(new PropertyValueFactory<>("termMonths"));
        table.getColumns().setAll(numberCol, landlordCol, tenantCol, apartmentCol, dateCol, termCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addLeaseAgreement(Control[] fields) {
        try {
            String number = ((TextField) fields[0]).getText();
            int landlordId = Integer.parseInt(((TextField) fields[1]).getText());
            int tenantId = Integer.parseInt(((TextField) fields[2]).getText());
            int apartmentId = Integer.parseInt(((TextField) fields[3]).getText());
            LocalDate localDate = ((DatePicker) fields[4]).getValue();
            String contractDuration = ((TextField) fields[5]).getText();

            if (number.isEmpty()) {
                showErrorAlert("Input Error", "Agreement number cannot be empty.");
                return;
            }
            if (localDate == null) {
                showErrorAlert("Input Error", "Signing date must be selected.");
                return;
            }

            Date sqlDate = Date.valueOf(localDate);
            LeaseAgreement la = new LeaseAgreement(number, landlordId, tenantId, apartmentId, sqlDate, contractDuration);
            leaseAgreementDao.add(la);
            refreshLeaseAgreementTable();
            ((TextField) fields[0]).clear();
            ((TextField) fields[1]).clear(); //...
            ((DatePicker) fields[4]).setValue(null);
            ((TextField) fields[5]).clear();


        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers for IDs and Term.");
        } catch (Exception ex) {
            showDbErrorAlert("add lease agreement", ex, "Agreement number might already exist or FK constraints failed.");
        }
    }

    private void updateLeaseAgreement(TableView<LeaseAgreement> table, Control[] fields) {
        LeaseAgreement selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("lease agreement");
            return;
        }
        try {
            int landlordId = Integer.parseInt(((TextField) fields[1]).getText());
            int tenantId = Integer.parseInt(((TextField) fields[2]).getText());
            int apartmentId = Integer.parseInt(((TextField) fields[3]).getText());
            LocalDate localDate = ((DatePicker) fields[4]).getValue();
            int termMonths = Integer.parseInt(((TextField) fields[5]).getText());

            if (localDate == null) {
                showErrorAlert("Input Error", "Signing date must be selected.");
                return;
            }
            Date sqlDate = Date.valueOf(localDate);

            selected.setLandlordId(landlordId);
            selected.setTenantId(tenantId);
            selected.setApartmentId(apartmentId);
            selected.setSigningDate(sqlDate);
            selected.setTermMonths(termMonths);

            leaseAgreementDao.update(selected);
            refreshLeaseAgreementTable();
            table.getSelectionModel().clearSelection();

        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers for IDs and Term.");
        } catch (Exception ex) {
            showDbErrorAlert("update lease agreement", ex, "FK constraints might have failed.");
        }
    }

    private void deleteLeaseAgreement(TableView<LeaseAgreement> table) {
        LeaseAgreement selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("lease agreement");
            return;
        }
        if (!showConfirmationAlert("Delete Agreement", "Are you sure you want to delete agreement: " + selected.getAgreementNumber() + "?"))
            return;
        try {
            leaseAgreementDao.delete(selected.getAgreementNumber());
            refreshLeaseAgreementTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete lease agreement", ex, "It might be referenced elsewhere (e.g., termination).");
        }
    }

    private void refreshLeaseAgreementTable() {
        try {
            leaseAgreementList.setAll(leaseAgreementDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh lease agreement data", e);
        }
    }

    private Tab createPaymentTab() {
        TableView<Payment> table = new TableView<>();
        setupPaymentTableColumns(table);
        paymentList = FXCollections.observableArrayList(paymentDao.findAll());
        table.setItems(paymentList);

        TextField tenantIdField = createNumericTextField("Tenant ID (FK)");
        TextField serviceCodeField = createNumericTextField("Service Code (FK)");
        TextField organizationIdField = createNumericTextField("Org ID (FK)");
        DatePicker paymentDatePicker = new DatePicker();
        paymentDatePicker.setPromptText("Payment Date");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear Form");

        Control[] fields = {tenantIdField, serviceCodeField, organizationIdField, paymentDatePicker};

        addBtn.setOnAction(e -> addPayment(fields));
        updateBtn.setOnAction(e -> updatePayment(table, fields));
        deleteBtn.setOnAction(e -> deletePayment(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFields(tenantIdField, serviceCodeField, organizationIdField);
            paymentDatePicker.setValue(null);
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tenantIdField.setText(String.valueOf(newVal.getTenantId()));
                serviceCodeField.setText(String.valueOf(newVal.getServiceCode()));
                organizationIdField.setText(String.valueOf(newVal.getOrganizationId()));
                paymentDatePicker.setValue(newVal.getPaymentDate() != null ? newVal.getPaymentDate().toLocalDate() : null);
            } else {
                clearFields(tenantIdField, serviceCodeField, organizationIdField);
                paymentDatePicker.setValue(null);
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(5);
        formGrid.add(new Label("Tenant ID:"), 0, 0);
        formGrid.add(tenantIdField, 1, 0);
        formGrid.add(new Label("Service Code:"), 0, 1);
        formGrid.add(serviceCodeField, 1, 1);
        formGrid.add(new Label("Org ID:"), 2, 0);
        formGrid.add(organizationIdField, 3, 0);
        formGrid.add(new Label("Payment Date:"), 2, 1);
        formGrid.add(paymentDatePicker, 3, 1);

        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formGrid, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Payments");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupPaymentTableColumns(TableView<Payment> table) {
        TableColumn<Payment, Integer> idCol = new TableColumn<>("Paym ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        TableColumn<Payment, Integer> tenantCol = new TableColumn<>("Tenant ID");
        tenantCol.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        TableColumn<Payment, Integer> serviceCol = new TableColumn<>("Service Code");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceCode"));
        TableColumn<Payment, Integer> orgCol = new TableColumn<>("Org ID");
        orgCol.setCellValueFactory(new PropertyValueFactory<>("organizationId"));
        TableColumn<Payment, Date> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        table.getColumns().setAll(idCol, tenantCol, serviceCol, orgCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addPayment(Control[] fields) {
        try {
            int tenantId = Integer.parseInt(((TextField) fields[0]).getText());
            int serviceCode = Integer.parseInt(((TextField) fields[1]).getText());
            int orgId = Integer.parseInt(((TextField) fields[2]).getText());
            LocalDate localDate = ((DatePicker) fields[3]).getValue();
            if (localDate == null) {
                showErrorAlert("Input Error", "Payment date must be selected.");
                return;
            }
            Date sqlDate = Date.valueOf(localDate);

            Payment p = new Payment(0, tenantId, serviceCode, orgId, sqlDate);
            paymentDao.add(p);
            refreshPaymentTable();
            clearFields(((TextField) fields[0]), ((TextField) fields[1]), ((TextField) fields[2]));
            ((DatePicker) fields[3]).setValue(null);

        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers for IDs and Code.");
        } catch (Exception ex) {
            showDbErrorAlert("add payment", ex, "FK constraints might have failed.");
        }
    }

    private void updatePayment(TableView<Payment> table, Control[] fields) {
        Payment selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("payment");
            return;
        }
        try {
            int tenantId = Integer.parseInt(((TextField) fields[0]).getText());
            int serviceCode = Integer.parseInt(((TextField) fields[1]).getText());
            int orgId = Integer.parseInt(((TextField) fields[2]).getText());
            LocalDate localDate = ((DatePicker) fields[3]).getValue();
            if (localDate == null) {
                showErrorAlert("Input Error", "Payment date must be selected.");
                return;
            }
            Date sqlDate = Date.valueOf(localDate);

            selected.setTenantId(tenantId);
            selected.setServiceCode(serviceCode);
            selected.setOrganizationId(orgId);
            selected.setPaymentDate(sqlDate);

            paymentDao.update(selected);
            refreshPaymentTable();
            table.getSelectionModel().clearSelection();

        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers for IDs and Code.");
        } catch (Exception ex) {
            showDbErrorAlert("update payment", ex, "FK constraints might have failed.");
        }
    }

    private void deletePayment(TableView<Payment> table) {
        Payment selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("payment");
            return;
        }
        if (!showConfirmationAlert("Delete Payment", "Are you sure you want to delete payment ID: " + selected.getPaymentId() + "?"))
            return;
        try {
            paymentDao.delete(selected.getPaymentId());
            refreshPaymentTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete payment", ex);
        }
    }

    private void refreshPaymentTable() {
        try {
            paymentList.setAll(paymentDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh payment data", e);
        }
    }


    private Tab createAddressTab() {
        TableView<Address> table = new TableView<>();
        setupAddressTableColumns(table);
        addressList = FXCollections.observableArrayList(addressDao.findAll());
        table.setItems(addressList);

        TextField apartmentIdField = createNumericTextField("Apartment ID (PK/FK)");
        TextField streetField = new TextField("Street");
        streetField.setPromptText("Street");
        TextField houseField = new TextField("House No");
        houseField.setPromptText("House No");
        TextField floorField = createNumericTextField("Floor (optional)");
        TextField aptNoField = new TextField("Apt No");
        aptNoField.setPromptText("Apt No");
        TextField regionField = new TextField("Region");
        regionField.setPromptText("Region");
        TextField cityField = new TextField("City");
        cityField.setPromptText("City");


        Button addBtn = new Button("Add/Update Address");
        Button deleteBtn = new Button("Delete Address");
        Button clearBtn = new Button("Clear Form");


        Control[] fields = {apartmentIdField, streetField, houseField, floorField, aptNoField, regionField, cityField};

        addBtn.setOnAction(e -> addOrUpdateAddress(table, fields));
        deleteBtn.setOnAction(e -> deleteAddress(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                apartmentIdField.setText(String.valueOf(newVal.getApartmentId()));
                streetField.setText(newVal.getStreet());
                houseField.setText(newVal.getHouseNumber());
                floorField.setText(newVal.getFloor() != null ? String.valueOf(newVal.getFloor()) : "");
                aptNoField.setText(newVal.getApartmentNumber());
                regionField.setText(newVal.getRegion());
                cityField.setText(newVal.getCity());
                apartmentIdField.setDisable(true);
            } else {
                clearFields((TextField[]) fields);
                apartmentIdField.setDisable(false);
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(5);
        formGrid.addColumn(0, new Label("Apt ID (PK):"), new Label("Street:"), new Label("House No:"), new Label("Floor:"));
        formGrid.addColumn(1, apartmentIdField, streetField, houseField, floorField);
        formGrid.addColumn(2, new Label("Apt No:"), new Label("Region:"), new Label("City:"));
        formGrid.addColumn(3, aptNoField, regionField, cityField);


        HBox buttonBox = new HBox(10, addBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formGrid, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Addresses");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupAddressTableColumns(TableView<Address> table) {
        TableColumn<Address, Integer> aptIdCol = new TableColumn<>("Apt ID");
        aptIdCol.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        TableColumn<Address, String> streetCol = new TableColumn<>("Street");
        streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
        TableColumn<Address, String> houseCol = new TableColumn<>("House No");
        houseCol.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
        TableColumn<Address, Integer> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
        TableColumn<Address, String> aptNoCol = new TableColumn<>("Apt No");
        aptNoCol.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        TableColumn<Address, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        TableColumn<Address, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        table.getColumns().setAll(aptIdCol, cityCol, regionCol, streetCol, houseCol, aptNoCol, floorCol); // Порядок
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addOrUpdateAddress(TableView<Address> table, Control[] fields) {
        try {
            int apartmentId = Integer.parseInt(((TextField) fields[0]).getText());
            String street = ((TextField) fields[1]).getText();
            String house = ((TextField) fields[2]).getText();
            String floorStr = ((TextField) fields[3]).getText();
            String aptNo = ((TextField) fields[4]).getText();
            String region = ((TextField) fields[5]).getText();
            String city = ((TextField) fields[6]).getText();

            if (street.isEmpty() || house.isEmpty() || city.isEmpty()) {
                showErrorAlert("Input Error", "Street, House No, and City are required.");
                return;
            }
            Integer floor = floorStr.isEmpty() ? null : Integer.parseInt(floorStr);

            Address address = new Address(0, apartmentId, street, house, floor, aptNo, region, city);

            Optional<Address> existing = addressDao.findByApartmentId(apartmentId);
            if (existing.isPresent()) {
                addressDao.update(address);
                showInfoAlert("Success", "Address updated successfully.");
            } else {
                addressDao.add(address);
                showInfoAlert("Success", "Address added successfully.");
            }

            refreshAddressTable();
            table.getSelectionModel().clearSelection();


        } catch (NumberFormatException ex) {
            showErrorAlert("Input Error", "Please enter valid numbers for Apartment ID and Floor (if provided).");
        } catch (Exception ex) {
            showDbErrorAlert("add/update address", ex, "Apartment ID might not exist.");
        }
    }

    private void deleteAddress(TableView<Address> table) {
        Address selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("address to delete based on selection");
            return;
        }
        int aptIdToDelete = selected.getApartmentId();
        if (!showConfirmationAlert("Delete Address", "Are you sure you want to delete address for apartment ID: " + aptIdToDelete + "?"))
            return;
        try {
            addressDao.delete(aptIdToDelete);
            refreshAddressTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete address", ex);
        }
    }

    private void refreshAddressTable() {
        try {
            addressList.setAll(addressDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh address data", e);
        }
    }

    private Tab createLeaseTerminationTab() {
        TableView<LeaseTermination> table = new TableView<>();
        setupLeaseTerminationTableColumns(table);
        leaseTerminationList = FXCollections.observableArrayList(leaseTerminationDao.findAll());
        table.setItems(leaseTerminationList);

        TextField agreementNumberField = new TextField();
        agreementNumberField.setPromptText("Agreement No (PK/FK)");
        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason");
        DatePicker terminationDatePicker = new DatePicker();
        terminationDatePicker.setPromptText("Termination Date");

        Button addBtn = new Button("Add/Update Termination");
        Button deleteBtn = new Button("Delete Termination");
        Button clearBtn = new Button("Clear Form");

        Control[] fields = {agreementNumberField, reasonField, terminationDatePicker};

        addBtn.setOnAction(e -> addOrUpdateLeaseTermination(table, fields));
        deleteBtn.setOnAction(e -> deleteLeaseTermination(table));
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                agreementNumberField.setText(newVal.getAgreementNumber());
                reasonField.setText(newVal.getReason());
                terminationDatePicker.setValue(newVal.getTerminationDate() != null ? newVal.getTerminationDate().toLocalDate() : null);
                agreementNumberField.setDisable(true);
            } else {
                clearFields(agreementNumberField, reasonField);
                terminationDatePicker.setValue(null);
                agreementNumberField.setDisable(false);
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(5);
        formGrid.add(new Label("Agreem. No (PK):"), 0, 0);
        formGrid.add(agreementNumberField, 1, 0);
        formGrid.add(new Label("Reason:"), 0, 1);
        formGrid.add(reasonField, 1, 1);
        formGrid.add(new Label("Termin. Date:"), 0, 2);
        formGrid.add(terminationDatePicker, 1, 2);

        HBox buttonBox = new HBox(10, addBtn, deleteBtn, clearBtn);
        VBox contentBox = new VBox(10, table, formGrid, buttonBox);
        contentBox.setPadding(new Insets(10));
        Tab tab = new Tab("Terminations");
        tab.setContent(contentBox);
        return tab;
    }

    private void setupLeaseTerminationTableColumns(TableView<LeaseTermination> table) {
        TableColumn<LeaseTermination, String> numberCol = new TableColumn<>("Agreement No");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("agreementNumber"));
        TableColumn<LeaseTermination, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        TableColumn<LeaseTermination, Date> dateCol = new TableColumn<>("Termination Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("terminationDate"));
        table.getColumns().setAll(numberCol, reasonCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addOrUpdateLeaseTermination(TableView<LeaseTermination> table, Control[] fields) {
        try {
            String agrNum = ((TextField) fields[0]).getText();
            String reason = ((TextField) fields[1]).getText();
            LocalDate localDate = ((DatePicker) fields[2]).getValue();

            if (agrNum.isEmpty()) {
                showErrorAlert("Input Error", "Agreement number cannot be empty.");
                return;
            }
            if (localDate == null) {
                showErrorAlert("Input Error", "Termination date must be selected.");
                return;
            }
            Date sqlDate = Date.valueOf(localDate);

            LeaseTermination term = new LeaseTermination(agrNum, reason, sqlDate);

            Optional<LeaseTermination> existing = leaseTerminationDao.findByAgreementNumber(agrNum);
            if (existing.isPresent()) {
                leaseTerminationDao.update(term);
                showInfoAlert("Success", "Termination record updated.");
            } else {
                leaseTerminationDao.add(term);
                showInfoAlert("Success", "Termination record added.");
            }

            refreshLeaseTerminationTable();
            table.getSelectionModel().clearSelection();


        } catch (Exception ex) {
            showDbErrorAlert("add/update lease termination", ex, "Agreement Number might not exist.");
        }
    }

    private void deleteLeaseTermination(TableView<LeaseTermination> table) {
        LeaseTermination selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showSelectionErrorAlert("termination record");
            return;
        }
        String agrNumToDelete = selected.getAgreementNumber();
        if (!showConfirmationAlert("Delete Termination Record", "Are you sure you want to delete termination for agreement: " + agrNumToDelete + "?"))
            return;
        try {
            leaseTerminationDao.delete(agrNumToDelete);
            refreshLeaseTerminationTable();
        } catch (Exception ex) {
            showDbErrorAlert("delete lease termination", ex);
        }
    }

    private void refreshLeaseTerminationTable() {
        try {
            leaseTerminationList.setAll(leaseTerminationDao.findAll());
        } catch (Exception e) {
            showDbErrorAlert("refresh termination data", e);
        }
    }



    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDbErrorAlert(String operation, Exception ex) {
        showDbErrorAlert(operation, ex, null);
    }

    private void showDbErrorAlert(String operation, Exception ex, String hint) {
        String msg = "Could not " + operation + ": " + ex.getMessage()
                + (hint != null ? "\nHint: " + hint : "");
        showErrorAlert("Database Error", msg);
        ex.printStackTrace();
    }

    private void showSelectionErrorAlert(String itemType) {
        showErrorAlert("Selection Error", "Please select a " + itemType + " from the table first.");
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            if (field != null) field.clear();
        }
    }

    private TextField createNumericTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        return tf;
    }

    private TextField createDecimalTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*[\\.,]?\\d*")) {
                String filtered = newValue.replaceAll("[^\\d\\.,]", "");
                if (filtered.indexOf('.') != filtered.lastIndexOf('.') || filtered.indexOf(',') != filtered.lastIndexOf(',') || (filtered.contains(".") && filtered.contains(","))) {
                    tf.setText(oldValue);
                } else {
                    tf.setText(filtered);
                }
            }
        });
        return tf;
    }

    @Override
    public void stop() {
        System.out.println("Closing application and database connection...");
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection:");
                e.printStackTrace();
            }
        }
    }
}