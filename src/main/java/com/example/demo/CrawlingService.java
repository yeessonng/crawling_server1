package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CrawlingService {

    public Map<String, String> fetchWishData(String url) {
        Map<String, String> productData = new HashMap<>();

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/snap/bin/chromium");

        // EC2 환경 대응 옵션
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(options);
            driver.get(url);

            WebElement titleElement = driver.findElement(By.cssSelector("h1.prod-buy-header__title"));
            WebElement imageElement = driver.findElement(By.cssSelector("img.prod-image__detail"));

            String title = titleElement.getText();
            String imageUrl = imageElement.getAttribute("src");

            String highResImage = imageElement.getAttribute("data-zoom-image-url");
            if (highResImage != null && !highResImage.isEmpty()) {
                imageUrl = highResImage;
            }

            if (imageUrl != null && imageUrl.startsWith("//")) {
                imageUrl = "https:" + imageUrl;
            }

            productData.put("title", title);
            productData.put("image", imageUrl);

        } catch (Exception e) {
            // 크롤링 실패 시 null로 반환 (에러 포함)
            productData.put("title", null);
            productData.put("image", null);
            productData.put("error", e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        return productData;
    }
}
