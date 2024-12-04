
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void testGetDetailsById() {
        Long testId = 1L;
        Result<?> result = searchService.getDetailsById(testId);
        System.out.println("Result: " + result);
    }
}
