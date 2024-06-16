package com.example.jobparser.database.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.util.Set;

@Service
@Slf4j
public class LinkedInSeleniumService {

    public WebDriver getDriver() {
        var driverOptions = new ChromeOptions();
//        driverOptions.addArguments("--headless"); // Запуск без UI
//        driverOptions.addArguments("--disable-gpu"); // Необходимо для Windows
//        driverOptions.addArguments("--no-sandbox"); // Необходимо для Linux
//        driverOptions.addArguments("--disable-dev-shm-usage"); // Необходимо для Linux

       return new ChromeDriver(driverOptions);
    }

    public void sendMessage(String message, String messageId) {
        var driver = getDriver();
        doAuth(driver);

        // driver.get("https://www.linkedin.com/messaging/thread/2-ZDY3YTBmNmQtMDlhYS00OTUyLWEzNTEtZGQ5NGZkNWQwMjc4XzAxMg==/");
        driver.get("https://www.linkedin.com/messaging/thread/" + messageId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        var messageField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("msg-form__contenteditable")
        ));

        messageField.sendKeys(message);

        var button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'msg-form__send-button') and contains(@class, 'artdeco-button--1')]")
        ));

        if (button.getAttribute("disabled") != null) {
            ((ChromeDriver) driver).executeScript("arguments[0].removeAttribute('disabled')", button);
        }

        button.click();

        driver.quit();

        log.info("Message: '" + message + "' message_id: " + messageId);
    }

    private void loginToLinkedIn(WebDriver driver) {
        driver.get("https://www.linkedin.com/login");

        var usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("ooshkap@gmail.com");

        var passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Q26094431993q");

        var loginButton = driver.findElement(By.cssSelector("button[data-litms-control-urn='login-submit'"));
        loginButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleContains("LinkedIn"));

        saveCookies(driver);
    }

    private void saveCookies(WebDriver driver) {
        log.info("Save cookies start!");

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("cookies.ser"))) {
            Set<Cookie> cookies = driver.manage().getCookies();
            outputStream.writeObject(cookies);

            log.info("Cookies saved successfully.");
        } catch (IOException e) {
            log.error("Failed to save cookies: " + e.getMessage());
        }
    }

    public void doAuth(WebDriver driver) {
        if (!loadCookies(driver)) {
            loginToLinkedIn(driver);
        }
    }

    private boolean loadCookies(WebDriver driver) {
        log.info("Start loading cookies");

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("cookies.ser"))) {
            Set<Cookie> cookies = (Set<Cookie>) inputStream.readObject();

            driver.get("https://www.linkedin.com");

            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }

            log.info("Cookies loaded successfully.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            log.error("No cookies found, login required.");
            return false;
        }
    }
}
