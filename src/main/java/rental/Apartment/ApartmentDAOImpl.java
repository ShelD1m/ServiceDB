package rental.Apartment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApartmentDAOImpl implements ApartmentDAO {
    private final Connection connection;

    public ApartmentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public List<Apartment> findAll() {
        List<Apartment> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rental.apartment")) {
            while (rs.next()) {
                list.add(new Apartment(
                    rs.getInt("apartment_id"),
                    rs.getInt("landlord_id"),
                    rs.getInt("room_count"),
                    rs.getDouble("square_meters")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(Apartment apartment) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO rental.apartment (landlord_id, room_count, square_meters) VALUES (?, ?, ?)")) {
            stmt.setInt(1, apartment.getLandlordId());
            stmt.setInt(2, apartment.getRoomCount());
            stmt.setDouble(3, apartment.getSquareMeters());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Apartment apartment) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE rental.apartment SET landlord_id = ?, room_count = ?, square_meters = ? WHERE apartment_id = ?")) {
            stmt.setInt(1, apartment.getLandlordId());
            stmt.setInt(2, apartment.getRoomCount());
            stmt.setDouble(3, apartment.getSquareMeters());
            stmt.setInt(4, apartment.getApartmentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int apartmentId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM rental.apartment WHERE apartment_id = ?")) {
            stmt.setInt(1, apartmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}