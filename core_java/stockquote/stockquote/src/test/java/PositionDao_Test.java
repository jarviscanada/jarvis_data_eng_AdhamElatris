import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.model.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PositionDao_Test {

  @Mock
  private Connection mockConnection;

  @Mock
  private PreparedStatement mockCheckStmt;

  @Mock
  private PreparedStatement mockInsertStmt;

  @Mock
  private PreparedStatement mockFindByIdStmt;

  @Mock
  private PreparedStatement mockFindAllStmt;

  @Mock
  private PreparedStatement mockDeleteStmt;

  @Mock
  private Statement mockDeleteAllStmt;

  @Mock
  private ResultSet mockResultSet;

  private PositionDao positionDao;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    positionDao = new PositionDao(mockConnection);
  }

  @Test
  public void testSave_existingQuote() throws Exception {
    // Given: a position with a ticker that exists in the quote table
    Position position = new Position();
    position.setTicker("AAPL");
    position.setNumOfShares(10);

    // Simulate the Quote table check
    String checkSql = "SELECT * FROM quote WHERE symbol = ?";
    when(mockConnection.prepareStatement(checkSql)).thenReturn(mockCheckStmt);
    when(mockCheckStmt.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getDouble("price")).thenReturn(150.0);

    // Simulate the INSERT statement
    String insertSql = "INSERT INTO position (symbol, number_of_shares, value_paid)"
        + " VALUES (?, ?, ?)"
        + "ON CONFLICT (symbol) DO UPDATE SET number_of_shares = ?, value_paid = ?";
    when(mockConnection.prepareStatement(insertSql)).thenReturn(mockInsertStmt);
    when(mockInsertStmt.executeUpdate()).thenReturn(1);

    // When
    Position result = positionDao.save(position);

    // Then
    assertNotNull(result);
    assertEquals("AAPL", result.getTicker());

    verify(mockCheckStmt).setString(1, "AAPL");

    verify(mockInsertStmt).setString(1, "AAPL");
    verify(mockInsertStmt).setInt(2, 10);
    verify(mockInsertStmt).setDouble(3, 150.0 * 10);
    verify(mockInsertStmt).setInt(4, 10);
    verify(mockInsertStmt).setDouble(5, 150.0 * 10);
  }

  @Test
  public void testSave_nonExistingQuote() throws Exception {
    // Given: a position with a ticker that does not exist in the quote table
    Position position = new Position();
    position.setTicker("GOOG");
    position.setNumOfShares(5);

    String checkSql = "SELECT * FROM quote WHERE symbol = ?";
    when(mockConnection.prepareStatement(checkSql)).thenReturn(mockCheckStmt);
    when(mockCheckStmt.executeQuery()).thenReturn(mockResultSet);
    // Simulate that no quote exists
    when(mockResultSet.next()).thenReturn(false);

    // When
    Position result = positionDao.save(position);

    // Then
    assertNull(result);
    verify(mockCheckStmt).setString(1, "GOOG");
  }

  @Test
  public void testFindById_found() throws Exception {
    // Given: the symbol exists in the position table
    String findSql = "SELECT * FROM position WHERE symbol = ?";
    when(mockConnection.prepareStatement(findSql)).thenReturn(mockFindByIdStmt);
    when(mockFindByIdStmt.executeQuery()).thenReturn(mockResultSet);

    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getString("symbol")).thenReturn("MSFT");
    when(mockResultSet.getInt("number_of_shares")).thenReturn(20);
    when(mockResultSet.getDouble("value_paid")).thenReturn(3000.0);

    // When
    Optional<Position> result = positionDao.findById("MSFT");

    // Then
    assertTrue(result.isPresent());
    Position pos = result.get();
    assertEquals("MSFT", pos.getTicker());
    assertEquals(20, pos.getNumOfShares());
    assertEquals(3000.0, pos.getValuePaid(), 0.001);

    verify(mockFindByIdStmt).setString(1, "MSFT");
  }

  @Test
  public void testFindById_notFound() throws Exception {
    // Given: the symbol does not exist in the position table
    String findSql = "SELECT * FROM position WHERE symbol = ?";
    when(mockConnection.prepareStatement(findSql)).thenReturn(mockFindByIdStmt);
    when(mockFindByIdStmt.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(false);

    // When
    Optional<Position> result = positionDao.findById("NFLX");

    // Then
    assertFalse(result.isPresent());
    verify(mockFindByIdStmt).setString(1, "NFLX");
  }

  @Test
  public void testFindAll() throws Exception {
    // Given: the position table returns two rows.
    String findAllSql = "SELECT * FROM position";
    when(mockConnection.prepareStatement(findAllSql)).thenReturn(mockFindAllStmt);
    when(mockFindAllStmt.executeQuery()).thenReturn(mockResultSet);

    when(mockResultSet.next()).thenReturn(true, true, false);

    when(mockResultSet.getString("symbol")).thenReturn("AAPL", "GOOGL");
    when(mockResultSet.getInt("number_of_shares")).thenReturn(10, 5);
    when(mockResultSet.getDouble("value_paid")).thenReturn(1500.0, 2500.0);

    // When
    Iterable<Position> positions = positionDao.findAll();
    List<Position> posList = new ArrayList<>();
    positions.forEach(posList::add);

    // Then
    assertEquals(2, posList.size());

    // Validate the first row.
    Position pos1 = posList.get(0);
    assertEquals("AAPL", pos1.getTicker());
    assertEquals(10, pos1.getNumOfShares());
    assertEquals(1500.0, pos1.getValuePaid(), 0.001);

    // Validate the second row.
    Position pos2 = posList.get(1);
    assertEquals("GOOGL", pos2.getTicker());
    assertEquals(5, pos2.getNumOfShares());
    assertEquals(2500.0, pos2.getValuePaid(), 0.001);
  }


  @Test
  public void testDeleteById() throws Exception {
    // Given: a position to delete by symbol.
    String deleteSql = "DELETE FROM position WHERE symbol=?";
    when(mockConnection.prepareStatement(deleteSql)).thenReturn(mockDeleteStmt);
    when(mockDeleteStmt.executeUpdate()).thenReturn(1);

    // When
    positionDao.deleteById("TSLA");

    // Then
    verify(mockDeleteStmt).setString(1, "TSLA");
    verify(mockDeleteStmt).executeUpdate();
  }

  @Test
  public void testDeleteAll() throws Exception {
    // Given: deletion of all positions.
    String deleteAllSql = "DELETE FROM position";
    when(mockConnection.createStatement()).thenReturn(mockDeleteAllStmt);
    when(mockDeleteAllStmt.executeUpdate(deleteAllSql)).thenReturn(1);

    // When
    positionDao.deleteAll();

    // Then
    verify(mockDeleteAllStmt).executeUpdate(deleteAllSql);
  }
}
