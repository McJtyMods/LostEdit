package com.mcjty.lostedit.client.rendering;

import com.mcjty.lostedit.client.PartInfo;
import com.mcjty.lostedit.client.ProjectInfoHolder;
import com.mcjty.lostedit.client.ProjectInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mcjty.lib.client.CustomRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.Set;

public class EditorRenderer {

    private static final RenderType TRANSLUCENT = RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);

    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        ProjectInfo info = ProjectInfoHolder.getProjectInfo();
        if (info.isEditing()) {
            ResourceKey<Level> dimension = info.editingAtDimension();
            if (dimension == Minecraft.getInstance().level.dimension()) {
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

                MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                VertexConsumer builder = buffer.getBuffer(TRANSLUCENT);
                Integer x = info.editingAtChunkX() << 4;
                Integer z = info.editingAtChunkZ() << 4;
                Integer y = info.editingAtY();
                PartInfo partInfo = info.partsProject().get(info.partName());
                int height = partInfo.height();
                renderHighlightedOutline(poseStack, builder, x, y, z, x + 16, y + height , z + 16, 1.0f, 1.0f, 1.0f, 1.0f);

                poseStack.popPose();
                buffer.endBatch();
            }
        }
    }


    private static void renderBlocks(RenderLevelStageEvent evt, Set<BlockPos> blocks) {
        if (System.currentTimeMillis() % 1000 < 200) {
            return;
        }
        PoseStack matrixStack = evt.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

        matrixStack.pushPose();

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        for (BlockPos pos : blocks) {
            renderHighLightedBlocksOutline(matrixStack, builder, pos.getX(), pos.getY(), pos.getZ(), 1.0f, 1.0f, 1.0f, 1.0f);
        }

        matrixStack.popPose();

        RenderSystem.disableDepthTest();
        buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);
    }

    private static void renderHighlightedOutline(PoseStack poseStack, VertexConsumer buffer,
                                                 float x1, float y1, float z1,
                                                 float x2, float y2, float z2,
                                                 float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a).endVertex();
    }

    private static void renderHighLightedBlocksOutline(PoseStack poseStack, VertexConsumer buffer, float mx, float my, float mz, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
    }
}
