package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.util.List;

public class XPathExtractor {
    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {

            String url = "https://google.com";
            driver.get(url);


            List<WebElement> elements = driver.findElements(By.xpath("//*"));
            StringBuilder xpaths = new StringBuilder();

            for (WebElement element : elements) {

                if (element.isDisplayed()) {
                    String xpath = generateXPath(driver, element);
                    xpaths.append(xpath).append("\n");
                }
            }


            File file = new File("W:\\ARTemp\\study\\automation\\projects\\xpath locator\\xpaths.txt");


            if (!file.exists()) {
                System.out.println("Error: File does not exist! Create it first.");
                return;
            }


            try (FileWriter writer = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(writer)) {
                bw.write(xpaths.toString());
            }

            System.out.println("XPaths successfully written to file!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }


    public static String generateXPath(WebDriver driver, WebElement element) {
        String script =
                "function getXPath(element) {" +
                        " if (element.id !== '') { return '//*[@id=\"' + element.id + '\"]'; }" +
                        " if (element === document.body) { return '/html/' + element.tagName.toLowerCase(); }" +
                        " var ix = 0;" +
                        " var siblings = element.parentNode.children;" +
                        " for (var i = 0; i < siblings.length; i++) {" +
                        " if (siblings[i] === element) { return getXPath(element.parentNode) + '/' + element.tagName.toLowerCase() + '[' + (ix+1) + ']'; }" +
                        " if (siblings[i].tagName === element.tagName) { ix++; }" +
                        " } return ''; }" +
                        " return getXPath(arguments[0]);";

        return (String) ((JavascriptExecutor) driver).executeScript(script, element);
    }
}
