package rental.LeaseTermination;


import java.sql.*;
import java.util.Optional;

public class LeaseTerminationDAOImpl implements LeaseTerminationDAO {
    private final Connection connection;

    public LeaseTerminationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<LeaseTermination> findByAgreementNumber(String agreementNumber) {
        String sql = "SELECT * FROM rental.contract_termination WHERE termination_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new LeaseTermination(
                        rs.getString("termination_id"),
                        rs.getString("termination_reason"),
                        rs.getDate("termination_date")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(LeaseTermination termination) {
        String sql = "INSERT INTO rental.contract_termination (termination_id, termination_reason, termination_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, termination.getAgreementNumber());
            stmt.setString(2, termination.getReason());
            stmt.setDate(3, termination.getTerminationDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LeaseTermination termination) {
        String sql = "UPDATE rental.contract_termination SET termination_reason = ?, termination_date = ? WHERE termination_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, termination.getReason());
            stmt.setDate(2, termination.getTerminationDate());
            stmt.setString(3, termination.getAgreementNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String agreementNumber) {
        String sql = "DELETE FROM rental.contract_termination WHERE termination_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LeaseTermination findAll() {
        return null;
    }
}
