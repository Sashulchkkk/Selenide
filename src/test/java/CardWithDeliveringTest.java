import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import static com.codeborne.selenide.Condition.*;
public class CardWithDeliveringTest {
    @BeforeAll
    public static void setupClass() {
        //WebDriverManager.chromedriver().driverVersion("116.0").setup();
    }
    String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldBeValidTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input ").setValue("Хабаровск");
        $("[data-test-id='date'] input ").doubleClick().sendKeys(generateDate(3, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79282890201");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofMillis(15000));
        $(".notification__content").shouldHave(text("Встреча успешно забронирована на " + generateDate(3, "dd.MM.yyyy")),
                Duration.ofSeconds(15)).shouldBe(visible);
    }
    @Test
    public void BadCityTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input ").setValue("Таганрог");
        $("[data-test-id='date'] input ").doubleClick().sendKeys(generateDate(3, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79282890201");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldBe(visible);

    }

    @Test
    public void BadDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input ").setValue("Хабаровск");
        $("[data-test-id='date'] input ").doubleClick().sendKeys(generateDate(1, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79282890201");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(byText("Заказ на выбранную дату невозможен")).shouldBe(visible);

    }

    @Test
    public void BadName() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input ").setValue("Хабаровск");
        $("[data-test-id='date'] input ").doubleClick().sendKeys(generateDate(5, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Ivanov Petr");
        $("[data-test-id='phone'] input").setValue("+79282890201");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible);

    }

    @Test
    public void BadPhone() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input ").setValue("Хабаровск");
        $("[data-test-id='date'] input ").doubleClick().sendKeys(generateDate(5, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("89282890201");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible);

    }
}
