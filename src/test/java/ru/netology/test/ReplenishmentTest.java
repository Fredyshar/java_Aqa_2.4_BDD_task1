package ru.netology.test;

import com.codeborne.selenide.Condition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.data.DataHelper;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class ReplenishmentTest {

    int balanceCard0001, balanceCard0002, actualBalanceCard0001, actualBalanceCard0002;


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void successfulAuth() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        $("#root")
                .shouldHave(Condition.text("Ваши карты"))
                .shouldBe(visible);
    }

    @Test
    void authByInvalidLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.invalidLogin(authInfo);
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void authByInvalidPassword() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.invalidpassword(authInfo);
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void authByInvalidCode() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var wrongcode = DataHelper.getCodeFor();
        verificationPage.invalidVerify(wrongcode);
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Неверно указан код! Попробуйте ещё раз."))
                .shouldBe(visible);
    }


    @Test
    void successfulReplenishmentOfCard0001() {
        var dashboard = new DashboardPage();
        dashboard.MainPage();
        balanceCard0001 = dashboard.getCardBalance("0001");
        balanceCard0002 = dashboard.getCardBalance("0002");
        dashboard.replenishCard0001();

        var transfer = new TransferPage().transfer0001(5000);
        actualBalanceCard0001 = transfer.getCardBalance("0001");
        actualBalanceCard0002 = transfer.getCardBalance("0002");

        Assertions.assertEquals(balanceCard0001 + 5000, actualBalanceCard0001);
        Assertions.assertEquals(balanceCard0002 - 5000, actualBalanceCard0002);
    }


    @Test
    void successfulReplenishmentOfCard0002() {
        var dashboard = new DashboardPage().MainPage();
        balanceCard0001 = dashboard.getCardBalance("0001");
        balanceCard0002 = dashboard.getCardBalance("0002");
        dashboard.replenishCard0002();

        var transfer = new TransferPage().transfer0002(500);
        actualBalanceCard0001 = transfer.getCardBalance("0001");
        actualBalanceCard0002 = transfer.getCardBalance("0002");

        Assertions.assertEquals(balanceCard0001 - 500, actualBalanceCard0001);
        Assertions.assertEquals(balanceCard0002 + 500, actualBalanceCard0002);
    }

    /*    TODO Тест с ошибкой */
//    @Test
//    void replenishmentOfCard0001IfThereIsNotEnoughMoneyOnCard0002() {
//        var dashboard = new DashboardPage().MainPage();
//        balanceCard0001 = dashboard.getCardBalance("0001");
//        balanceCard0002 = dashboard.getCardBalance("0002");
//        dashboard.replenishCard0001();
//
//        var transfer = new TransferPage().transfer0001(1000000);
//
//        $("[data-test-id='error-notification']")
//                .shouldHave(Condition.text("Ошибка"))
//                .shouldBe(visible);
//    }
}
