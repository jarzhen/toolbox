package cn.tool.code;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockPriceSpider {
    Logger logger = LoggerFactory.getLogger(StockPriceSpider.class);

    @Test
    public void test1() {
        seleniumProcess();
    }
    private void seleniumProcess() {

        String uri = "http://quote.eastmoney.com/sh600036.html";

        // 设置 chromedirver 的存放位置
        System.getProperties().setProperty("webdriver.chrome.driver", "D:/soft_install/chromedriver/chromedriver.exe");

        // 设置浏览器参数
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");//禁用沙箱
        chromeOptions.addArguments("--disable-dev-shm-usage");//禁用开发者shm
        chromeOptions.addArguments("--headless"); //无头浏览器，这样不会打开浏览器窗口
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get(uri);
        WebElement webElements = webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div[8]/div[1]/div/div[1]/span[1]/span"));
        String stockPrice = webElements.getText();
        logger.info("最新股价为 >>> {}", stockPrice);
        webDriver.close();
    }

}