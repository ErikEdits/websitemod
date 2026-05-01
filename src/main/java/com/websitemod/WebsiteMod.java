package com.websitemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsiteMod implements ModInitializer {

    public static final String MOD_ID = "websitemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Runnable openWebsiteCallback = null;
    public static String pendingUrl = null;

    @Override
    public void onInitialize() {
        LOGGER.info("WebsiteMod initialized!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("website")
                    .then(
                        CommandManager.argument("url", StringArgumentType.greedyString())
                            .executes(context -> {
                                String url = StringArgumentType.getString(context, "url");

                                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                    url = "https://" + url;
                                }

                                final String finalUrl = url;

                                context.getSource().sendFeedback(
                                    () -> Text.literal("§aOpening website: §b" + finalUrl),
                                    false
                                );

                                if (openWebsiteCallback != null) {
                                    pendingUrl = finalUrl;
                                    openWebsiteCallback.run();
                                }

                                return 1;
                            })
                    )
                    .executes(context -> {
                        context.getSource().sendFeedback(
                            () -> Text.literal("§cUsage: /website <url>"),
                            false
                        );
                        return 0;
                    })
            );
        });
    }
}
