package borg.locutus.moneyshot;

import net.labymod.main.LabyMod;

public class MessageUtils {
    public static void displayLocalMessage(String message) {
        LabyMod.getInstance().displayMessageInChat(message);
    }
}