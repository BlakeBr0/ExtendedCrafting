# Extended Crafting [![](http://cf.way2muchnoise.eu/full_268387_downloads.svg)](https://minecraft.curseforge.com/projects/extended-crafting)
Adds some new ways to craft items, as well as extra crafting items and utilities.

[Documentation](https://blakesmods.com/docs/extendedcrafting)

## Download

The official release builds can be downloaded from the following websites.

- [Blake's Mods](https://blakesmods.com/extended-crafting/download)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/extended-crafting)
- [Modrinth](https://modrinth.com/mod/extended-crafting)

## Development

To use this mod in a development environment, you will need to add the following to your `build.gradle`.

```groovy
repositories {
    maven {
        url 'https://maven.blakesmods.com'
    }
}

dependencies {
    implementation fg.deobf('com.blakebr0.cucumber:Cucumber:<minecraft_version>-<mod_version>')
    implementation fg.deobf('com.blakebr0.extendedcrafting:ExtendedCrafting:<minecraft_version>-<mod_version>')
}
```

## License

[MIT License](./LICENSE)
