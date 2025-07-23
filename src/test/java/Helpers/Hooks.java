package Helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Hooks {
    public static WebDriver driver;
    public static void OpenBrowser(String browserType){

        if(browserType.toLowerCase().equals("chrome")){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--incognito");

            driver=new ChromeDriver(options);
            driver.manage().window().maximize();
        }

    }

    public static void WaitTillElementVisible(WebElement ele){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(ele));
    }

    public static void WaitTillElementClickable(WebElement ele){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(ele));
    }

    public static void WaitTillElementPresent(By locator){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void ScrollToElement(WebElement ele){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
    }

    public static void forcedWait(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}
