package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static Helpers.Hooks.driver;

public class login {
    private static WebElement element;
    public static WebElement header_loginButton(){
        element = driver.findElement(By.id("login_Layer"));
        return element;
    }

    public static WebElement username_text(){
        element = driver.findElement(By.xpath("//div[@class='form-row']//input[@type='text']"));
        return element;
    }

    public static WebElement password_text(){
        element = driver.findElement(By.xpath("//div[@class='form-row']//input[@type='password']"));
        return element;
    }

    public static WebElement SignIn_button(){
        element = driver.findElement(By.xpath("//button[contains(@class,'loginButton')]"));
        return element;
    }

    public static WebElement cookieAccept_button(){
        element = driver.findElement(By.xpath("//div[@class='acceptance-btn']"));
        return element;
    }


}
