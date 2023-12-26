package ru.netology.selenide;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class cardDelivery {
    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    private long generateDateTs(int addDays) {
        LocalDate localDate = LocalDate.now().plusDays(addDays);
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        return instant.toEpochMilli();
    }

    @Test
    public void shouldBeSuccessfullyCompleted() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Рязань");
        String planningDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иван Петров-Сидоров");
        $("[data-test-id='phone'] input").setValue("+79161234567");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldBeSuccessfullyCompleted2() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").click();
        $("[data-test-id='city'] input").setValue("Ка");
        $$(".menu").findBy(text("Казань")).click();
        String planningDate = generateDate(7, "dd.MM.yyyy");
        long planningDateTs = generateDateTs(7);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $(".calendar__layout").click();
        if (!$(".calendar__day[data-day='" + planningDateTs + "']").exists()) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $(".calendar__day[data-day='" + planningDateTs + "']").click();
        $("[data-test-id='name'] input").setValue("Иван Петров-Сидоров");
        $("[data-test-id='phone'] input").setValue("+79161234567");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }
}
