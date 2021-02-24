package borg.locutus.moneyshot.payments;

import borg.locutus.moneyshot.MoneyShot;
import borg.locutus.moneyshot.screenshot.ScreenshotCreator;
import net.labymod.api.events.MessageReceiveEvent;
import org.apache.commons.lang3.StringUtils;

public class OutgoingTransactionListener implements MessageReceiveEvent {
    public boolean onReceive(String message, String unformattedMessage) {
        if (!MoneyShot.outgoingPayments) {
            return false;
        }

        if (!checkIfMessageIdValidPayment(message)) {
            return false;
        }

        String[] args = unformattedMessage.split(" ");
        String playername = args[4];
        String money = args[5].substring(1).replace(",", "");
        money = StringUtils.substringBefore(money, ".");
        int moneyint = Integer.parseInt(money);
        if (moneyint == 0)
            return false;

        ScreenshotCreator.takeScreenshotOfValidPayment(playername, moneyint, "dispensing");

        return false;
    }

    private boolean checkIfMessageIdValidPayment(String message) {

        //§r§aDu hast §r§bDeveloper§r§8 ┃ §r§bLocutusVonBorg§r§a $1 gegeben.§r
        return message.startsWith("§r§aDu hast") && message.endsWith("§r") && message.contains("§a") &&
                !message.contains(":") && message.contains("gegeben") && message
                .contains("$") && !message.split(" ")[2].endsWith("§f");
    }
}