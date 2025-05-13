package rental.Apartment;

public class Apartment {
    private int apartmentId;
    private int landlordId;
    private int roomCount;
    private double squareMeters;

    public Apartment(int apartmentId, int landlordId, int roomCount, double squareMeters) {
        this.apartmentId = apartmentId;
        this.landlordId = landlordId;
        this.roomCount = roomCount;
        this.squareMeters = squareMeters;
    }

    public int getApartmentId() { return apartmentId; }
    public void setApartmentId(int apartmentId) { this.apartmentId = apartmentId; }

    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public int getRoomCount() { return roomCount; }
    public void setRoomCount(int roomCount) { this.roomCount = roomCount; }

    public double getSquareMeters() { return squareMeters; }
    public void setSquareMeters(double squareMeters) { this.squareMeters = squareMeters; }
}