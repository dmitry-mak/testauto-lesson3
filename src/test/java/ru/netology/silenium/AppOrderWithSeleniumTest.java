package ru.netology.silenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class AppOrderWithSeleniumTest {

    private WebDriver webDriver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        webDriver = new ChromeDriver(options);
        webDriver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        webDriver.quit();
        webDriver = null;
    }

    // Должен отправлять форму, заполненную валидными данными.
    // В ответ должно быть получено сообщение с подтверждением отправки заявки
    @Test
    public void shouldSendFormTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345678901");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement actualResponseMessage = webDriver.findElement(By.cssSelector("[data-test-id='order-success']"));
        String responseText = actualResponseMessage.getText();
        String controlPhrase = "Ваша заявка успешно отправлена";

        Assertions.assertTrue(actualResponseMessage.isDisplayed());
        Assertions.assertTrue(responseText.contains(controlPhrase));
    }

    //    Сервис должен показывать сообщение "Поле обязательно для заполнения" при отправке формы с пустым полем "Телефон"
    @Test
    public void shouldSendMessageForEmptyNameFieldTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        String responseText = invalidNameMessage.getText();
        String controlPhrase = "Поле обязательно для заполнения";

        Assertions.assertTrue(invalidNameMessage.isDisplayed());
        Assertions.assertTrue(responseText.contains(controlPhrase));
    }

    //    Сервис должен показывать сообщение "Поле обязательно для заполнения" при отправке формы с пустым полем "Фамилия и имя"
    @Test
    public void shouldSendMessageForEmptyPhoneFieldTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345678901");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        String responseText = invalidNameMessage.getText();
        String controlPhrase = "Поле обязательно для заполнения";

        Assertions.assertTrue(invalidNameMessage.isDisplayed());
        Assertions.assertTrue(responseText.contains(controlPhrase));
    }

    //   Должен показывать предупреждающее сообщение при использовании недопустимых символов в поле "Имя"
    @Test
    public void shouldSendMessageForLatinNameTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345123451");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualMessage = invalidNameMessage.getText();
        String controlPhrase = "пробелы и дефисы";

        Assertions.assertTrue(actualMessage.contains(controlPhrase));
    }

    //    Должен показывать предупреждение при использовании недопустимых символов в поле "Телефон"
    @Test
    public void shouldSendMessageForInvalidPhoneNumberTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+ТЕЛЕФОН");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualMessage = invalidNameMessage.getText();
        String controlPhrase = "Телефон указан неверно. Должно быть 11 цифр";

        Assertions.assertTrue(actualMessage.contains(controlPhrase));
    }

    //    Должен показывать предупреждение при превышении количества допустимых символов в поле "Телефон"
    @Test
    public void shouldSendMessageForLongPhoneNumberTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+123456789012");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualMessage = invalidNameMessage.getText();
        String controlPhrase = "Телефон указан неверно. Должно быть 11 цифр";

        Assertions.assertTrue(actualMessage.contains(controlPhrase));
    }

    //    Должен показывать предупреждение если в поле "Телефон" введено меньше 11 цифр
    @Test
    public void shouldSendMessageForShortPhoneNumberTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+1234567890");
        webDriver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement invalidNameMessage = webDriver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualMessage = invalidNameMessage.getText();
        String controlPhrase = "Телефон указан неверно. Должно быть 11 цифр";

        Assertions.assertTrue(actualMessage.contains(controlPhrase));
    }


    //    Должен возвращать пользовательское соглашение с текстом красного цвета,
    //    если при отправке формы не отмечен чекбокс о согласии с условиями обработки персональных данных
    @Test
    public void shouldChangeMessageColorForUncheckedCheckboxTest() {

        webDriver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Дмитрий Мамин-Сибиряк");
        webDriver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345678901");
        webDriver.findElement(By.cssSelector(".button.button_view_extra")).click();

        WebElement parentElement = webDriver.findElement(By.cssSelector("[data-test-id='agreement']"));
        String actualClass = parentElement.getAttribute("class");
        Assertions.assertTrue(actualClass.contains("input_invalid"));

        WebElement invalidFormMessage = webDriver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        Assertions.assertTrue(invalidFormMessage.isDisplayed());
    }

}
