package sample;

public class CarServiceOrder {

    private long pKey;
    private String Customer_ID;
    private String Car_ID;
    private String Service_ID;
    private String Part_ID;
    private double Price;
    private String Comments = "";

    public CarServiceOrder(String cust_id, String car_id, String service_id, String part_ID, double price,
                           String comments){
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
    }

    public CarServiceOrder(long pkey, String cust_id, String car_id, String service_id, String part_ID, double price,
                           String comments){
        this.pKey = pkey;
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
    }


    public long getpKey() {
        return pKey;
    }

    public void setpKey(long pKey) {
        this.pKey = pKey;
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
}
