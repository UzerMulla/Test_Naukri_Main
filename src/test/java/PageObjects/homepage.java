package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static Helpers.Hooks.driver;

public class homepage {
    private static WebElement element;

    public static WebElement profileImage(){
        element =driver.findElement(By.xpath("//img[@alt='naukri user profile img']"));
        return element;
    }

    public static WebElement profileUpdate_link(){
        element =driver.findElement(By.xpath("//div[contains(@class,'profile-info')]//a"));
        return element;
    }

}
