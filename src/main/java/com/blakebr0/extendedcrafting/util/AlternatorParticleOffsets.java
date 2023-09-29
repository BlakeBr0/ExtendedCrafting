package com.blakebr0.extendedcrafting.util;

import net.minecraft.core.Direction;

import java.util.Map;

public enum AlternatorParticleOffsets {
    DOWN(Direction.DOWN, 0.5D, -0.25D, 0.5D),
    UP(Direction.UP, 0.5D, 1.0D, 0.5D),
    NORTH(Direction.NORTH, 0.5D, 0.5D, 0.0D),
    SOUTH(Direction.SOUTH, 0.5D, 0.5D, 1.0D),
    EAST(Direction.EAST, 1.0D, 0.5D, 0.5D),
    WEST(Direction.WEST, 0.0D, 0.5D, 0.5D);

    private static final Map<Direction, AlternatorParticleOffsets> BY_DIRECTION = Map.of(
            Direction.DOWN, DOWN,
            Direction.UP, UP,
            Direction.NORTH, NORTH,
            Direction.SOUTH, SOUTH,
            Direction.EAST, EAST,
            Direction.WEST, WEST
    );

    public final Direction direction;
    public final double x;
    public final double y;
    public final double z;

    AlternatorParticleOffsets(Direction direction, double x, double y, double z) {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static AlternatorParticleOffsets fromDirection(Direction direction) {
        return BY_DIRECTION.get(direction);
    }
}
