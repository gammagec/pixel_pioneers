package com.graphics_2d.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObject {

    private boolean isBlocking;
    private final String name;
    private final ImageAsset imageAsset;
    private final int id;
    private int damage = 0;
    private static int nextId = 0;
    private boolean canPickup = false;
    private boolean canEat = false;
    private boolean canUse = false;
    private boolean canBuild = false;

    private int uses = 0;

    public static final Map<Integer, GameObject> OBJECTS_BY_ID = new HashMap<>();

    private final List<UseEffect> useEffects = new ArrayList<>();
    private final Map<Integer, ImageAsset> assetsAtUse = new HashMap<>();

    public GameObject(String name, ImageAsset imageAsset) {
        this.imageAsset = imageAsset;
        this.isBlocking = false;
        this.name = name;
        this.id = nextId++;
        OBJECTS_BY_ID.put(this.id, this);
    }

    public void setAssetAtUse(int use, ImageAsset asset) {
        this.assetsAtUse.put(use, asset);
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public void addUseEffect(UseEffect useEffect) {
        this.useEffects.add(useEffect);
    }

    public boolean isCanEat() {
        return canEat;
    }

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public void setCanPickup(boolean canPickup) {
        this.canPickup = canPickup;
    }

    public boolean isCanPickup() {
        return this.canPickup;
    }

    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public ImageAsset getImageAsset(int use) {
        ImageAsset assetAtUse = assetsAtUse.get(use);
        if (assetAtUse != null) {
            return assetAtUse;
        }
        return imageAsset;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public List<UseEffect> getUseEffects() {
        return useEffects;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }
}
