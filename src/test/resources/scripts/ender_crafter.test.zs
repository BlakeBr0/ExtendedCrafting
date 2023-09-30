mods.extendedcrafting.EnderCrafting.addShaped("test_shapedsgdfs", <item:minecraft:stick>, [
  [<item:minecraft:diamond>, <item:minecraft:air>],
  [<item:minecraft:diamond>, <item:minecraft:diamond>],
  [<item:minecraft:diamond>, <item:minecraft:air>]
], 500);

mods.extendedcrafting.EnderCrafting.addShapeless("test_shapelessasd", <item:minecraft:stone>, [
  <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:gold_ingot>, <item:minecraft:diamond>
]);

var recipes = <recipetype:extendedcrafting:ender_crafter>.getAllRecipes();

println("There are " + recipes.length + " ender crafter recipes");