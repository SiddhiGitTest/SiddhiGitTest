package amazonTest;

import common.BasePO;
import common.BaseTest;
import org.testng.annotations.Test;

public class amazonProductSearch extends BaseTest {


    @Test
    public void searchProduct() throws InterruptedException {
        BasePO po=new BasePO(driver);

        logger.info("*** Launching amazon URL***");
        po.launchAmazonURL();

        logger.info("*** Searching product on amazon***");
        po.searchProduct();

        logger.info("*** to verify search page filters");
        po.searchedPageFilter();

        logger.info("searched result page");
        po.resultPage();
    }
}
