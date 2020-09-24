package com.grinderwolf.swm.nms.v1_16_R3;

import com.flowpowered.nbt.CompoundTag;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.nms.CraftSlimeWorld;
import net.minecraft.server.v1_16_R2.Convertable;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.GameRules;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.WorldData;
import net.minecraft.server.v1_16_R2.WorldNBTStorage;
import lombok.Getter;

@Getter
public class CustomNBTStorage extends WorldNBTStorage {

    private static final GameRules EMPTY_GAMERULES = new GameRules();

    private final SlimeWorld world;
    private WorldData worldData;

    public CustomNBTStorage(SlimeWorld world, Convertable.ConversionSession conversionSession) {
        super(conversionSession, MinecraftServer.getServer().getDataFixer());

        this.world = world;
    }

    public WorldData getWorldData() {
        if (worldData == null) {
            worldData = new CustomWorldData((CraftSlimeWorld) world);
        }

        return worldData;
    }

    public void saveWorldData(WorldData worldData) {
        CompoundTag gameRules = (CompoundTag) Converter.convertTag("gamerules", worldData.q().a()).getAsCompoundTag().get();
        CompoundTag extraData = this.world.getExtraData();

        extraData.getValue().remove("gamerules");

        if (!gameRules.getValue().isEmpty()) {
            extraData.getValue().put("gamerules", gameRules);
        }
    }

    @Override public void save(EntityHuman entityHuman) {}
    @Override public NBTTagCompound load(EntityHuman entityHuman) { return null; }
    @Override public String[] getSeenPlayers() { return new String[0]; }
}