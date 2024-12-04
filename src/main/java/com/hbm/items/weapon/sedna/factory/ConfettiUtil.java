package com.hbm.items.weapon.sedna.factory;

import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.particle.helper.AshesCreator;
import com.hbm.particle.helper.SkeletonCreator;
import com.hbm.util.DamageResistanceHandler.DamageClass;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class ConfettiUtil {
	
	public static void decideConfetti(EntityLivingBase entity, DamageSource source) {
		if(source.damageType.equals(DamageClass.LASER.name())) pulverize(entity);
		if(source.damageType.equals(DamageClass.EXPLOSIVE.name())) gib(entity);
		skullanize(entity);
	}

	public static void pulverize(EntityLivingBase entity) {
		if(entity.isEntityAlive()) return;
		int amount = MathHelper.clamp_int((int) (entity.width * entity.height * entity.width * 25), 5, 50);
		AshesCreator.composeEffect(entity.worldObj, entity, amount, 0.125F);
	}

	public static void skullanize(EntityLivingBase entity) {
		if(entity.isEntityAlive()) return;
		SkeletonCreator.composeEffect(entity.worldObj, entity);
	}

	public static void gib(EntityLivingBase entity) {
		NBTTagCompound vdat = new NBTTagCompound();
		vdat.setString("type", "giblets");
		vdat.setInteger("ent", entity.getEntityId());
		PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(vdat, entity.posX, entity.posY + entity.height * 0.5, entity.posZ), new TargetPoint(entity.dimension, entity.posX, entity.posY + entity.height * 0.5, entity.posZ, 150));
		entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.zombie.woodbreak", 2.0F, 0.95F + entity.getRNG().nextFloat() * 0.2F);
	}
}
