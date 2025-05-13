package rental.Address;


import rental.Payment.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressDAOImpl implements AddressDAO {
    private final Connection connection;

    public AddressDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Address> findAll() {
        List<Address> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rental.address")) {
            while (rs.next()) {
                list.add(new Address(
                        rs.getInt("address_id"),
                        rs.getInt("apartment_id"),
                        rs.getString("street_name"),
                        rs.getString("house_number"),
                        rs.getInt("floor_number"),
                        rs.getString("apartment_number"),
                        rs.getString("region_name"),
                        rs.getString("city_name")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Address> findByApartmentId(int apartmentId) {
        String sql = "SELECT * FROM rental.address WHERE apartment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, apartmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToAddress(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Address address) {
        // apartment_id (PK/FK) должен быть предоставлен
        String sql = "INSERT INTO rental.address (apartment_id, street, house_number, floor, apartment_number, region, city) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, address.getApartmentId());
            stmt.setString(2, address.getStreet());
            stmt.setString(3, address.getHouseNumber());
            // Обработка возможного NULL для этажа
            if (address.getFloor() != null) {
                stmt.setInt(4, address.getFloor());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, address.getApartmentNumber());
            stmt.setString(6, address.getRegion());
            stmt.setString(7, address.getCity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Address address) {
        String sql = "UPDATE rental.address SET street = ?, house_number = ?, floor = ?, apartment_number = ?, region = ?, city = ? WHERE apartment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getHouseNumber());
            if (address.getFloor() != null) {
                stmt.setInt(3, address.getFloor());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, address.getApartmentNumber());
            stmt.setString(5, address.getRegion());
            stmt.setString(6, address.getCity());
            stmt.setInt(7, address.getApartmentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int apartmentId) {
        String sql = "DELETE FROM rental.address WHERE apartment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, apartmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Address mapRowToAddress(ResultSet rs) throws SQLException {
        Integer floor = rs.getInt("floor");
        if (rs.wasNull()) {
            floor = null;
        }
        return new Address(
                rs.getInt("address_id"),
                rs.getInt("apartment_id"),
                rs.getString("street_name"),
                rs.getString("house_number"),
                rs.getInt("floor_number"),
                rs.getString("apartment_number"),
                rs.getString("region_name"),
                rs.getString("city_name")
        );
    }
}
