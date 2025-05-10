package service_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.model.Position;
import ca.jrvs.apps.stockquote.service.PositionService;

class PositionService_UnitTest {

  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private PositionService positionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testBuy_existingPosition() {
    // Arrange
    String ticker = "AAPL";
    int additionalShares = 100;
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(50);

    // Make sure the ticker exists in the Quote table (simulate that behavior)
    when(positionDao.tickerExistsInQuoteTable(ticker)).thenReturn(true);
    when(positionDao.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Act
    Position result = positionService.buy(ticker, additionalShares);

    // Assert: shares should be updated
    assertEquals(150, result.getNumOfShares());
    verify(positionDao, times(1)).save(existingPosition);
  }


  @Test
  void testBuy_newPosition() {
    // Arrange
    String ticker = "GOOGL";
    int buyShares = 100;

    // Ensure the ticker exists in the Quote table
    when(positionDao.tickerExistsInQuoteTable(ticker)).thenReturn(true);
    when(positionDao.findById(ticker)).thenReturn(Optional.empty());

    // Act
    Position result = positionService.buy(ticker, buyShares);

    // Assert
    assertEquals(ticker, result.getTicker());
    assertEquals(buyShares, result.getNumOfShares());
    verify(positionDao, times(1)).save(result);
  }


  @Test
  void testSell_existingPosition() {
    // Arrange
    String ticker = "GOOGL";
    when(positionDao.findById(ticker)).thenReturn(Optional.of(new Position(ticker, 100, 2000.0)));

    // Act
    positionService.sell(ticker);

    // Assert
    verify(positionDao, times(1)).deleteById(ticker);
  }

  @Test
  void testSell_nonExistingPosition() {
    // Arrange
    String ticker = "AAPL";
    when(positionDao.findById(ticker)).thenReturn(Optional.empty());

    // Act
    positionService.sell(ticker);

    // Assert
    verify(positionDao, never()).deleteById(ticker);
  }

  @Test
  void testSell_nullOrBlankTicker() {
    // Expect IllegalArgumentException for invalid input
    assertThrows(IllegalArgumentException.class, () -> positionService.sell(null));
    assertThrows(IllegalArgumentException.class, () -> positionService.sell("  "));
  }

}
