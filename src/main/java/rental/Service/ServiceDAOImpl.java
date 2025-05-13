package rental.Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;


public class ServiceDAOImpl implements ServiceDAO {
    private final Connection connection;

    public ServiceDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Service> findAll() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM rental.service";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Service> findByCode(int serviceCode) {
        String sql = "SELECT * FROM rental.service WHERE service_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToService(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Service service) {
        String sql = "INSERT INTO rental.service (service_code, service_name, service_cost, service_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, service.getServiceCode());
            stmt.setString(2, service.getName());
            stmt.setBigDecimal(3, service.getCost());
            stmt.setString(4, service.getServiceType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Service service) {
        String sql = "UPDATE rental.service SET service_name = ?, service_cost = ?, service_type = ? WHERE service_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, service.getName());
            stmt.setBigDecimal(2, service.getCost());
            stmt.setString(3, service.getServiceType());
            stmt.setInt(4, service.getServiceCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int serviceCode) {
        String sql = "DELETE FROM rental.service WHERE service_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Service mapRowToService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_code"),
                rs.getString("service_name"),
                rs.getBigDecimal("service_cost"),
                rs.getString("service_type")
        );
    }
}
