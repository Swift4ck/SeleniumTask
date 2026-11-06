import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


public class GoogleSearchSelenideTest {

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--remote-allow-origins=*");
        Configuration.browserCapabilities = options;

        open("https://www.google.com");
    }


    @Test
    @DisplayName("Проверка функциональности поиска")
    public void checkSearchFunctionality(){

        $("[name='q']").setValue("Selenide vs Selenium").pressEnter();

        $("#search h3").shouldHave(partialText("Selenide"));

    }


    @Test
    @DisplayName("Проверка кнопки «Мне повезёт»")
    public void buttonLuckyActive() {

        $("[name='q']").setValue("Selenide vs Selenium");

        $("input[value = 'Мне повезёт!']").shouldBe(visible, enabled);

    }

    @Test
    @DisplayName("Проверка ссылок в футере (Настройки и язык")
    public void checkProperties() {

        $(By.xpath("//div[text() = 'Настройки']")).shouldBe(visible);

        $(By.xpath("//div[text() = 'Россия']")).shouldBe(visible);

    }

    @Test
    @DisplayName("Проверка логотипа и ссылок «О нас» / «Реклама»")
    public void checkLogoAndAboutUs() {

        $("svg[aria-label='Google']").shouldBe(visible);

        $(byText("Всё о Google")).shouldBe(visible);

        $(byText("Реклама")).shouldBe(visible)
                .shouldHave(attribute("href"));

    }




}
