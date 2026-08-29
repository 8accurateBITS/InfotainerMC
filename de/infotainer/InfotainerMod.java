package de.infotainer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class InfotainerMod implements ClientModInitializer {
    public static final NewsManager NEWS_MANAGER = new NewsManager();
    public static final ServerInfoProvider SERVER_INFO = new ServerInfoProvider();

    @Override
    public void onInitializeClient() {
        NEWS_MANAGER.start();
        HudRenderCallback.EVENT.register(new InfotainerHud());
    }
}
