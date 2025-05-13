package rental.Organization;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganizationDAOImpl implements OrganizationDAO {
    private final Connection connection;

    public OrganizationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Organization> findAll() {
        List<Organization> list = new ArrayList<>();
        String sql = "SELECT * FROM rental.organization";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToOrganization(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Organization> findById(int organizationId) {
        String sql = "SELECT * FROM rental.organization WHERE organization_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToOrganization(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Organization organization) {
        String sql = "INSERT INTO rental.organization (website_url, organization_name, tax_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, organization.getWebsite());
            stmt.setString(2, organization.getName());
            stmt.setString(3, organization.getInn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Organization organization) {
        String sql = "UPDATE rental.organization SET website_url = ?, organization_name = ?, tax_id = ? WHERE organization_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, organization.getWebsite());
            stmt.setString(2, organization.getName());
            stmt.setString(3, organization.getInn());
            stmt.setInt(4, organization.getOrganizationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int organizationId) {
        String sql = "DELETE FROM rental.organization WHERE organization_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, organizationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Organization mapRowToOrganization(ResultSet rs) throws SQLException {
        return new Organization(
                rs.getInt("organization_id"),
                rs.getString("website_url"),
                rs.getString("organization_name"),
                rs.getString("tax_id")
        );
    }
}
