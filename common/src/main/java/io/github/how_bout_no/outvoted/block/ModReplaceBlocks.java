package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.*;

public class ModReplaceBlocks {
    public static class Stairs extends StairsBlock {
        public Stairs(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }

    public static class WoodenButton extends WoodenButtonBlock {
        public WoodenButton(Settings settings) {
            super(settings);
        }
    }

    public static class PressurePlate extends PressurePlateBlock {
        public PressurePlate(ActivationRule type, Settings settings) {
            super(type, settings);
        }
    }

    public static class Trapdoor extends TrapdoorBlock {
        public Trapdoor(Settings settings) {
            super(settings);
        }
    }

    public static class Door extends DoorBlock {
        public Door(Settings settings) {
            super(settings);
        }
    }
}
