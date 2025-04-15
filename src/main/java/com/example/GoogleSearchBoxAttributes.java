package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GoogleSearchBoxAttributes {
    public static void main(String[] args) {
        // Auto-setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // Open Google
        driver.get("https://demowebshop.tricentis.com/");

        // Handle cookie consent if shown
        try {
            WebElement consentButton = driver.findElement(By.id("L2AGLb"));
            consentButton.click();
        } catch (Exception ignored) {}

        // Find the search box
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));

        // Extract attributes using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, Object> attributes = (Map<String, Object>) js.executeScript(
                "var items = {}; " +
                        "for (index = 0; index < arguments[0].attributes.length; ++index) { " +
                        "  items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; " +
                        "return items;", searchBox);

        // Build XPath
        StringBuilder xpathBuilder = new StringBuilder("//" + searchBox.getTagName() + "[");
        boolean first = true;
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (!first) {
                xpathBuilder.append(" and ");
            }
            xpathBuilder.append("@").append(entry.getKey()).append("='").append(entry.getValue()).append("'");
            first = false;
        }
        xpathBuilder.append("]");

        // Print XPath
        System.out.println("\n--- Generated XPath ---");
        System.out.println(xpathBuilder.toString());

        driver.quit();
    }
}
