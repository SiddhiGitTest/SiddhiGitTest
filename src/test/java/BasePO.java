import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BasePO {
    public static final Logger logger = Logger.getLogger(String.valueOf(BasePO.class));
    protected WebDriver driver;

    public BasePO(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//*[@class='menu_Flights']")
    public WebElement flightClick;

    @FindBy(xpath = "//*[@class='makeFlex']")
    public WebElement tripType;

    @FindBy(xpath = "//label[@for='fromCity']")
    public WebElement fromCity;

    @FindBy(xpath = "//*[@id='toCity']")
    public WebElement toCity;

    @FindBy(xpath = "//div[@class='hsw_autocomplePopup autoSuggestPlugin ']//div//input")
    public WebElement from;

    public void launchURL(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void selectFlight() {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
       // flightClick.click();
    }

    public void selectTripType() throws InterruptedException {
        List<WebElement> ls=driver.findElements((By.xpath("//*[@class='makeFlex']//ul//li")));

        String type="ONEWAY";
        for (int i=0;i<ls.size();i++) {
            System.out.println(ls.get(i).getText());
            if (ls.get(i).getText().equals(type)) {
                logger.info("User wants to book OneWay trip");
                oneWayTrip();
            }
            else if(ls.get(i).equals("Round Trip")) {
                logger.info("User wants to book Round trip");
            }
            else
            {
                logger.info("Multi city trip");
            }
        }
    }

    private void oneWayTrip() throws InterruptedException {
        Thread.sleep(3000);
     //   fromCity.click();
        Actions act = new Actions(driver);

        WebElement ele= driver.findElement(By.xpath("//label[@for=\"fromCity\"]"));
        act.doubleClick(ele).perform();
       logger.info("clicked");
        Thread.sleep(3000);
        from.sendKeys("Del");
        WebDriverWait wait=new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//*[@class='hsw_autocomplePopup autoSuggestPlugin ']//ul//li"))));
        List<WebElement> fromCities=driver.findElements(By.xpath("//*[@class='hsw_autocomplePopup autoSuggestPlugin ']//ul//li"));
        for (int i=0;i<fromCities.size();i++)
        {
            if(fromCities.get(i).equals("New Delhi, India"))
            {
                fromCities.get(i).click();
            }
        }
        logger.info("From city is selected");

    }
}
