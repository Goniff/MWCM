package sample;

import java.util.Objects;

public class CarService {

    private String service_ID;
    private String service_Name;
    private double price;

    public CarService(String serviceID, String serviceName, double price){
        this.service_ID = serviceID;
        this.service_Name = serviceName;
        this.price = price;
    }

    public String getService_ID() {
        return service_ID;
    }

    public void setService_ID(String service_ID) {
        this.service_ID = service_ID;
    }

    public String getService_Name() {
        return service_Name;
    }

    public void setService_Name(String service_Name) {
        this.service_Name = service_Name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return service_Name + " \n$" + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarService carService = (CarService) o;
        return Objects.equals(getService_ID(), carService.getService_ID()) &&
                Objects.equals(getService_Name(), carService.getService_Name()) &&
                Objects.equals(getPrice(), carService.getPrice());
    }
}
