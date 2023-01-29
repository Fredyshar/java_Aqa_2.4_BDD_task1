package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferAmount = $("[data-test-id='amount'] input"); //сумма перевода
    private SelenideElement fromTransfer = $("[data-test-id='from'] input"); //откуда перевод
    private SelenideElement toTransfer = $("[data-test-id='to'] input"); // куда перевод - окно не активно
    private SelenideElement buttonTransfer = $("[data-test-id='action-transfer']"); // кнопка перевод
    private SelenideElement buttonCancel = $("[data-test-id='action-cancel']"); // кнопка отмены

    public TransferPage() {
        transferAmount.shouldBe(Condition.visible);
        fromTransfer.shouldBe(Condition.visible);
        toTransfer.shouldBe(Condition.visible);
        buttonTransfer.shouldBe(Condition.visible);
        buttonCancel.shouldBe(Condition.visible);
    }

    public DashboardPage transfer0001(int amount) {
        transferAmount.setValue(String.valueOf(amount));
        fromTransfer.setValue("5559000000000002");
        buttonTransfer.click();
        return new DashboardPage();
    }

    public DashboardPage transfer0002(int amount) {
        transferAmount.setValue(String.valueOf(amount));
        fromTransfer.setValue("5559000000000001");
        buttonTransfer.click();
        return new DashboardPage();
    }

}