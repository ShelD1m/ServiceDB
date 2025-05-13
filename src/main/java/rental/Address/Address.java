package rental.Address;


public class Address {
    private int apartmentId;
    private String street;
    private String houseNumber;
    private Integer floor;
    private String apartmentNumber;
    private String region;
    private String city;

    public Address(int addressId, int apartmentId, String streetName, String houseNumber, int floorNumber, String apartmentNumber, String regionName, String cityName) {
        this.apartmentId = apartmentId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.region = region;
        this.city = city;
    }


    public int getApartmentId() { return apartmentId; }
    public void setApartmentId(int apartmentId) { this.apartmentId = apartmentId; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
