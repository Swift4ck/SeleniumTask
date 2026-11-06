import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleSearchSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.google.com");


    }

    @AfterEach
    public void deleteUp() {
        driver.quit();
    }

    @Test
    @DisplayName("Проверка функциональности поиска")
    public void checkSearchFunctionality() {

        WebElement searchBox;

        try {
            searchBox = driver.findElement(By.name("q"));
        } catch (NoSuchElementException e) {
            WebElement searchContainer = driver.findElement(By.cssSelector("ntp-searchbox"));
            searchBox = searchContainer.getShadowRoot().findElement(By.cssSelector("textarea#input"));
        }
        searchBox.sendKeys("Selenide vs Selenium" + Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));


        WebElement firstResultHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@id='search']//h3)[1]")
        ));

        String headingText = firstResultHeading.getText();

        assertThat(firstResultHeading.isDisplayed()).isTrue();

        assertThat(headingText).containsIgnoringCase("Selenide");

        System.out.println("Найден заголовок: " + headingText);
    }

    @Test
    @DisplayName("Проверка кнопки «Мне повезёт»")
    public void buttonLuckyActive() {

        WebElement box;

        try {
            box = driver.findElement(By.name("q"));
        } catch (NoSuchElementException e) {
            WebElement searchContainer = driver.findElement(By.cssSelector("ntp-searchbox"));
            box = searchContainer.getShadowRoot().findElement(By.cssSelector("textarea#input"));
        }

        box.sendKeys("Selenide vs Selenium");

        By buton = By.xpath("//input[@value='Мне повезёт!']");
        box = wait.until(ExpectedConditions.elementToBeClickable(buton));

        assertThat(box.isEnabled()).isTrue();
        assertThat(box.isDisplayed()).isTrue();

        String oldUrl = driver.getCurrentUrl();

        box.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(oldUrl)));

        String newUrl = driver.getCurrentUrl();

        assertThat(newUrl).isNotEqualTo(oldUrl);
    }

    @Test
    @DisplayName("Проверка ссылок в футере (Настройки и язык")
    public void checkProperties() {

        WebElement properties;
        WebElement language;
        WebElement conditions;

        properties = driver.findElement(By.xpath("//div[text() = 'Настройки']"));
        language = driver.findElement(By.xpath("//div[text() = 'Россия']"));

        assertThat(properties.isDisplayed()).isTrue();
        assertThat(language.isDisplayed()).isTrue();

        conditions = driver.findElement(By.xpath("//a[text() = 'Условия']")); //у меня у гугла нет ссылки и соответственно href
        String href = conditions.getAttribute("href"); //я взял соседнюю кнопку
        assertThat(href).isNotNull();

    }

    @Test
    @DisplayName("Проверка логотипа и ссылок «О нас» / «Реклама»")
    public void checkLogoAndAboutUs() {

        WebElement logo;
        WebElement aboutGoogle;
        WebElement advertisement;

        logo = driver.findElement(By.cssSelector("svg[aria-label = 'Google']"));

        assertThat(logo.isDisplayed()).isTrue();

        aboutGoogle = driver.findElement(By.xpath("//a[text() = 'Всё о Google']"));
        String checkText = aboutGoogle.getText();

        assertThat(aboutGoogle.isDisplayed()).isTrue();
        assertThat(checkText).isNotNull();
        assertThat(checkText).isNotBlank();

        advertisement = driver.findElement(By.xpath("//a[text() = 'Реклама']"));
        assertThat(advertisement.isDisplayed()).isTrue();

        String href = advertisement.getAttribute("href");
        assertThat(href).startsWith("https://www.google.com");
    }

}
