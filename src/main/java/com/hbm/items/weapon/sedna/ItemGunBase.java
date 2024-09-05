package com.hbm.items.weapon.sedna;

import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.items.IKeybindReceiver;
import com.hbm.util.EnumUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGunBase extends Item implements IKeybindReceiver {

	public static final String KEY_DRAWN = "drawn";
	public static final String KEY_TIMER = "timer";
	public static final String KEY_STATE = "state";
	public static final String KEY_PRIMARY = "mouse1";
	public static final String KEY_SECONDARY = "mouse2";
	public static final String KEY_TERTIARY = "mouse3";
	public static final String KEY_RELOAD = "reload";
	
	/** NEVER ACCESS DIRECTLY - USE GETTER */
	private GunConfig config_DNA;
	
	public GunConfig getConfig(ItemStack stack) {
		return config_DNA;
	}

	public static enum GunState {
		DRAWING,	//initial delay after selecting
		IDLE,		//gun can be fired or reloaded
		WINDUP,		//fire button is down, added delay before fire
		JUST_FIRED,	//gun has been fired, cooldown
		RELOADING	//gun is currently reloading
	}
	
	@Override
	public void handleKeybind(EntityPlayer player, ItemStack stack, EnumKeybind keybind, boolean state) {
		
		GunConfig config = getConfig(stack);

		if(keybind == EnumKeybind.GUN_PRIMARY &&	state && !getPrimary(stack)) {		if(config.onPressPrimary != null)		config.onPressPrimary.accept(stack, config);		return; }
		if(keybind == EnumKeybind.GUN_PRIMARY &&	!state && getPrimary(stack)) {		if(config.onReleasePrimary != null)		config.onReleasePrimary.accept(stack, config);		return; }
		if(keybind == EnumKeybind.GUN_SECONDARY &&	state && !getSecondary(stack)) {	if(config.onPressSecondary != null)		config.onPressSecondary.accept(stack, config);		return; }
		if(keybind == EnumKeybind.GUN_SECONDARY &&	!state && getSecondary(stack)) {	if(config.onReleaseSecondary != null)	config.onReleaseSecondary.accept(stack, config);	return; }
		if(keybind == EnumKeybind.GUN_TERTIARY &&	state && !getTertiary(stack)) {		if(config.onPressTertiary != null)		config.onPressTertiary.accept(stack, config);		return; }
		if(keybind == EnumKeybind.GUN_TERTIARY &&	!state && getTertiary(stack)) {		if(config.onReleaseTertiary != null)	config.onReleaseTertiary.accept(stack, config);		return; }
		if(keybind == EnumKeybind.RELOAD &&			state && !getReloadKey(stack)) {	if(config.onPressReload != null)		config.onPressReload.accept(stack, config);			return; }
		if(keybind == EnumKeybind.RELOAD &&			!state && getReloadKey(stack)) {	if(config.onReleaseReload != null)		config.onReleaseReload.accept(stack, config);		return; }
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
		if(world.isRemote) return;
		
		GunConfig config = this.getConfig(stack);
		
		if(!isHeld) {
			this.setState(stack, GunState.DRAWING);
			this.setTimer(stack, config.drawDuration);
			return;
		}
		
		int timer = this.getTimer(stack);
		if(timer > 0) this.setTimer(stack, timer - 1);
		if(timer <= 1) nextState();
	}
	
	public void nextState() {
		// run the decider
	}

	// GUN DRAWN //
	public static boolean getIsDrawn(ItemStack stack) { return getValueBool(stack, KEY_DRAWN); }
	public static void setIsDrawn(ItemStack stack, boolean value) { setValueBool(stack, KEY_DRAWN, value); }

	// GUN STATE TIMER //
	public static int getTimer(ItemStack stack) { return getValueInt(stack, KEY_TIMER); }
	public static void setTimer(ItemStack stack, int value) { setValueInt(stack, KEY_TIMER, value); }

	// GUN STATE //
	public static GunState getState(ItemStack stack) { return EnumUtil.grabEnumSafely(GunState.class, getValueByte(stack, KEY_STATE)); }
	public static void setState(ItemStack stack, GunState value) { setValueByte(stack, KEY_STATE, (byte) value.ordinal()); }
	
	// BUTTON STATES //
	public static boolean getPrimary(ItemStack stack) { return getValueBool(stack, KEY_PRIMARY); }
	public static void setPrimary(ItemStack stack, boolean value) { setValueBool(stack, KEY_PRIMARY, value); }
	public static boolean getSecondary(ItemStack stack) { return getValueBool(stack, KEY_SECONDARY); }
	public static void setSecondary(ItemStack stack, boolean value) { setValueBool(stack, KEY_SECONDARY, value); }
	public static boolean getTertiary(ItemStack stack) { return getValueBool(stack, KEY_TERTIARY); }
	public static void setTertiary(ItemStack stack, boolean value) { setValueBool(stack, KEY_TERTIARY, value); }
	public static boolean getReloadKey(ItemStack stack) { return getValueBool(stack, KEY_RELOAD); }
	public static void setReloadKey(ItemStack stack, boolean value) { setValueBool(stack, KEY_RELOAD, value); }
	
	
	/// UTIL ///
	public static int getValueInt(ItemStack stack, String name) { if(stack.hasTagCompound()) stack.getTagCompound().getInteger(name); return 0; }
	public static void setValueInt(ItemStack stack, String name, int value) { if(!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound(); stack.getTagCompound().setInteger(name, value); }
	
	public static byte getValueByte(ItemStack stack, String name) { if(stack.hasTagCompound()) stack.getTagCompound().getByte(name); return 0; }
	public static void setValueByte(ItemStack stack, String name, byte value) { if(!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound(); stack.getTagCompound().setByte(name, value); }
	
	public static boolean getValueBool(ItemStack stack, String name) { if(stack.hasTagCompound()) stack.getTagCompound().getBoolean(name); return false; }
	public static void setValueBool(ItemStack stack, String name, boolean value) { if(!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound(); stack.getTagCompound().setBoolean(name, value); }
}
