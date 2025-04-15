package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GoogleSearchBoxCssSelector {
    public static void main(String[] args) {
        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // Open the demo website
        driver.get("https://demowebshop.tricentis.com/");

        // Locate the search box
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));

        // Extract all attributes using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, Object> attributes = (Map<String, Object>) js.executeScript(
                "var items = {}; " +
                        "for (var index = 0; index < arguments[0].attributes.length; ++index) { " +
                        "  items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value; " +
                        "} return items;", searchBox);

        // Build CSS Selector
        StringBuilder cssSelector = new StringBuilder(searchBox.getTagName());

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            // Prefer id or class if available
            if (key.equalsIgnoreCase("id")) {
                cssSelector = new StringBuilder("#" + value);
                break; // ID is unique, no need to append more
            } else if (key.equalsIgnoreCase("class")) {
                cssSelector = new StringBuilder("." + value.trim().replace(" ", "."));
                // don't break, could add more specific attrs
            } else {
                cssSelector.append("[").append(key).append("='").append(value).append("']");
            }
        }

        // Print CSS Selector
        System.out.println("\n--- Generated CSS Selector ---");
        System.out.println(cssSelector.toString());

        driver.quit();
    }
}
