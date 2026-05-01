package com.websitemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class WebsiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WebsiteMod.LOGGER.info("WebsiteMod client initialized!");

        WebsiteMod.openWebsiteCallback = () -> {
            String url = WebsiteMod.pendingUrl;
            if (url != null) {
                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> client.setScreen(new WebsiteBrowserScreen(url)));
            }
        };
    }
}
