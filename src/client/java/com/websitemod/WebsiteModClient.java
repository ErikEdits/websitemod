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
    }

    public static void openWebsite(String url) {
        MinecraftClient client = MinecraftClient.getInstance();
        // Schedule on the main thread
        client.execute(() -> {
            client.setScreen(new WebsiteBrowserScreen(url));
        });
    }
}
