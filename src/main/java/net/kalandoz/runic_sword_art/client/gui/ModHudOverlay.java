package net.kalandoz.runic_sword_art.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.utils.ManaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class ModHudOverlay extends IngameGui {
    private static final ResourceLocation MANA = new ResourceLocation(RunicSwordArt.MOD_ID,
            "textures/mana.png");

    //Flags to toggle the rendering of certain aspects of the HUD, valid conditions
    //must be met for them to render normally. If those conditions are met, but this flag
    //is false, they will not be rendered.
    public static boolean renderMana = true;

    public static int height = 59;

    public ModHudOverlay(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderIngameGui(MatrixStack mStack, float partialTicks) {

        this.scaledWidth = this.mc.getMainWindow().getScaledWidth();
        this.scaledHeight = this.mc.getMainWindow().getScaledHeight();

        if (!this.mc.gameSettings.hideGUI) {

            assert this.mc.playerController != null;
            if (this.mc.playerController.shouldDrawHUD() && this.mc.getRenderViewEntity() instanceof PlayerEntity)
            {
                if (renderMana) renderMana(mStack, this.scaledWidth, this.scaledHeight, partialTicks);
            }
        }
    }

    protected void renderMana(MatrixStack mStack, int width, int height, float partialTicks) {
        RenderSystem.enableBlend();

        Minecraft.getInstance().getTextureManager().bindTexture(MANA);
        int left = 10;
        int top = 10;

        if (mc.player != null) {
            ManaUtils.displayTotal(mc.player, mc.fontRenderer, mStack, left, top);
            /*
            int level = mc.player.getTotalArmorValue();
            for (int i = 1; level > 0 && i < 20; i += 2) {
                if (i < level) {
                    blit(mStack, left, top, 34, 0, 9, 9);
                } else if (i == level) {
                    blit(mStack, left, top, 25, 0, 9, 9);
                } else {
                    blit(mStack, left, top, 16, 0, 9, 9);
                }
                left += 8;
            }
             */
        }
        RenderSystem.disableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
    }
}
