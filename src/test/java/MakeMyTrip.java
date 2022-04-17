import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class MakeMyTrip  extends BaseTest{

    public MakeMyTrip() throws IOException {
    }

    @Test
    public void makeMyTrip() throws IOException, InterruptedException {
        BasePO po=new BasePO(driver);

        po.launchURL(readProperties("url"));
        po.selectFlight();
        po.selectTripType();

    }
}
