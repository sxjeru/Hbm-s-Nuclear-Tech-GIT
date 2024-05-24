package com.hbm.inventory.gui;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.ContainerMachineSolderingStation;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.TileEntityMachineSolderingStation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIMachineSolderingMachine extends GuiInfoContainer {

	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/processing/gui_soldering_station.png");
	private TileEntityMachineSolderingStation solderer;

	public GUIMachineSolderingMachine(InventoryPlayer playerInv, TileEntityMachineSolderingStation tile) {
		super(new ContainerMachineSolderingStation(playerInv, tile));
		
		this.solderer = tile;
		this.xSize = 176;
		this.ySize = 204;
	}
	
	@Override
	public void drawScreen(int x, int y, float interp) {
		super.drawScreen(x, y, interp);

		solderer.tank.renderTankInfo(this, x, y, guiLeft + 35, guiTop + 63, 34, 16);
		this.drawElectricityInfo(this, x, y, guiLeft + 152, guiTop + 18, 16, 52, solderer.getPower(), solderer.getMaxPower());
		
		this.drawCustomInfoStat(x, y, guiLeft + 78, guiTop + 67, 8, 8, guiLeft + 78, guiTop + 67, this.getUpgradeInfo(solderer));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String name = this.solderer.hasCustomInventoryName() ? this.solderer.getInventoryName() : I18n.format(this.solderer.getInventoryName());
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2 - 18, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float interp, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int p = (int) (solderer.power * 52 / solderer.maxPower);
		drawTexturedModalRect(guiLeft + 152, guiTop + 70 - p, 176, 52 - p, 16, p);
		
		int i = solderer.progress * 33 / solderer.processTime;
		drawTexturedModalRect(guiLeft + 72, guiTop + 28, 192, 0, i, 14);
		
		if(solderer.power >= solderer.consumption) {
			drawTexturedModalRect(guiLeft + 156, guiTop + 4, 176, 52, 9, 12);
		}

		this.drawInfoPanel(guiLeft + 78, guiTop + 67, 8, 8, 8);
		solderer.tank.renderTank(guiLeft + 35, guiTop + 79, this.zLevel, 34, 16, 1);
	}
}
