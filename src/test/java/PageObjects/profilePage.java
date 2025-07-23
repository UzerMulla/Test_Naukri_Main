package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static Helpers.Hooks.driver;

public class profilePage {
    private static WebElement element;

    public static WebElement ProfileDashBoard(){
        element=driver.findElement(By.xpath("//div[@class='dashboard']"));
        return element;
    }
    public static WebElement profileName(){
        element=driver.findElement(By.xpath("//span[@class='fullname']"));
        return element;
    }
    public static WebElement Edit_Icon(){
        element=driver.findElement(By.xpath("//em[contains(@class,'icon edit')]"));
        return element;
    }
    public static WebElement profileUpdatedValue(){
        element=driver.findElement(By.xpath("//span[contains(@class,'mod-date-val')]"));
        return element;
    }
    public static WebElement name_text(){
        element=driver.findElement(By.id("name"));
        return element;
    }
    public static WebElement submit_button(){
        element=driver.findElement(By.id("saveBasicDetailsBtn"));
        return element;
    }


}
