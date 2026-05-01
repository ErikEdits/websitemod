package com.websitemod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.awt.Desktop;
import java.net.URI;

@Environment(EnvType.CLIENT)
public class WebsiteBrowserScreen extends Screen {

    private final String url;
    private boolean opened = false;
    private String statusMessage = "";

    public WebsiteBrowserScreen(String url) {
        super(Text.literal("WebsiteMod Browser"));
        this.url = url;
    }

    @Override
    protected void init() {
        // "Open in Browser" button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Open in Browser"),
            button -> openInSystemBrowser()
        ).dimensions(this.width / 2 - 100, this.height / 2 - 10, 200, 20).build());

        // Close button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            button -> this.close()
        ).dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build());

        // Auto-open on init
        openInSystemBrowser();
    }

    private void openInSystemBrowser() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                statusMessage = "§aWebsite opened in your browser!";
                opened = true;
            } else {
                // Fallback: try Runtime exec
                openWithRuntime();
            }
        } catch (Exception e) {
            WebsiteMod.LOGGER.error("Failed to open URL: " + url, e);
            statusMessage = "§cCould not open browser: " + e.getMessage();
        }
    }

    private void openWithRuntime() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        if (os.contains("win")) {
            rt.exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
        } else if (os.contains("mac")) {
            rt.exec(new String[]{"open", url});
        } else {
            // Linux
            rt.exec(new String[]{"xdg-open", url});
        }

        statusMessage = "§aWebsite opened!";
        opened = true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw dark background
        this.renderBackground(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;

        // Title
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§6§lWebsiteMod Browser"),
            centerX,
            this.height / 2 - 80,
            0xFFFFFF
        );

        // URL display box background
        int boxX = centerX - 200;
        int boxY = this.height / 2 - 60;
        int boxW = 400;
        int boxH = 30;

        context.fill(boxX - 2, boxY - 2, boxX + boxW + 2, boxY + boxH + 2, 0xFF333333);
        context.fill(boxX, boxY, boxX + boxW, boxY + boxH, 0xFF1a1a2e);

        // URL text (truncated if too long)
        String displayUrl = url.length() > 55 ? url.substring(0, 52) + "..." : url;
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§b" + displayUrl),
            centerX,
            boxY + 10,
            0xFFFFFF
        );

        // Status message
        if (!statusMessage.isEmpty()) {
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(statusMessage),
                centerX,
                this.height / 2 + 55,
                0xFFFFFF
            );
        }

        // Info text
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§7The website has been opened in your system browser."),
            centerX,
            this.height / 2 - 25,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }
}
