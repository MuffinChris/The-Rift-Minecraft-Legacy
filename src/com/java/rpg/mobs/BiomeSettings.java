package com.java.rpg.mobs;

import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BiomeSettings {

    Map<List<Biome>, List<EntityType>> bs;

    public BiomeSettings() {
        bs = new LinkedHashMap<>();

        List<Biome> grassy = new ArrayList<>();
        grassy.add(Biome.PLAINS);
        grassy.add(Biome.SUNFLOWER_PLAINS);
        grassy.add(Biome.FOREST);
        grassy.add(Biome.FLOWER_FOREST);
        grassy.add(Biome.BIRCH_FOREST);
        grassy.add(Biome.TALL_BIRCH_FOREST);
        grassy.add(Biome.TALL_BIRCH_HILLS);

        List<EntityType> grassyMobs = new ArrayList<>();
        grassyMobs.add(EntityType.ZOMBIE);

    }

}
