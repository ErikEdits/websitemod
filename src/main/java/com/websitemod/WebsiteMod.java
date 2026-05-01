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

                                // Normalize URL
                                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                    url = "https://" + url;
                                }

                                final String finalUrl = url;

                                // Send feedback to the command sender
                                context.getSource().sendFeedback(
                                    () -> Text.literal("§aOpening website: §b" + finalUrl),
                                    false
                                );

                                // If this is a player on the client side, open the browser
                                if (context.getSource().getPlayer() != null) {
                                    WebsiteModClient.openWebsite(finalUrl);
                                }

                                return 1;
                            })
                    )
                    // Also allow /website without args to show usage
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
