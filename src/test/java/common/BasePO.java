package common;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BasePO {
    public static final Logger logger = Logger.getLogger(String.valueOf(BasePO.class));
    protected WebDriver driver;

    public BasePO(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /*
        ** All page objects are declared below
     */
    @FindBy(xpath="//input[@id='twotabsearchtextbox']")
    public WebElement searchBox;

    @FindBy(xpath="//*[@class='autocomplete-results-container']")
    public WebElement amazonSearchSuggestions;

    @FindBy(xpath ="//*[@id='nav-search-submit-button']")
    public WebElement searchClick;

    @FindBy(xpath ="//*[@class='a-section a-spacing-small a-spacing-top-small']//span[contains(text(),\"result\")]")
    public WebElement searchedResult;

    @FindBy(xpath ="//*[@class='a-dropdown-prompt']")
    public WebElement sortPrice;


    /*
            **All common methods are declared below
     */
    public static String readPropertiesFile(String propertyName)
    {
        Properties prop= new Properties();
        try
        {
            prop.load(new FileInputStream(System.getProperty("user.dir")+ File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"readProperties"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(propertyName);
    }

    // launch amazon url
    public void launchAmazonURL() {
        driver.get(readPropertiesFile("amazonURL"));
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        logger.info("*** Amazon url is loaded fine****");
    }

    // search product on amazon
    public void searchProduct() throws InterruptedException {
        searchBox.sendKeys(readPropertiesFile("productSearch"));
        //searchListSelection();
        searchClick.click();
    }

    private void searchListSelection() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        Thread.sleep(3000);
        if (amazonSearchSuggestions.isDisplayed()) {
            logger.info(("Amazon search list is displayed"));
            List<WebElement> list = driver.findElements(By.xpath("//*[@class='autocomplete-results-container']//div"));

            logger.info("Total suggestion :" + list.size());

            String expectedSearch = readPropertiesFile("productSearch");

            for (int ls = 0; ls < list.size(); ls++) {
                if (list.get(ls).equals(expectedSearch)) {
                    list.get(ls).click();
                    break;
                }
            }
            Thread.sleep(3000);
        }
        else
        {
            logger.info("no search");
        }
    }

    // to verify searched page and its filters
    public void searchedPageFilter() throws InterruptedException {
        WebDriverWait wait =new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='a-section a-spacing-small a-spacing-top-small']//span[contains(text(),\"result\")]")));

        logger.info("**** total searched result before filter apply*** "+ searchedResult.getText() +" \"oneplus 9\"");

        logger.info("Selecting review filter");
        reviewRatingFilter();
        logger.info("Selecting brand filter");
        brandFilter();
        logger.info("Selecting memory filter");
        internalMemoryFilter();
        logger.info("Selecting price filter");
        sortPrice();

    }

    private void sortPrice() throws InterruptedException {
        List<WebElement> price=driver.findElements(By.xpath("//*[@class='a-popover-wrapper']//ul//li//a"));

        Select drpCountry = new Select(driver.findElement(By.name("s")));
        drpCountry.selectByVisibleText("Price: Low to High");
        Thread.sleep(3000);
    }

    private void internalMemoryFilter() throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,350)", "");
        if(driver.findElement(By.xpath("//*[@id='filters']//ul//li[@id='p_n_feature_eight_browse-bin/8561112031']//i[@class='a-icon a-icon-checkbox']")).isDisplayed())
            driver.findElement(By.xpath("//*[@id='filters']//ul//li[@id='p_n_feature_eight_browse-bin/8561112031']//i[@class='a-icon a-icon-checkbox']")).click();
        Thread.sleep(3000);
    }

    private void brandFilter() throws InterruptedException {

        List<WebElement> brandFilter=driver.findElements(By.xpath("//*[@id='brandsRefinements']//ul//li"));
        for (int i=0;i<brandFilter.size();i++)
        {
            if (brandFilter.get(i).getText().equals("OnePlus"))
            {
                brandFilter.get(i).click();
                driver.findElement(By.xpath("//*[@id='brandsRefinements']//ul//li//i[@class='a-icon a-icon-checkbox']")).click();
                Thread.sleep(5000);
                break;
            }
            else
            {
                logger.info("brand is not visible");
            }
        }
    }

    private void reviewRatingFilter() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id='reviewsRefinements']//ul//li//i[@class=\"a-icon a-icon-star-medium a-star-medium-4\"]")).click();

    }

    public void resultPage() throws InterruptedException {

        List<WebElement> resultPage=driver.findElements(By.xpath("//*[@class='s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16']"));

        logger.info("Total product on final result page "+ resultPage.size());

        List<WebElement> productName= driver.findElements(By.xpath("//*[@class='s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16']//h2"));
        List<WebElement> productPrice= driver.findElements(By.xpath("//*[@class='s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16']//a//span[@class='a-price']"));
        List<WebElement> productLink= driver.findElements(By.xpath("//*[@class='s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16']//h2//a[contains(@href,'OnePlus')]"));

        for (int i=0;i<productName.size();i++)
        {
            logger.info("Product Name : "+ productName.get(i).getText() +" **** "+" Product Price : "+ productPrice.get(i).getText()+" ***** "+" Product Link : "+ productLink.get(i).getAttribute("href"));
        }

        Thread.sleep(3000);
    }
}
