package sample;

import database.DBConnector;
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
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class InventoryController implements Initializable {

    @FXML
    private TableView<AutoPart> tableView;
    @FXML
    private TableColumn<AutoPart, String> col_id;
    @FXML
    private TableColumn<AutoPart, String> col_name;
    @FXML
    private TableColumn<AutoPart, String> col_category;
    @FXML
    private TableColumn<AutoPart, String> col_qty;
    @FXML
    private TableColumn<AutoPart, String> col_price;

    @FXML
    private TextField text_id;
    @FXML
    private TextField text_name;
    @FXML
    private TextField text_qty;
    @FXML
    private TextField text_price;
    @FXML
    private ComboBox<String> category_cb;
    @FXML
    private TextField text_search;

    static ObservableList<AutoPart> parts = FXCollections.observableArrayList();
    private ObservableList<String> category_list = FXCollections.observableArrayList();
    private String[] categories = {"Accessory","Tire","Engine"};




    public void initialize(URL location, ResourceBundle resources) {
        parts.clear();
        col_id.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("name"));
        col_qty.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("price"));
        col_category.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("category"));

        tableView.setItems(getInventory());
        category_cb.setItems(getCategories());

    }

    public ObservableList<AutoPart> getInventory(){
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Inventory");

            while (rs.next()) {
                AutoPart autoPart = new AutoPart(rs.getString("id"),
                        rs.getString("name"), rs.getString("quantity"),
                        rs.getString("price"), rs.getString("category"));
                parts.add(autoPart);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return parts;
    }

    public ObservableList<String> getCategories(){
        for (int i = 0; i<categories.length; i++){
            category_list.add(categories[i]);
        }
        return category_list;
    }
    /*
       Creates AutoPart object and adds it to database and refreshes the table with the new AutoPart added
       precondition: user clicks add button and has entered the information in the proper formats in the proper fields
       postcondition: new AutoPart is added to both the database and observable list which is used to display AutoPart
    */
    public void addPart(ActionEvent actionEvent) {
        String id = text_id.getText();
        String name = text_name.getText();
        String quantity = text_qty.getText();
        String price = text_price.getText();
        String category = category_cb.getSelectionModel().getSelectedItem();


        if (id.length() == 0 || name.length() == 0 || quantity.length() == 0 || price.length() == 0
                || category.length() == 0)  {
            System.out.println("One or more fields encountered and error");
        } else {
            AutoPart autoPart = new AutoPart(id, name, quantity, price, category);
            parts.add(autoPart);
            String sql = "INSERT INTO Inventory(id,name,quantity,price,category)  VALUES(?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, quantity);
                preparedStatement.setString(4, price);
                preparedStatement.setString(5, category);
                preparedStatement.execute();
                System.out.println("Part successfully added");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    /*
   Updates the employee the user currently has selected on the table with the new information in the text fields
   precondition: a employee on the table has been selected and the user entered information in the proper format in the text fields
   postcondition: the selected employee is updated in both table and database
*/
    public void updatePart(ActionEvent actionEvent) {
        AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        String id = text_id.getText();
        String name = text_name.getText();
        String quantity = text_qty.getText();
        String price = text_price.getText();
        String category = category_cb.getSelectionModel().getSelectedItem();

        if (id.length() == 0 || name.length() == 0 || quantity.length() == 0 || price.length() == 0
                || category.length() == 0)  {
            System.out.println("One or more fields encountered and error");
        } else {
            String sql = "UPDATE Inventory SET id=?, name=?, quantity=?, price=?, category=? where id ='" + clickedPart.getId() + "'";
            System.out.println(sql);
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, quantity);
                preparedStatement.setString(4, price);
                preparedStatement.setString(5, category);
                preparedStatement.execute();
                System.out.println("Update Successful");
                parts.set(index, clickedPart);
                refreshTable();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    /*
   Sets the information of the AutoPart to the text boxes on the left menu when the user clicks a part in the list
   precondition: proper part has been stored and user double clicks on the employee
   postcondition: text fields on the left of the main window have been populated with the part's information
*/
    public void clickPart(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();

            text_id.setText(clickedPart.getId());
            text_name.setText(clickedPart.getName());
            text_qty.setText(clickedPart.getQuantity());
            text_price.setText(clickedPart.getPrice());
            for (int i = 0; i<category_list.size(); i++){
                if(clickedPart.getCategory().equals(category_list.get(i))){
                    category_cb.getSelectionModel().select(categories[i]);
                }
            }
        }
    }
    /*
   Deletes the AutoPart the user has currently selected in both the database and the table
   precondition: proper AutoPart has been stored and user clicks on the AutoPart
   postcondition: the selected AutoPart object is deleted from both the table and the database
*/
    public void deletePart(ActionEvent actionEvent) {
        AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();
        String sql = "DELETE FROM Inventory where id = '" + clickedPart.getId() + "'";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_id.clear();
            text_name.clear();
            text_qty.clear();
            text_price.clear();
            category_cb.valueProperty().set(null);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
   Refreshes the table by rereading from the database when called
   precondition: method has to be called by other methods like deletePart(), addPart(), or updatePart()
   postcondition: the table on the GUI is refreshed with the information about AutoParts read from the database
*/
    public void refreshTable() {
        try {
            parts.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Inventory");
            while (rs.next()) {
                AutoPart newPart = new AutoPart(rs.getString("id"),
                        rs.getString("name"), rs.getString("quantity"),
                        rs.getString("price"), rs.getString("category"));
                parts.add(newPart);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
   Filters the Employee table based on what string input the user types into the bar (a search function)
   precondition: there must be objects for the search to filter through and the user must enter a proper string
   postcondition: the table will list only the employees that have strings matching what the user entered in the search bar
*/
    FilteredList filter = new FilteredList(parts, e->true);
    public void searchPart(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super AutoPart>) part -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (part.getId().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getQuantity().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getPrice().contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<AutoPart> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }


}
