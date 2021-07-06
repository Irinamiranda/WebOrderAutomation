import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.List;

public class Dice {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kmira\\Downloads\\drivers\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://www.dice.com/");

        driver.findElementById("typeaheadInput").sendKeys("SDET");
        driver.findElementById("google-location-search").sendKeys("Rockville, MD, USA");
        driver.findElementById("submitSearch-button").click();
        List<WebElement> list = driver.findElementsByCssSelector(".ng-star-inserted a");
        for (WebElement e : list) {
            Assert.assertTrue(e.getText().contains("SDET"));

        }
    }
}
