package rental.Landlord;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LandlordDAOImpl implements LandlordDAO {
    private final Connection connection;

    public LandlordDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Landlord> findAll() {
        List<Landlord> list = new ArrayList<>();
        String sql = "SELECT landlord_id, passport_number, full_name, phone_number FROM rental.landlord";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Landlord(
                        rs.getInt("landlord_id"),
                        rs.getString("passport_number"),
                        rs.getString("full_name"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(Landlord landlord) {
               String sql = "INSERT INTO rental.landlord (passport_number, full_name, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, landlord.getPassportSeriesNumber());
            stmt.setString(2, landlord.getFullName());
            stmt.setString(3, landlord.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Landlord landlord) {
        String sql = "UPDATE rental.landlord SET passport_number = ?, full_name = ?, phone_number = ? WHERE landlord_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, landlord.getPassportSeriesNumber());
            stmt.setString(2, landlord.getFullName());
            stmt.setString(3, landlord.getPhoneNumber());
            stmt.setInt(4, landlord.getLandlordId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int landlordId) {
        String sql = "DELETE FROM rental.landlord WHERE landlord_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, landlordId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
