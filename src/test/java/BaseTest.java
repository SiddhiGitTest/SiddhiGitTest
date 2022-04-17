import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class BaseTest  {

    public  String USERNAME = readProperties("browserStackUserName");;

    public  String AUTOMATE_KEY = readProperties("browserStackPassword");
    public  final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    WebDriver driver;

    public BaseTest() throws IOException {
    }

    @Parameters({"browser","browser_version","os","os_version"})
    @BeforeTest
    public void setup(String browser, String browser_version, String os, String os_version) throws MalformedURLException {
        DesiredCapabilities caps=new DesiredCapabilities();
        caps.setCapability("browser",browser);
        caps.setCapability("browser_version",browser_version);
        caps.setCapability("os",os);
        caps.setCapability("os_version",os_version);

        URL browserURL=new URL(URL);
        driver=new RemoteWebDriver(browserURL,caps);

    }

    @AfterTest
    public void teardown()
    {
        driver.quit();
    }

    public static String readProperties(String propertyName) throws IOException {
        Properties prop=new Properties();

        try {
            prop.load(new FileInputStream(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "commonProperties"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(propertyName);

    }

}
