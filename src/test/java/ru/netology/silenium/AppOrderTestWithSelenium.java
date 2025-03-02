package ru.netology.silenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class AppOrderTestWithSelenium {

    @Test
    public void seleniumTest() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\akkar\\Drivers\\chromedriver.exe");

        WebDriver chromeDriver;

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");

        chromeDriver = new ChromeDriver(options);

        chromeDriver.get("http://localhost:9999/");


    }
}
