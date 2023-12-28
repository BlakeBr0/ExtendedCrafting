mods.extendedcrafting.CombinationCrafting.addRecipe("test", <item:minecraft:arrow>, 100000, [<item:minecraft:potato>, <item:minecraft:potato>]);
mods.extendedcrafting.CombinationCrafting.addRecipe("test22222", <item:minecraft:stick> * 10, 10000, [<item:minecraft:diamond>, <tag:items:forge:ingots/iron>, <item:minecraft:stick>]);

mods.extendedcrafting.CompressionCrafting.addRecipe("testasd", <tag:items:forge:ingots/iron>, <item:minecraft:apple>, 50, <tag:items:forge:ingots/gold>, 0, 0);

mods.extendedcrafting.EnderCrafting.addShaped("test_shapedsgdfs", <item:minecraft:stick>, [
  [<item:minecraft:diamond>, <item:minecraft:air>],
  [<item:minecraft:diamond>, <item:minecraft:diamond>],
  [<item:minecraft:diamond>, <item:minecraft:air>]
], 500);

mods.extendedcrafting.EnderCrafting.addShapeless("test_shapelessasd", <item:minecraft:cobblestone>, [
  <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:gold_ingot>, <item:minecraft:diamond>
]);

mods.extendedcrafting.TableCrafting.addShaped("test_shaped4weet", 1, <item:minecraft:stick>, [
  [<tag:items:forge:gems/diamond>, <item:minecraft:air>],
  [<item:minecraft:diamond>, <item:minecraft:diamond>],
  [<item:minecraft:diamond>, <item:minecraft:air>]
]);

//mods.extendedcrafting.TableCrafting.remove(<item:extendedcrafting:crystaltine_ingot>);
//
//mods.extendedcrafting.TableCrafting.addShaped("65f26d24-ccb0-4b76-b622-5915c4ee2b72", 0, <item:minecraft:iron_sword>.withTag({Damage: 0, Enchantments: [{lvl: 1, id: "minecraft:bane_of_arthropods" as string}]}), [
//	[<item:extendedcrafting:recipe_maker>.withTag({Shapeless: 1, Type: "CraftTweaker" as string}), <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:extendedcrafting:ender_star>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:gold_ingot>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:air>, <item:minecraft:coal>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:air>, <item:extendedcrafting:ender_alternator>, <item:minecraft:air>, <item:minecraft:air>],
//	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>]
//]);

mods.extendedcrafting.TableCrafting.addShapeless("aab3beed-286c-47de-b0c2-a116bf5ada6f", 0, <item:minecraft:potato>, [
	<item:extendedcrafting:recipe_maker>.withTag({Shapeless: 1 as byte, Type: "CraftTweaker" as string}), <item:extendedcrafting:ender_star>.reuse(), <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:coal>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:extendedcrafting:ender_alternator>
]);

mods.extendedcrafting.TableCrafting.addShapeless("aab3beed-286c-47de-b0c2-a116badsf5ada6f", 0, <item:minecraft:carrot>, [
	<item:extendedcrafting:ender_star>, <item:extendedcrafting:ender_star>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:coal>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:extendedcrafting:ender_alternator>
]);

mods.extendedcrafting.TableCrafting.addShapeless("aab3beed-286c-47de-b0badsf5ada6f", 0, <item:minecraft:carrot>, [
	<item:extendedcrafting:ender_star>, <item:minecraft:diamond_pickaxe>.anyDamage().reuse(), <item:minecraft:gold_ingot>.reuse(), <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>, <item:extendedcrafting:ender_alternator>
]);

//mods.extendedcrafting.CombinationCrafting.addRecipe("639dc8c0-9ddb-4501-8df5-c1e98311972c", <item:extendedcrafting:frame>, 100000, [
//	<item:extendedcrafting:compressor>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>, <item:minecraft:hopper>
//]);

mods.extendedcrafting.TableCrafting.addShaped("6f07b4c3-12f0-4c54-9c3b-0e661c4c6f3b", 0, <item:minecraft:cobblestone>, [
	[<item:minecraft:stone>, <item:extendedcrafting:recipe_maker>.withTag({Shapeless: 0 as byte, Type: "Datapack"}), <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>],
	[<item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>, <item:minecraft:air>]
]);