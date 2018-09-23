package com.blakebr0.extendedcrafting.lib;

public class ViewRecipeInfo {

	public static final ViewRecipeInfo BASIC = new ViewRecipeInfo(176, 170, 0, -23, 19, -4, 124, 36);
	public static final ViewRecipeInfo ADVANCED = new ViewRecipeInfo(176, 206, 0, 13, 1, -4, 142, 53);
	public static final ViewRecipeInfo ELITE = new ViewRecipeInfo(200, 242, 12, 49, -5, -4, 172, 71);
	public static final ViewRecipeInfo ULTIMATE = new ViewRecipeInfo(234, 278, 31, 85, -5, -4, 206, 89);
	
	public int width;
	public int height;
	public int invOffsetX;
	public int invOffsetY;
	public int gridStartX;
	public int gridStartY;
	public int outputX;
	public int outputY;
	
	public ViewRecipeInfo(int width, int height, int invOffsetX, int invOffsetY, int gridStartX, int gridStartY, int outputX, int outputY) {
		this.width = width;
		this.height = height;
		this.invOffsetX = invOffsetX;
		this.invOffsetY = invOffsetY;
		this.gridStartX = gridStartX;
		this.gridStartY = gridStartY;
		this.outputX = outputX;
		this.outputY = outputY;
	}
}
