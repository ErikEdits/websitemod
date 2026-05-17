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
    private String statusMessage = "";

    public WebsiteBrowserScreen(String url) {
        super(Text.literal("WebsiteMod Browser"));
        this.url = url;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Open in Browser"),
            button -> openInSystemBrowser()
        ).dimensions(this.width / 2 - 100, 160, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            button -> this.close()
        ).dimensions(this.width / 2 - 100, 190, 200, 20).build());

        openInSystemBrowser();
    }

    private void openInSystemBrowser() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                statusMessage = "§aWebsite opened in your browser!";
            } else {
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
            rt.exec(new String[]{"xdg-open", url});
        }
        statusMessage = "§aWebsite opened!";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int centerX = this.width / 2;

        context.fill(0, 0, this.width, this.height, 0xC0000000);

        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§6§lWebsiteMod Browser"),
            centerX,
            40,
            0xFFFFFF
        );

        int boxX = centerX - 200;
        int boxY = 70;
        context.fill(boxX - 2, boxY - 2, boxX + 402, boxY + 32, 0xFF333333);
        context.fill(boxX, boxY, boxX + 400, boxY + 30, 0xFF1a1a2e);

        String displayUrl = url.length() > 55 ? url.substring(0, 52) + "..." : url;
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§b" + displayUrl),
            centerX,
            boxY + 10,
            0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§7The website has been opened in your system browser."),
            centerX,
            115,
            0xFFFFFF
        );

        if (!statusMessage.isEmpty()) {
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(statusMessage),
                centerX,
                135,
                0xFFFFFF
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }
}
