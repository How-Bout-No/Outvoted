package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.GlareModel;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;

@Environment(EnvType.CLIENT)
public class GlareRenderer extends GeoMobRenderer<GlareEntity> {
    public GlareRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GlareModel());
    }

    private static final Identifier DEFAULT = new Identifier(Outvoted.MOD_ID, "textures/entity/glare/glare.png");
    private static final Identifier ANGRY = new Identifier(Outvoted.MOD_ID, "textures/entity/glare/glare_angry.png");

    @Override
    public RenderLayer getRenderType(GlareEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public Identifier getTexture(GlareEntity entity) {
        return entity.isAngry() ? ANGRY : DEFAULT;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("body")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
            stack.push();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-75));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.4D, 0.3D, 0.6D);
            //Sets the scaling of the item.
            stack.scale(1.0f, 1.0f, 1.0f);
            // Change mainHand to predefined Itemstack and Mode to what transform you would want to use.
            MinecraftClient.getInstance().getItemRenderer().renderItem(mainHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}