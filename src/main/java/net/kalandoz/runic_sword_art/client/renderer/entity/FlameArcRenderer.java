package net.kalandoz.runic_sword_art.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.world.entity.projectile.FlameArcEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlameArcRenderer extends EntityRenderer<FlameArcEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(RunicSwordArt.MOD_ID, "textures/entity/projectile/flame_arc.png");

    private static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(TEXTURE);

    public FlameArcRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    protected int getBlockLight(FlameArcEntity entityIn, BlockPos pos) {
        return 15;
    }

    public void render(FlameArcEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void func_229045_a_(IVertexBuilder vertexBuilder, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float x, int y, int u, int v) {
        vertexBuilder.pos(p_229045_1_, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).tex((float)u, (float)v).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(FlameArcEntity entity) {
        return TEXTURE;
    }
}