package sample;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class CarServiceOrder {

    private LongProperty pKey;
    private LongProperty Employee_Key;
    private String Customer_ID;
    private String Car_ID;
    private String Service_ID;
    private String Part_ID;
    private double Price;
    private String Comments = "";
    private String Order_ID;


    public CarServiceOrder(String cust_id, String car_id, String service_id, String part_ID, double price,
                           String comments, long emp_key){
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
        this.Employee_Key = new SimpleLongProperty(emp_key);
        Order_ID = cust_id + service_id + car_id;
    }

    public CarServiceOrder(long pkey, String cust_id, String car_id, String service_id, String part_ID, double price,
                           String comments, long emp_key){
        this.pKey = new SimpleLongProperty(pkey);
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
        this.Employee_Key = new SimpleLongProperty(emp_key);
        Order_ID = cust_id + service_id + car_id;
    }

    public final LongProperty getpKey() {
        return pKey;
    }

    public final void setpKey(long pKey) {
        this.pKey.set(pKey);
    }

    public LongProperty pKeyProperty(){
        return pKey;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCar_ID() {
        return Car_ID;
    }

    public void setCar_ID(String car_ID) {
        Car_ID = car_ID;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }

    public String getPart_ID() {
        return Part_ID;
    }

    public void setPart_ID(String part_ID) {
        Part_ID = part_ID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public final long getEmployee_Key() {
        return Employee_Key.get();
    }

    public LongProperty employee_KeyProperty() {
        return Employee_Key;
    }

    public final void setEmployee_Key(long employee_Key) {
        this.Employee_Key.set(employee_Key);
    }

    public String getOrder_ID() { return Order_ID; }
}

