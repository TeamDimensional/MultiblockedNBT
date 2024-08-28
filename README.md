# Multiblocked NBT

Multiblocked NBT is an addon [Multiblocked](https://www.curseforge.com/minecraft/mc-mods/multiblocked) for Minecraft 1.12.
It allows writing recipes that rely on or modify NBT data in items and/or fluids. This is a (theoretical) example of a recipe
that can be implemented through MultiNBT: "Consume any item with Smite and a Broken Spawner - Zombie to insert 27 stacks of Rotten Flesh into a shulker box in an input slot".

## Supported mods

The following mods are supported out of the box. You can also add your own requirements and modifiers if you want! Check the last section.

Anything in quotes can be substituted by the modpack developer.

### Vanilla

* Requirement: `Input item must be of type 'Iron Axe'`
* Requirement: `Input item requires enchantment 'Sharpness' of at least level '2'`
* Modifier: `Add enchantment 'Fire Aspect' of level '3' to input item`
* Fluid requirement: `Input fluid must be of type 'Water'`

### Deep Resonance

* Requirement: `Input fluid must be RCL with 'Purity' >= '0.5'`
* Modifier: `Increase or reduce the 'Purity' of input RCL by '0.5'`

## Warnings

Currently Multiblocked NBT requires adding a Trait to your multiblock, which adds an ugly UI to it. Not adding traits may cause the multiblock to crash.
I'm looking into ways to remedy that.

## Writing addons

Take a look at [this package](https://github.com/TeamDimensional/MultiblockedNBT/tree/master/src/main/java/com/teamdimensional/multiblockednbt/api).
You want to implement the classes `INBTModifier` and/or `INBTRequirement` with either `T = ItemStack` or `T = FluidStack`.
If you want to add a new option for `T`, it is a bit more convoluted and not described here.
