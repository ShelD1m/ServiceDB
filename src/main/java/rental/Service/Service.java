package rental.Service;


import java.math.BigDecimal;

public class Service {
    private int serviceCode;
    private String name;
    private BigDecimal cost;
    private String serviceType;

    public Service(int serviceCode, String name, BigDecimal cost, String serviceType) {
        this.serviceCode = serviceCode;
        this.name = name;
        this.cost = cost;
        this.serviceType = serviceType;
    }

    public int getServiceCode() { return serviceCode; }
    public void setServiceCode(int serviceCode) { this.serviceCode = serviceCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
}
