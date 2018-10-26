package sample;

import database.DBConnector;
import javafx.beans.property.LongProperty;
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
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CarServiceOrderController implements Initializable {

    @FXML
    private TableView<CarServiceOrder> carServiceOrderTableView;
    @FXML
    private TableColumn<CarServiceOrder, LongProperty> col_ordernumber;
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
    private ComboBox<Employee> employeeList;
    @FXML
    private TextField text_search;
    @FXML
    private TextField text_comments;
    @FXML
    private TextField text_quantity;

    // Track various things
    private ObservableList<CarServiceOrder> carServiceOrders = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<Car> cars = FXCollections.observableArrayList();
    private ObservableList<CarService> carServices = FXCollections.observableArrayList();
    private ObservableList<AutoPart> autoParts = FXCollections.observableArrayList();
    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    @Override
    /*
       Initializes the car database and populates the table and dropdown menu for customer selection on startup
       precondition: can only be accessed from a button on the Customer page gui
       postcondition: both the customer dropdown and car table are populated with the respective objects from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        // Establish observable lists
        carServiceOrders.clear();
        customerList.setItems(getCustomerList());
        carServiceList.setItems(getCarServiceList());
        employeeList.setItems(getEmployee());

        // Table View
        col_ordernumber.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, LongProperty>("pKey"));
        col_customer.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("Customer_ID"));
        col_car.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("Car_ID"));
        col_price.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, Double>("Price"));
        col_comments.setCellValueFactory(new PropertyValueFactory<CarServiceOrder, String>("Comments"));

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
                        rs.getString("Comments"),
                        rs.getLong("Employee_Key"));
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
    /*
     Connecting to database and reading Employee objects to populate ObservableList
     precondition: called by intialize
     postcondition:
  */
    private ObservableList<Employee> getEmployee() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Employee");

            while (rs.next()) {
                Employee newEmployee = new Employee(rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("number"),
                        rs.getString("email"), rs.getString("address"),
                        rs.getString("city"),rs.getString("zipcode"),
                        rs.getString("state"), rs.getString("payrate"));
                employees.add(newEmployee);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return employees;
    }

    // Select a customer from the drop down to filter car dropdown
    public void selectCustomer(){
        if (customerList.getSelectionModel().isEmpty()){

        }else{
            Customer selectedCustomer = customerList.getSelectionModel().getSelectedItem();
            String custID = selectedCustomer.getUserID();
            carList.setItems(getCarList(custID));
        }
    }

    // Select a Car Service from the drop down to filter Service Parts
    public void selectCarService(){
        if (carServiceList.getSelectionModel().isEmpty()){

        }else {
            CarService selectedService = carServiceList.getSelectionModel().getSelectedItem();
            String servID = selectedService.getService_ID();
            autopartList.setItems(getAutoPartList(servID));
        }
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

    /*
           Creates CarServiceOrder object and adds it to database and refreshes the table with the new CarServiceOrder
           added.
           precondition: user clicks add button and has entered the information in the proper formats in the
           proper fields
           postcondition: new CarServiceOrder is added to both the database and observable list which is used
           to display employees
        */
    public void addCarServiceOrder(ActionEvent actionEvent) {
        String customer_ID = customerList.getSelectionModel().getSelectedItem().getUserID();
        String car_ID = carList.getSelectionModel().getSelectedItem().getModel();
        String service_ID = carServiceList.getSelectionModel().getSelectedItem().getService_ID();
        String part_ID =
                (autopartList.getSelectionModel().getSelectedItem() == null) ?
                        "" : autopartList.getSelectionModel().getSelectedItem().getId();
        String comments = text_comments.getText();
        int index = employeeList.getSelectionModel().getSelectedIndex()+1;

        // Fetching employee_key
        long employee_key = 0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "Select * From Employee where Key = '" + index + "'";
            ResultSet rs = stat.executeQuery(sql);
            employee_key = rs.getInt("Key");
            con.close();

        }catch(Exception e){
            System.err.println(e);

        }
        double price;
        // calculate price
        int quantity = 1;
        quantity = Integer.parseInt(text_quantity.getText());

        if (autopartList.getSelectionModel().getSelectedItem() == null){
            price = carServiceList.getSelectionModel().getSelectedItem().getPrice();
        }
        else {
            Double partsPrice = Double.parseDouble(autopartList.getSelectionModel().getSelectedItem().getPrice()) * quantity;
            price = carServiceList.getSelectionModel().getSelectedItem().getPrice() + partsPrice;
        }
        System.out.println("Employee key = " + employee_key);
        CarServiceOrder cso = new CarServiceOrder(customer_ID, car_ID, service_ID, part_ID, price, comments, employee_key);
        String Order_ID = cso.getOrder_ID();
        carServiceOrders.add(cso);
        String sql =
                "INSERT INTO CarServiceOrder(Customer_ID,Car_ID,Service_ID,Part_ID,Price,Comments,Employee_Key,Order_ID) VALUES(?,?,?,?,?,?,?,?)";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, customer_ID);
            preparedStatement.setString(2, car_ID);
            preparedStatement.setString(3, service_ID);
            preparedStatement.setString(4, part_ID);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, comments);
            preparedStatement.setString(7, Long.toString(employee_key));
            preparedStatement.setString(8, Order_ID);
            preparedStatement.execute();
            refreshTable();
            //addedPopup();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
    Updates the car service order the user currently has selected on the table with the new information in the text fields
    precondition: a car service order on the table has been selected and the user entered information in the proper format in the text fields
    postcondition: the selected car service order is updated in both table and database
 */
    public void updateCarServiceOrder(ActionEvent actionEvent) {
        CarServiceOrder clickedCSO = carServiceOrderTableView.getSelectionModel().getSelectedItem();
        int clickedCSOindex = carServiceOrderTableView.getSelectionModel().getSelectedIndex();
        String customer_ID = customerList.getSelectionModel().getSelectedItem().getUserID();
        String car_ID = carList.getSelectionModel().getSelectedItem().getModel();
        String service_ID = carServiceList.getSelectionModel().getSelectedItem().getService_ID();
        String part_ID = autopartList.getSelectionModel().getSelectedItem().getId();


        // calculate price
        double price;
        int quantity = 1;
        quantity = Integer.parseInt(text_quantity.getText());

        if (autopartList.getSelectionModel().getSelectedItem() == null){
            price = carServiceList.getSelectionModel().getSelectedItem().getPrice();
        }
        else {
            Double partsPrice = Double.parseDouble(autopartList.getSelectionModel().getSelectedItem().getPrice()) * quantity;
            price = carServiceList.getSelectionModel().getSelectedItem().getPrice() + partsPrice;
        }
        String Order_ID = clickedCSO.getOrder_ID();
        String comments = text_comments.getText();


        // Fetching employee_key
        long employee_key = 0;
        int index  = employeeList.getSelectionModel().getSelectedIndex()+1;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "Select * From Employee where Key = '" + index + "'";
            ResultSet rs = stat.executeQuery(sql);
            employee_key = rs.getInt("Key");
            con.close();

        }catch(Exception e){
            System.err.println(e);
        }

        CarServiceOrder cso = new CarServiceOrder(customer_ID, car_ID, service_ID, part_ID, price, comments, employee_key);
        String sql =
                "UPDATE CarServiceOrder SET Customer_ID =?, Car_ID=?, Service_ID=?, Part_ID=?, Price=?, Comments=?, Employee_Key=?, Order_ID = ? where Order_ID ='" + clickedCSO.getOrder_ID() + "'";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, customer_ID);
            preparedStatement.setString(2, car_ID);
            preparedStatement.setString(3, service_ID);
            preparedStatement.setString(4, part_ID);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, comments);
            preparedStatement.setString(7, Long.toString(employee_key));
            preparedStatement.setString(8, Order_ID);
            preparedStatement.execute();
            System.out.println("Update Successful");
            carServiceOrders.set(clickedCSOindex, cso);
            refreshTable();
            //addedPopup();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }




    }

    public void deleteCarServiceOrder(ActionEvent actionEvent){
        CarServiceOrder clickedCSO = carServiceOrderTableView.getSelectionModel().getSelectedItem();
        String sql = "DELETE FROM CarServiceOrder where Order_ID = '" + clickedCSO.getOrder_ID() + "'";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            customerList.valueProperty().set(null);
            carList.valueProperty().set(null);
            carServiceList.valueProperty().set(null);
            employeeList.valueProperty().set(null);
            autopartList.valueProperty().set(null);
            cars.clear();
            autoParts.clear();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
   Refreshes the table by rereading from the database when called
   precondition: method has to be called by other methods like deleteCarServiceOrder(), addCarServiceOrder(), or updateCarServiceOrder()
   postcondition: the table on the GUI is refreshed with the information about employees read from the database
*/
    public void refreshTable() {
        try {
            carServiceOrders.clear();
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
                        rs.getString("Comments"),
                        rs.getLong("Employee_Key"));
                carServiceOrders.add(newOrder);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }



    /*
       Sets the information of the car to the text boxes on the left menu when the user clicks a car in the list
       precondition: proper car has been stored and user double clicks on the car
       postcondition: text fields on the left of the main window have been populated with the car's information
    */
    public void clickCarServiceOrder(MouseEvent actionEvent){
        int indexCustomer;
        int indexCarSerive;
        if(actionEvent.getClickCount() == 1){
            if (carServiceOrderTableView.getSelectionModel().isEmpty()){
            }else {
                CarServiceOrder clickedCSO = carServiceOrderTableView.getSelectionModel().getSelectedItem();
                for (int i = 0; i<customers.size(); i++){
                    if (customers.get(i).getUserID().equals(clickedCSO.getCustomer_ID())){
                        customerList.getSelectionModel().select(customers.get(i));
                    }
                }
                for (int i = 0; i<cars.size(); i++){
                    if (cars.get(i).getModel().contains(clickedCSO.getCar_ID())){
                        carList.getSelectionModel().select(cars.get(i));
                    }
                }
                for (int i = 0; i<carServices.size(); i++){
                    if (carServices.get(i).getService_Name().equals(clickedCSO.getService_ID())){
                        carServiceList.getSelectionModel().select(carServices.get(i));
                    }
                }

                long index = clickedCSO.getEmployee_Key();
                System.out.println((int)index);
                employeeList.getSelectionModel().select((int)index - 1);

                for (int i = 0; i<autoParts.size(); i++){
                    if (autoParts.get(i).getId().equals(clickedCSO.getPart_ID())){
                        autopartList.getSelectionModel().select(autoParts.get(i));
                    }
                }
                text_comments.setText(clickedCSO.getComments());

                for (int i = 0; i < customers.size(); i++) {
                    Customer myCustomer = customers.get(i);
                    if (clickedCSO.getCustomer_ID().equals(myCustomer.getUserID())) {
                        indexCustomer = i;
                        customerList.getSelectionModel().select(customers.get(indexCustomer));
                    }
                }

            }
        }
    }

}