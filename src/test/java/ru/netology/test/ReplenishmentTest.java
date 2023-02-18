package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.*;

class ReplenishmentTest {

    int balanceCard0001, balanceCard0002, actualBalanceCard0001, actualBalanceCard0002;

    public DashboardPage mainPage() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        return new DashboardPage();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void authByInvalidLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWithOtherLogin();
        loginPage.invalidLoginOrPassword(authInfo);
        loginPage.getErrorMassage("Неверно указан логин или пароль");
    }

    @Test
    void authByInvalidPassword() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWithOtherPassword();
        loginPage.invalidLoginOrPassword(authInfo);
        loginPage.getErrorMassage("Неверно указан логин или пароль");
    }

    @Test
    void authByInvalidLoginAndPassword() {                                      /*    TODO Тест с ошибкой */
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getOtherAuthInfo();
        loginPage.invalidLoginOrPassword(authInfo);
        loginPage.getErrorMassage("Неверно указан логин или пароль");
    }

    @Test
    void authByInvalidCode() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var wrongcode = DataHelper.getNotVerificationCodeFor();
        verificationPage.invalidVerify(wrongcode);
        verificationPage.getErrorMassage("Неверно указан код! Попробуйте ещё раз.");
    }

    @Test
    void successfulReplenishmentOfCard0001() {
        var mainPage = mainPage();
        balanceCard0001 = mainPage.getCardBalance("0001");
        balanceCard0002 = mainPage.getCardBalance("0002");
        var transfer = mainPage.replenishCard0001();
        transfer.transfer(5000, DataHelper.cardNumber("0001"));
        actualBalanceCard0001 = mainPage.getCardBalance("0001");
        actualBalanceCard0002 = mainPage.getCardBalance("0002");

        Assertions.assertEquals(balanceCard0001 + 5000, actualBalanceCard0001);
        Assertions.assertEquals(balanceCard0002 - 5000, actualBalanceCard0002);
    }

    @Test
    void successfulReplenishmentOfCard0002() {
        var mainPage = mainPage();
        balanceCard0001 = mainPage.getCardBalance("0001");
        balanceCard0002 = mainPage.getCardBalance("0002");
        var transfer = mainPage.replenishCard0002();

        transfer.transfer(500, DataHelper.cardNumber("0002"));
        actualBalanceCard0001 = mainPage.getCardBalance("0001");
        actualBalanceCard0002 = mainPage.getCardBalance("0002");

        Assertions.assertEquals(balanceCard0001 - 500, actualBalanceCard0001);
        Assertions.assertEquals(balanceCard0002 + 500, actualBalanceCard0002);
    }

    @Test
    void replenishmentOfCard0001IfThereIsNotEnoughMoneyOnCard0002() {               /*    TODO Тест с ошибкой */
        var mainPage = mainPage();
        balanceCard0001 = mainPage.getCardBalance("0001");
        balanceCard0002 = mainPage.getCardBalance("0002");
        var transfer = mainPage.replenishCard0001();

        transfer.transfer(100000, DataHelper.cardNumber("0001"));
        transfer.getErrorMassage("Ошибка! На карте **** **** **** 0001 не достаточно денег");

        actualBalanceCard0001 = mainPage().getCardBalance("0001");
        actualBalanceCard0002 = mainPage().getCardBalance("0002");

        Assertions.assertEquals(balanceCard0001, actualBalanceCard0001);
        Assertions.assertEquals(balanceCard0002, actualBalanceCard0002);
    }
}
