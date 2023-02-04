# Extended Crafting

<p align="left">
    <a href="https://blakesmods.com/extended-crafting" alt="Downloads">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/extendedcrafting/downloads&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/extended-crafting" alt="Latest Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/extendedcrafting/version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/extended-crafting" alt="Minecraft Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/extendedcrafting/mc_version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/docs/extendedcrafting" alt="Docs">
        <img src="https://img.shields.io/static/v1?label=docs&message=view&color=brightgreen&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/wiki/extendedcrafting" alt="Wiki">
        <img src="https://img.shields.io/static/v1?label=wiki&message=view&color=brightgreen&style=for-the-badge" />
    </a>
</p>

Adds some new ways to craft items, as well as extra crafting items and utilities.

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
