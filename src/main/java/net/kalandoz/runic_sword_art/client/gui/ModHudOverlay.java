package net.kalandoz.runic_sword_art.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.utils.AttunementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

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
        System.out.println("Method Overriden!");

        this.scaledWidth = this.mc.getMainWindow().getScaledWidth();
        this.scaledHeight = this.mc.getMainWindow().getScaledHeight();

        if (!this.mc.gameSettings.hideGUI) {
            System.out.println("GUI is not hidden!");

            if (this.mc.playerController != null) {
                System.out.println("Player Controller is not null!");
                if (this.mc.getRenderViewEntity() instanceof PlayerEntity) {
                    System.out.println("About to render Mana!");
                    if (renderMana) renderMana(mStack, this.scaledWidth, this.scaledHeight, partialTicks);
                }
            }
        }
    }

    protected void renderMana(MatrixStack mStack, int width, int height, float partialTicks) {
        RenderSystem.enableBlend();
        System.out.println("Rendering Mana!");

        Minecraft.getInstance().getTextureManager().bindTexture(MANA);
        int left = 10;
        int top = 10;

        if (mc.player != null) AttunementUtils.displayTotal(mc.player, mc.fontRenderer, mStack, left, top);
        RenderSystem.disableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
    }
}
