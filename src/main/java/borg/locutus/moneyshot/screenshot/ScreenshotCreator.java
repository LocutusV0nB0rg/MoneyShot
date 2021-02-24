package borg.locutus.moneyshot.screenshot;

import borg.locutus.moneyshot.MessageUtils;
import borg.locutus.moneyshot.MoneyShot;
import borg.locutus.moneyshot.scheduling.SyncTickScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotCreator {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private static void saveProofScreenshot(String playerName, int amount, String suffix) {
        String screenshotFileName = generateNameForProofScreenshot(playerName, amount, suffix);

        if (MoneyShot.pathOfSaveFolder.equals("")) {
            ScreenShotHelper.saveScreenshot(Minecraft.getMinecraft().mcDataDir, screenshotFileName, Minecraft.getMinecraft().displayWidth,
                    Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
        } else {
            File directory = new File(MoneyShot.pathOfSaveFolder);

            if (!directory.exists()) {
                MessageUtils.displayLocalMessage("§f[§eMoneyShot§f]§r §cDas angegebene Verzeichnis §b" + MoneyShot.pathOfSaveFolder + "konnte nicht gefunden werden.");
                MessageUtils.displayLocalMessage("§4§lDER SCREENSHOT WURDE NICHT GESPEICHERT!");
                return;
            }

            ScreenShotHelper.saveScreenshot(directory, screenshotFileName, Minecraft.getMinecraft().displayWidth,
                    Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
        }
    }

    private static String generateNameForProofScreenshot(String playerName, int amount, String suffix) {
        String dateString = dateFormat.format(new Date());
        return dateString + "_" + playerName + "_" + amount + "_" + suffix + ".png";
    }

    public static void takeScreenshotOfValidPayment(final String playerName, final int amount, final String suffix) {
        SyncTickScheduler.scheduleNewSyncTask(new Runnable() {
            @Override
            public void run() {
                ScreenshotCreator.saveProofScreenshot(playerName, amount, suffix);
            }
        }, 2);
    }
}
