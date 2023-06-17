package com.graphics_2d.world.entities;

import com.graphics_2d.util.PointI;
import com.graphics_2d.world.*;
import com.graphics_2d.world.biomes.Biome;

import java.util.*;

public class Mob {
    private final String name;
    private final Set<Integer> biomes;

    private final int visionRange;
    private final ImageAsset imageAsset;

    private boolean canSwim = false;
    private int damage = 0;

    private static final Random RANDOM = new Random();

    private ObjectInstance drop = null;
    private final int id;
    private static int nextId = 0;

    public static Map<Integer, Mob> MOBS_BY_ID = new HashMap<>();

    public Mob(String name, Set<Integer> biomes, int visionRange, ImageAsset imageAsset) {
        this.name = name;
        this.biomes = biomes;
        this.visionRange = visionRange;
        this.imageAsset = imageAsset;
        this.id = nextId++;
        MOBS_BY_ID.put(id, this);
    }

    public String getName() {
        return name;
    }

    public static Mob getRandomMob() {
        return (Mob) MOBS_BY_ID.values().toArray()[RANDOM.nextInt(MOBS_BY_ID.values().size())];
    }

    public int getId() {
        return id;
    }

    public void setDrop(ObjectInstance objectInstance) {
        this.drop = objectInstance;
    }

    public ObjectInstance getDrop() {
        if (drop != null) {
            return drop.newCopy();
        }
        return null;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }

    public boolean isCanSwim() {
        return canSwim;
    }

    public Set<Integer> getBiomes() {
        return biomes;
    }

    public int getVisionRange() {
        return visionRange;
    }

    public ImageAsset getImageAsset() {
        return imageAsset;
    }

    public void update(World world, MobInstance mobInstance) {
        PointI dst = null;

        PointI pLoc = world.getPlayer().getLocation();

        PointI mLoc = mobInstance.getLocation();
        int xDist = pLoc.getX() - mLoc.getX();
        int yDist = pLoc.getY() - mLoc.getY();
        // Player Visible
        if (Math.abs(xDist) < visionRange && Math.abs(yDist) < visionRange) {
            if (Math.abs(xDist) > Math.abs(yDist)) {
                // Move x
                if (xDist > 0) {
                    dst = mLoc.delta(1, 0);
                } else {
                    dst = mLoc.delta(-1, 0);
                }
            } else {
                // Move y
                if (yDist > 0) {
                    dst = mLoc.delta(0, 1);
                } else {
                    dst = mLoc.delta(0, -1);
                }
            }
        } else {
            switch (RANDOM.nextInt(4)) {
                case 0 -> dst = mLoc.delta(1, 0); // right
                case 1 -> dst = mLoc.delta(-1, 0); // left
                case 2 -> dst = mLoc.delta(0, 1); // down
                case 3 -> dst = mLoc.delta(0, -1); // up
            }
        }
        if (!world.inBounds(dst.getX(), dst.getY())) {
            return;
        }
        Biome b = world.getBiomeAt(dst.getX(), dst.getY());
        if (!biomes.contains(b.getBiomeId())) {
            return;
        }
        Tile tile = world.getTileAt(dst.getX(), dst.getY());
        if (tile.isSwim() && !isCanSwim()) {
            return;
        }
        if (canMove(world, dst)) {
            mobInstance.setLocation(dst);
            Player player = world.getPlayer();
            if (Objects.equals(dst.getX(), pLoc.getX()) && Objects.equals(dst.getY(), pLoc.getY()) && !player.isFlying()) {
                player.takeDamage(getDamage());
                world.playerUpdated();
                world.worldUpdated();
            }
        }
    }

    public boolean canMove(World world, PointI loc) {
        return !world.getBlocking(loc.getX(), loc.getY());
    }
}
