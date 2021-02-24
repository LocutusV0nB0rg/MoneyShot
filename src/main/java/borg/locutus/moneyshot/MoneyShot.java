package borg.locutus.moneyshot;

import borg.locutus.moneyshot.payments.IncomingTransactionListener;
import borg.locutus.moneyshot.payments.OutgoingTransactionListener;
import borg.locutus.moneyshot.scheduling.SyncTickScheduler;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoneyShot extends LabyModAddon {
    public static boolean incomingPayments = true;
    public static boolean outgoingPayments = true;
    public static int daysOfSaving = 7;
    public static String pathOfSaveFolder = "";

    @Override
    public void onEnable() {
        getApi().getEventManager().register(new IncomingTransactionListener());
        getApi().getEventManager().register(new OutgoingTransactionListener());

        getApi().registerForgeListener(new SyncTickScheduler());

        SyncTickScheduler.scheduleNewSyncTask(new Runnable() {
            @Override
            public void run() {
                deleteOldFiles();
            }
        }, 200);
    }

    @Override
    public void loadConfig() {
        incomingPayments = !getConfig().has("incomingPayments") || getConfig().get("incomingPayments").getAsBoolean();
        outgoingPayments = !getConfig().has("outgoingPayments") || getConfig().get("outgoingPayments").getAsBoolean();
        daysOfSaving = getConfig().has( "daysOfSaving" ) ? getConfig().get( "daysOfSaving" ).getAsInt() : 7;
        pathOfSaveFolder = getConfig().has( "pathOfSaveFolder" ) ? getConfig().get( "pathOfSaveFolder" ).getAsString(): "";
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {
        subSettings.add( new HeaderElement("Protokoll-Einstellungen"));
        subSettings.add( new BooleanElement( "Eingehende Zahlungen protokollieren", this, new ControlElement.IconData( Material.LEVER ), "incomingPayments", incomingPayments ) );
        subSettings.add( new BooleanElement( "Ausgehende Zahlungen protokollieren", this, new ControlElement.IconData( Material.LEVER ), "outgoingPayments", outgoingPayments ) );

        subSettings.add( new HeaderElement("Dateispeicherung"));
        subSettings.add( new SliderElement( "Speicherdauer in Tagen", this, new ControlElement.IconData( Material.ITEM_FRAME ), "daysOfSaving", daysOfSaving ).setRange( 1, 31 ) );

        StringElement channelStringElement = new StringElement( "Dateispeicherpfad", this, new ControlElement.IconData( Material.PAPER ), "pathOfSaveFolder", pathOfSaveFolder);
        subSettings.add( channelStringElement );
        subSettings.add( new HeaderElement("Pfad leer lassen, damit der Standard-Ordner von Minecraft verwendet wird"));
    }

    private void deleteOldFiles() {
        File dir = pathOfSaveFolder.equals("") ? new File(Minecraft.getMinecraft().mcDataDir, "screenshots") : new File(pathOfSaveFolder, "screenshots");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) continue;
                if (!child.getName().contains("dispensing") && !child.getName().contains("received")) continue;

                long lastModified = child.lastModified();
                long currentTime = System.currentTimeMillis();

                long timePassed = currentTime - lastModified;
                long days = TimeUnit.MILLISECONDS.toDays(timePassed);

                if (days >= daysOfSaving) {
                    if(child.delete()) {
                        System.out.println("File deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                }
            }
        } else {
            throw new RuntimeException("Der in der Config angegebene Pfad ist kein Ordner");
        }
    }
}
