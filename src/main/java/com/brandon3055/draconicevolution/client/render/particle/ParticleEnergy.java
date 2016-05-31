package com.brandon3055.draconicevolution.client.render.particle;

import com.brandon3055.brandonscore.client.particle.BCEntityFX;
import com.brandon3055.brandonscore.client.particle.IBCParticleFactory;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.Utils;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleEnergy extends BCEntityFX {

    public Vec3D targetPos;

    public ParticleEnergy(World worldIn, Vec3D pos) {
        super(worldIn, pos);
    }

    public ParticleEnergy(World worldIn, Vec3D pos, Vec3D targetPos) {
        super(worldIn, pos, new Vec3D(0, 0, 0));
        this.targetPos = targetPos;
        this.particleMaxAge = 3000;
        this.particleScale = 1F;
        this.particleTextureIndexY = 1;
    }

    @Override
    public boolean func_187111_c() {
        return true;
    }

    @Override
	public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        particleTextureIndexX = rand.nextInt(5);

        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(posX, posY, posZ), targetPos);
        double speed = 0.5D;
        xSpeed = dir.x * speed;
        ySpeed = dir.y * speed;
        zSpeed = dir.z * speed;
        moveEntityNoClip(xSpeed, ySpeed, zSpeed);

        if (particleAge++ > particleMaxAge || Utils.getDistanceAtoB(posX, posY, posZ, targetPos.x, targetPos.y, targetPos.z) < 0.5) {
            setExpired();
        }
	}

	@Override
	//@SideOnly(Side.CLIENT)
    public void renderParticle(VertexBuffer vertexbuffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float minU = (float)this.particleTextureIndexX / 8.0F;
        float maxU = minU + 0.125F;
        float minV = (float)this.particleTextureIndexY / 8.0F;
        float maxV = minV + 0.125F;
        float scale = 0.1F * this.particleScale;

        float renderX = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float renderY = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float renderZ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int brightnessForRender = this.getBrightnessForRender(partialTicks);
        int j = brightnessForRender >> 16 & 65535;
        int k = brightnessForRender & 65535;
        vertexbuffer.pos((double)(renderX - rotationX * scale - rotationXY * scale), (double)(renderY - rotationZ * scale), (double)(renderZ - rotationYZ * scale - rotationXZ * scale)).tex((double)maxU, (double)maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos((double)(renderX - rotationX * scale + rotationXY * scale), (double)(renderY + rotationZ * scale), (double)(renderZ - rotationYZ * scale + rotationXZ * scale)).tex((double)maxU, (double)minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos((double)(renderX + rotationX * scale + rotationXY * scale), (double)(renderY + rotationZ * scale), (double)(renderZ + rotationYZ * scale + rotationXZ * scale)).tex((double)minU, (double)minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        vertexbuffer.pos((double)(renderX + rotationX * scale - rotationXY * scale), (double)(renderY - rotationZ * scale), (double)(renderZ + rotationYZ * scale - rotationXZ * scale)).tex((double)minU, (double)maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//		float minU = 0.0F + 0F;//(float)this.particleTextureIndexX / 32.0F;
//		float maxU = 0.0F + 0.1245F;//minU + 0.124F;
//		float minV = 0F;//(float)this.particleTextureIndexY / 32.0F;
//		float maxV = 0.1245F;//minV + 0.124F;
//		float drawScale = 0.1F * this.particleScale;
////        float minU = (float)this.particleTextureIndexX / 16.0F;
////        float maxU = minU + 0.0624375F;
////        float minV = (float)this.particleTextureIndexY / 16.0F;
////        float maxV = minV + 0.0624375F;
////        float drawScale = 0.1F * this.particleScale;
//
//        if (this.particleTexture != null)
//        {
//            minU = this.particleTexture.getMinU();
//            maxU = this.particleTexture.getMaxU();
//            minV = this.particleTexture.getMinV();
//            maxV = this.particleTexture.getMaxV();
//        }
//
//        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
//        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
//        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
//        int i = 1;//this.getBrightnessForRender(partialTicks);
//        int j = i >> 16 & 65535;
//        int k = i & 65535;
//        worldRendererIn.pos((double)(f5 - rotationX * drawScale - rotationXY * drawScale), (double)(f6 - rotationZ * drawScale), (double)(f7 - rotationYZ * drawScale - rotationXZ * drawScale)).tex((double)maxU, (double)maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        worldRendererIn.pos((double)(f5 - rotationX * drawScale + rotationXY * drawScale), (double)(f6 + rotationZ * drawScale), (double)(f7 - rotationYZ * drawScale + rotationXZ * drawScale)).tex((double)maxU, (double)minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        worldRendererIn.pos((double)(f5 + rotationX * drawScale + rotationXY * drawScale), (double)(f6 + rotationZ * drawScale), (double)(f7 + rotationYZ * drawScale + rotationXZ * drawScale)).tex((double)minU, (double)minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        worldRendererIn.pos((double)(f5 + rotationX * drawScale - rotationXY * drawScale), (double)(f6 - rotationZ * drawScale), (double)(f7 + rotationYZ * drawScale - rotationXZ * drawScale)).tex((double)minU, (double)maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();

	}

    public static class Factory implements IBCParticleFactory {

        @Override
        public EntityFX getEntityFX(int particleID, World world, Vec3D pos, Vec3D speed, int... args) {
            ParticleEnergy particleEnergy = new ParticleEnergy(world, pos, speed);

            if (args.length >= 3){
                particleEnergy.setRBGColorF(args[0] / 255F, args[1] / 255F, args[2] / 255F);
            }

            if (args.length >= 4){
                particleEnergy.multipleParticleScaleBy(args[3] / 100F);
            }

            return particleEnergy;
        }
    }
}