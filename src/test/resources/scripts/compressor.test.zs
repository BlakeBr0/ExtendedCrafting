mods.extendedcrafting.CompressionCrafting.addRecipe("testasd", <tag:items:forge:ingots/iron>, <item:minecraft:apple>, 50, <tag:items:forge:ingots/gold>, 10000, 100);

var recipes = <recipetype:extendedcrafting:compressor>.getAllRecipes();

println("There are " + recipes.length + " compressor crafting recipes");