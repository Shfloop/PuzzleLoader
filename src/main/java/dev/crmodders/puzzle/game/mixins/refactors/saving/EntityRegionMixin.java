package dev.crmodders.puzzle.game.mixins.refactors.saving;

import com.badlogic.gdx.utils.ByteArray;
import finalforeach.cosmicreach.io.CosmicReachBinarySerializer;
import finalforeach.cosmicreach.world.EntityRegion;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.reflect.Field;

import static finalforeach.cosmicreach.world.EntityRegion.getEntityRegionFolderName;

@Mixin(EntityRegion.class)
public class EntityRegionMixin {

    /**
     * @author Mr_Zombii
     * @reason Add NBT
     */
    @Overwrite
    public static String getEntityRegionFileName(Zone zone, int regionX, int regionY, int regionZ) {
        String regionFolderName = getEntityRegionFolderName(zone);
        CosmicReachBinarySerializer serializer = new CosmicReachBinarySerializer();
        serializer.writeByte("test", (byte) 10);
        Field field = null;
        int byteLen = 0;
        try {
            field = CosmicReachBinarySerializer.class.getDeclaredField("bytes");
            field.setAccessible(true);
            byteLen = ((ByteArray) field.get(serializer)).size;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String regionFileName = regionFolderName + "/entityRegion_" + regionX + "_" + regionY + "_" + regionZ + (byteLen != 0 ? ".crbin" : ".dat");
        return regionFileName;
    }

}