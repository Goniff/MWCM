package sample;

import database.DBConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CarServiceOrderController implements Initializable {

    @FXML
    private TableView<CarServiceOrder> carServiceOrderTableView;
    @FXML
    private TableColumn<CarServiceOrder, Long> col_ordernumber;
    @FXML
    private TableColumn<CarServiceOrder, String> col_customer;
    @FXML
    private TableColumn<CarServiceOrder, String> col_car;
    @FXML
    private TableColumn<CarServiceOrder, Double> col_price;
    @FXML
    private TableColumn<CarServiceOrder, String> col_comments;

    @FXML
    private ComboBox<Customer> customerList = new ComboBox<>();
    @FXML
    private ComboBox<Car> carList;
    @FXML
    private ComboBox<CarService> carServiceList;
    @FXML
    private ComboBox<AutoPart> autopartList;
    @FXML
    private TextField text_search;

    // Track various things
    private ObservableList<CarServiceOrder> carServiceOrders = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<Car> cars = FXCollections.observableArrayList();
    private ObservableList<Car> allCars = FXCollections.observableArrayList();
    private ObservableList<CarService> carServices = FXCollections.observableArrayList();
    private ObservableList<AutoPart> autoParts = FXCollections.observableArrayList();

    @Override
    /*
       Initializes the car database and populates the table and dropdown menu for customer selection on startup
       precondition: can only be accessed from a button on the Customer page gui
       postcondition: both the customer dropdown and car table are populated with the respective objects from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        // Establish observable lists
        customerList.setItems(getCustomerList());
        carServiceList.setItems(getCarServiceList());


        // Table View
        col_ordernumber.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, Long>("pKey"));
        col_customer.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("customer"));
        col_car.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("car"));
        col_price.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, Double>("price"));
        col_comments.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("comments"));
        carServiceOrderTableView.setItems(getCarServiceOrderList());

    }

    /*
       Reads CarService objects from the database and adds them to the observable list of CarService
       precondition: a proper list of CarService objects must be declared and method must be called through
       initialize
       postcondition: returns an ObservableList of CarService objects
    */
    private ObservableList<CarService> getCarServiceList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarService");

            while (rs.next()) {
                CarService newService = new CarService(
                        rs.getString("Service_ID"),
                        rs.getString("Service_Name"),
                        rs.getDouble("Price"));
                carServices.add(newService);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return carServices;
    }

    /*
       Reads CarServiceOrder objects from the database and adds them to the observable list of CarServiceOrder
       precondition: a proper list of CarServiceOrder objects must be declared and method must be called through
       initialize
       postcondition: returns an ObservableList of CarServiceOrder objects
    */
    private ObservableList<CarServiceOrder> getCarServiceOrderList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarServiceOrder");

            while (rs.next()) {
                CarServiceOrder newOrder = new CarServiceOrder(
                        rs.getLong("Key"),
                        rs.getString("Customer_ID"),
                        rs.getString("Car_ID"),
                        rs.getString("Service_ID"),
                        rs.getString("Part_ID"),
                        rs.getDouble("Price"),
                        rs.getString("Comments"));
                carServiceOrders.add(newOrder);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return carServiceOrders;
    }

    /*
       Reads customer objects from the database and adds them to the observable list of customers
       precondition: a proper list of customer objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of Customer objects
    */
    private ObservableList<Customer> getCustomerList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Customer");

            while (rs.next()) {
                Customer newCustomer = new Customer(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zipcode"),
                        rs.getString("state"));
                customers.add(newCustomer);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return customers;
    }

    /*
       Reads car objects from the database and adds them to the observable list of car objects
       precondition: a proper list of car objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of car objects
    */
    private ObservableList<Car> getCarList(String custID) {
        try {
            cars.clear();
            Connection con = DBConnector.getConnection();
            PreparedStatement statement = con.prepareStatement("select * from Car where userID = ?");
            statement.setString(1,custID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Car newCar = new Car(
                        rs.getString("userID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("comments"));
                System.out.println(newCar.getUserID());
                cars.add(newCar);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return cars;
    }

    /*
       Reads Auto objects from the database and adds them to the observable list of car objects
       precondition: a proper list of car objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of car objects
    */
    private ObservableList<AutoPart> getAutoPartList(String servID) {
        try {
            autoParts.clear();
            Connection con = DBConnector.getConnection();
            PreparedStatement statement = con.prepareStatement("select * from Inventory where category = ?");
            statement.setString(1,servID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                AutoPart newPart = new AutoPart(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("quantity"),
                        rs.getString("price"),
                        rs.getString("category"));
                System.out.println(newPart.getName());
                autoParts.add(newPart);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return autoParts;
    }

    // Select a customer from the drop down to filter car dropdown
    public void selectCustomer(ActionEvent actionEvent){
        Customer selectedCustomer = customerList.getSelectionModel().getSelectedItem();
        String custID = selectedCustomer.getUserID();
        carList.setItems(getCarList(custID));


    }

    // Select a Car Service from the drop down to filter Service Parts
    public void selectCarService(){
        carServiceList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<CarService>() {
                    @Override
                    public void changed(ObservableValue<? extends CarService> observable, CarService oldValue, CarService newValue) {
                        if (newValue != null){
                            String servID = newValue.getService_ID();
                            autopartList.setItems(getAutoPartList(servID));
                        }
                    }
                }
        );
    }

    /*
       Filters the Customer table based on what string input the user types into the bar (a search function)
       precondition: there must be objects for the search to filter through and the user must enter a proper string
       postcondition: the table will list only the car that have strings matching what the user entered in the search bar
    */
    FilteredList filter = new FilteredList(carServiceOrders, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    filter.setPredicate((Predicate<? super CarServiceOrder>) cso -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        } else {
                            String lowerCaseFilter = newValue.toLowerCase();
                            if (String.valueOf(cso.getpKey()).toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (cso.getCustomer_ID().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (cso.getCar_ID().contains(lowerCaseFilter)) {
                                return true;
                            } else if (String.valueOf(cso.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (cso.getComments().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        }
                    });
                    SortedList<CarServiceOrder> sortedDatabase = new SortedList<>(filter);
                    sortedDatabase.comparatorProperty().bind(carServiceOrderTableView.comparatorProperty());
                    carServiceOrderTableView.setItems(sortedDatabase);
                }));
    }
}