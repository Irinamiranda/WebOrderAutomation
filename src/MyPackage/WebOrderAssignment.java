package MyPackage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WebOrderAssignment {

    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kmira\\Downloads\\drivers\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");

        //login
        driver.findElementById("ctl00_MainContent_username").sendKeys("Tester" + Keys.ENTER);
        driver.findElementById("ctl00_MainContent_password").sendKeys("test");
        driver.findElementById("ctl00_MainContent_login_button").click();

        //click to order
        driver.findElementByXPath("//a[@href='Process.aspx']").click();

        //enter random quantity
        String quantity = generateQuantity();
        driver.findElementById("ctl00_MainContent_fmwOrder_txtQuantity").sendKeys(quantity);
        driver.findElementByClassName("btn_dark").click();
        int totalWithoutDiscount = Integer.parseInt(quantity) * 100;
        double total;
        if (Integer.parseInt(quantity)>10){
            total = totalWithoutDiscount - (totalWithoutDiscount * 0.08);

        }else{
            total = totalWithoutDiscount;
        }
        double actual = Double.parseDouble(driver.findElementById("ctl00_MainContent_fmwOrder_txtTotal").getAttribute("value"));
        Assert.assertTrue(actual==total);

        //read data from CSV file
        List<String[]> data = readFromCSV("data\\MOCK_DATA.csv");
        String[] line = data.get((int)(Math.random() * 999 + 1));
        System.out.println(Arrays.toString(line));

        String name = line[1] + " " + line[2];
        String street = line[3];
        String city = line[4];
        String state = line[5];
        String zip = line[6];

        driver.findElementById("ctl00_MainContent_fmwOrder_txtName").sendKeys(name);
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox2").sendKeys(street);
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox3").sendKeys(city);
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox4").sendKeys(state);
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox5").sendKeys(zip);

        List<WebElement> radioButtons = driver.findElementsByName("ctl00$MainContent$fmwOrder$cardList");

        WebElement button = radioButtons.get((int)(Math.random() * radioButtons.size()));
        String cardType = button.getAttribute("value");
        button.click();
        String cardNumber = generateCardNumber(cardType);
        String cardOfExp = "12/21";
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox6").sendKeys(cardNumber);
        driver.findElementById("ctl00_MainContent_fmwOrder_TextBox1").sendKeys(cardOfExp);
        driver.findElementById("ctl00_MainContent_fmwOrder_InsertButton").click();
        String actualMessage = driver.findElementByTagName("strong").getText();
        String expectedMessage = "New order has been successfully added.";
        Assert.assertTrue(actualMessage.equals(expectedMessage));
        driver.findElementByXPath("//a[@href='Default.aspx']").click();

        List<WebElement> orderInfo = driver.findElementsByCssSelector(".SampleTable tr:nth-child(2) td");
        List<String> myInfo = new ArrayList<>();
        for (int i = 1; i < orderInfo.size()-1; i++) {
            myInfo.add(orderInfo.get(i).getText());
        }
        System.out.println("myInfo = " + myInfo);
        Assert.assertTrue(myInfo.get(0).equalsIgnoreCase(name));
        Assert.assertTrue(myInfo.get(2).equalsIgnoreCase(quantity));
        Assert.assertTrue(myInfo.get(4).equalsIgnoreCase(street));
        Assert.assertTrue(myInfo.get(5).equalsIgnoreCase(city));
        Assert.assertTrue(myInfo.get(6).equalsIgnoreCase(state));
        Assert.assertTrue(myInfo.get(7).equalsIgnoreCase(zip));
        Assert.assertTrue(myInfo.get(8).equalsIgnoreCase(cardType));
        Assert.assertTrue(myInfo.get(9).equalsIgnoreCase(cardNumber));
        Assert.assertTrue(myInfo.get(10).equalsIgnoreCase(cardOfExp));

        driver.findElementById("ctl00_logout").click();


    }
    public static String generateQuantity(){
        int max = 100;
        int min = 1;
        int range = max - min + 1;
        String rand = String.valueOf((int)(Math.random() * range) + min);
        return rand;
    }

    public static String generateName(){
        String str = "abcdefghijklmnopqrstuvwxyz";
        String firstName = "";
        String lastName = "";

        for (int i = 0; i < 6; i++) {
            firstName += str.charAt((int)(Math.random() * str.length()));
            lastName += str.charAt((int)(Math.random() * str.length()));
        }
        return firstName.substring(0,1).toUpperCase() + firstName.substring(1) + " "
                + lastName.substring(0,1).toUpperCase()  + lastName.substring(1);
    }

    public static String generateAddress(){
        String str = "abcdefghijklmnopqrstuvwxyz";
        String street = "";

        String numbers = "" + (1000 + (int)(Math.random() * 900));
        for (int i = 0; i < 10; i++) {
            street += str.charAt((int)(Math.random() * str.length()));
        }
        return numbers + " " + street.substring(0,1).toUpperCase() + street.substring(1) + " Dr";
    }

    public static String generateCity(){
        String str = "abcdefghijklmnopqrstuvwxyz";
        String city = "";
        for (int i = 0; i < 8; i++) {
            city += str.charAt((int)(Math.random() * str.length()));
        }
        return city.substring(0,1).toUpperCase() + city.substring(1);
    }

    public static String generateZip(){
        String zip = "" + (10000 + (int)(Math.random() * 9000));
        return zip;
    }
    public static List<String[]> readFromCSV(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        List<String[]> content = new ArrayList<>();
        while((line = br.readLine()) != null){
            content.add(line.split(","));

        }
        br.close();
        return content;
    }

    public static String generateCardNumber(String type){
        String cardNumber = "";
        Random random = new Random();
        if (type.equalsIgnoreCase("Visa")){
            cardNumber = "4" + (long) (100000000000000L + random.nextFloat() * 900000000000000L);
        }else if (type.equalsIgnoreCase("MasterCard")){
            cardNumber = "5" + (long) (100000000000000L + random.nextFloat() * 900000000000000L);
        }else {
            cardNumber = "3"  + (long) (10000000000000L + random.nextFloat() * 90000000000000L);
        }
        return cardNumber;
    }


}
