package dev.crmodders.puzzle.core.game.mixins.loot;

import com.badlogic.gdx.utils.ObjectMap;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.core.game.PuzzleRegistries;
import dev.crmodders.puzzle.core.game.loot.PuppetLootClass;
import dev.crmodders.puzzle.core.game.loot.PuzzleLootTable;
import dev.crmodders.puzzle.core.loader.registries.IRegistry;
import dev.crmodders.puzzle.core.loader.registries.NotStorableException;
import dev.crmodders.puzzle.core.loader.registries.RegistryObject;
import finalforeach.cosmicreach.items.loot.Loot;
import javassist.NotFoundException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Loot.class)
public class LootTableMixin {

    @Shadow public static ObjectMap<String, Loot> lootMap;

    @Redirect(method = "loadLoot", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/loot/Loot;registerLoot(Lfinalforeach/cosmicreach/items/loot/Loot;)V"))
    private static void registerLoot0(Loot loot) {
        if (lootMap.containsKey(loot.lootId)) {
            throw new RuntimeException("Cannot register loot with a duplicate id: " + loot.lootId);
        } else {
            lootMap.put(loot.lootId, loot);
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Force the game to get the new lootTables
     * @see PuzzleLootTable
     */
    @Overwrite
    public static Loot get(String lootId) {
        try {
            Identifier id = Identifier.fromString(lootId);
            RegistryObject<PuzzleLootTable> table = new RegistryObject<>(PuzzleRegistries.PuzzleLootTables, id);
            return new PuppetLootClass(id, table.getObject());
        } catch (NotStorableException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Force the game to use my new lootTable
     */
    @Overwrite
    public static void registerLoot(Loot loot) {
        Pair<Identifier, PuzzleLootTable> table = PuzzleLootTable.fromVanillaTable(loot);
        IRegistry.register(PuzzleRegistries.PuzzleLootTables, table.getLeft(), table::getRight);
    }

    @Inject(method = "loadLoot", at = @At("TAIL"))
    private static void lootLootTables(CallbackInfo ci) {
        for (Loot loot : lootMap.values()) {
            Pair<Identifier, PuzzleLootTable> table = PuzzleLootTable.fromVanillaTable(loot);
            IRegistry.register(PuzzleRegistries.PuzzleLootTables, table.getLeft(), table::getRight);
        }
        lootMap.clear();
        PuzzleRegistries.PuzzleLootTables.freeze();
    }

}
