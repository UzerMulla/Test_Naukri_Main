package Helpers;


import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static Helpers.Hooks.driver;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/main/resources/Naukri_profile_update.feature"},
        glue = "src/main/java/StepDefs",
        plugin ={"json:target/cucumber-report/Naukri_profile_update.json"},
        dryRun = true
)
public class TestRunner {

    @After
    public static void tearDown(){
        driver.quit();
    }

    @AfterStep
    public static void afterStepWait() throws InterruptedException {
        Hooks.forcedWait(2000);
    }
}
