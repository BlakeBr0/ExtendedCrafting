mods.extendedcrafting.CombinationCrafting.addRecipe("test", <item:minecraft:arrow>, 100000, [<item:minecraft:potato>, <item:minecraft:potato>]);
mods.extendedcrafting.CombinationCrafting.addRecipe("test22222", <item:minecraft:stick> * 10, 10000, [<item:minecraft:diamond>, <tag:items:forge:ingots/iron>, <item:minecraft:stick>]);

//mods.extendedcrafting.CombinationCrafting.addRecipe("639dc8c0-9ddb-4501-8df5-c1e98311972c", <item:extendedcrafting:frame>, 100000, [
//	<item:extendedcrafting:compressor>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>
//]);

var recipes = <recipetype:extendedcrafting:combination>.getAllRecipes();

println("There are " + recipes.length + " combination crafting recipes");