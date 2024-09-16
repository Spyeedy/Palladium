package net.threetag.palladium.compat.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.compat.geckolib.armor.AddonGeoArmorItem;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayer;
import software.bernie.geckolib.GeckoLibConstants;

public class GeckoLibCompat {

    public static void init() {
        ItemParser.registerTypeSerializer(new AddonGeoArmorItem.Parser());

//        DeferredRegister<AbilitySerializer> deferredRegister = DeferredRegister.create(GeckoLibConstants.MODID, PalladiumRegistryKeys.ABILITY_SERIALIZER);
//        deferredRegister.register();
//        deferredRegister.register("render_layer_animation", RenderLayerAnimationAbility::new);
//        deferredRegister.register("armor_animation", ArmorAnimationAbility::new);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        PackRenderLayerManager.registerParser(GeckoLibConstants.id("default"), GeckoRenderLayer::parse);
    }

    @Environment(EnvType.CLIENT)
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
//        if (stack.getItem() instanceof AddonGeoArmorItem gecko) {
//            var rendererProvider = gecko.getRenderProvider().getRenderProvider();
//
//            if (rendererProvider instanceof GeoRenderProvider provider) {
//
//                PlayerModel origModel = ((PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getModel();
//                GeckoArmorRenderer<AddonGeoArmorItem> renderer = (GeckoArmorRenderer<AddonGeoArmorItem>) provider.getGeoArmorRenderer(player, stack, EquipmentSlot.CHEST, origModel);
//
//                if (provider instanceof GeoArmorRendererInvoker invoker) {
//                    invoker.invokeGrabRelevantBones(renderer.getGeoModel().getBakedModel(renderer.getGeoModel().getModelResource(gecko)));
//                }
//
//                var bone = (rightArm ? renderer.getRightArmBone() : renderer.getLeftArmBone());
//
//                if (bone != null) {
//                    var partialTick = Minecraft.getInstance().getFrameTime();
//                    RenderType renderType = renderer.getRenderType(gecko, renderer.getTextureLocation(gecko), bufferSource, partialTick);
//                    VertexConsumer buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, stack.hasFoil());
//
//                    RenderUtils.matchModelPartRot(rendererArm, bone);
//                    GeckoRenderLayerModel.copyScaleAndVisibility(rendererArm, bone);
//                    bone.updatePosition(rendererArm.x + (rightArm ? 5 : -5), 2 - rendererArm.y, rendererArm.z);
//
//                    poseStack.pushPose();
//                    poseStack.translate(0, 24 / 16F, 0);
//                    poseStack.scale(-1, -1, 1);
//
//                    Color renderColor = renderer.getRenderColor(gecko, partialTick, combinedLight);
//                    float red = renderColor.getRedFloat();
//                    float green = renderColor.getGreenFloat();
//                    float blue = renderColor.getBlueFloat();
//                    float alpha = renderColor.getAlphaFloat();
//                    int packedOverlay = renderer.getPackedOverlay(gecko, 0, partialTick);
//
//                    AnimationState<AddonGeoArmorItem> animationState = new AnimationState<>(gecko, 0, 0, partialTick, false);
//                    long instanceId = renderer.getInstanceId(gecko);
//
//                    animationState.setData(DataTickets.TICK, gecko.getTick(player));
//                    animationState.setData(DataTickets.ITEMSTACK, stack);
//                    animationState.setData(DataTickets.ENTITY, player);
//                    animationState.setData(DataTickets.EQUIPMENT_SLOT, EquipmentSlot.CHEST);
//                    renderer.getGeoModel().addAdditionalStateData(gecko, instanceId, animationState::setData);
//                    renderer.getGeoModel().handleAnimations(gecko, instanceId, animationState);
//                    renderer.renderRecursively(poseStack, gecko, bone, renderType, bufferSource, buffer, false, partialTick, combinedLight, packedOverlay, red, green, blue, alpha);
//
//                    poseStack.popPose();
//                }
//            }
//        }
    }
}
