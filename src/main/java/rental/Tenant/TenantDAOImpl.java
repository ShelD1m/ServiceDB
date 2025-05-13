package rental.Tenant;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TenantDAOImpl implements TenantDAO {
    private final Connection connection;

    public TenantDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Tenant> findAll() {
        List<Tenant> list = new ArrayList<>();
        String sql = "SELECT tenant_id, passport_number, phone_number, full_name FROM rental.tenant";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Tenant(
                        rs.getInt("tenant_id"),
                        rs.getString("passport_number"),
                        rs.getString("phone_number"),
                        rs.getString("full_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(Tenant tenant) {
        String sql = "INSERT INTO rental.tenant (passport_number, phone_number, full_name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tenant.getPassportSeriesNumber());
            stmt.setString(2, tenant.getPhoneNumber());
            stmt.setString(3, tenant.getFullName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tenant tenant) {
        String sql = "UPDATE rental.tenant SET passport_number = ?, phone_number = ?, full_name = ? WHERE tenant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tenant.getPassportSeriesNumber());
            stmt.setString(2, tenant.getPhoneNumber());
            stmt.setString(3, tenant.getFullName());
            stmt.setInt(4, tenant.getTenantId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int tenantId) {
        String sql = "DELETE FROM rental.tenant WHERE tenant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tenantId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
