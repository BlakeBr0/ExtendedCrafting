<recipetype:extendedcrafting:combination>.addRecipe("test_combination", <item:minecraft:arrow>, <item:minecraft:carrot>, [<item:minecraft:potato>, <item:minecraft:potato>], 100000);
<recipetype:extendedcrafting:combination>.addRecipe("test_combination_many_outputs", <item:minecraft:stick> * 10, <item:minecraft:diamond>, [<item:minecraft:diamond>, <tag:item:c:ingots/iron>, <item:minecraft:stick>], 10000);

<recipetype:extendedcrafting:combination>.addRecipe("639dc8c0-9ddb-4501-8df5-c1e98311972c", <item:extendedcrafting:frame>, <item:extendedcrafting:compressor>, [
	<item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>
], 100000);

<recipetype:extendedcrafting:combination>.addRecipe("test_combination_transformer", <item:minecraft:cobblestone>, <item:minecraft:carrot>, [
	<item:minecraft:hopper>.reuse(), <item:minecraft:iron_ingot>, <item:minecraft:gold_ingot>
], 100000);

var recipes = <recipetype:extendedcrafting:combination>.allRecipes;

println("There are " + recipes.length + " combination crafting recipes");