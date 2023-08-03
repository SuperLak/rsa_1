package net.kalandoz.runic_sword_art.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public final class RunicUtils {

    @Nullable
    public static Integer getNearestSurface(World world, BlockPos pos, Direction direction, int range,
                                            boolean doubleSided, SurfaceCriteria criteria){

        // This is a neat trick that allows a default 'not found' return value for integers where all possible integer
        // values could, in theory, be returned. The alternative is to use a double and have NaN as the default, but
        // that would introduce extra casting, and since NaN can be calculated with, it could produce strange results
        // when unaccounted for. Using an Integer means it'll immediately throw an NPE instead.
        Integer surface = null;
        int currentBest = Integer.MAX_VALUE;

        for(int i = doubleSided ? -range : 0; i <= range && i < currentBest; i++){ // Now short-circuits for efficiency

            BlockPos testPos = pos.offset(direction, i);
            if(criteria.test(world, testPos, direction)) {
                // Because the loop now short-circuits, this must be closer than the previous surface found
                surface = (int) GeometryUtils.component(GeometryUtils.getFaceCentre(testPos, direction), direction.getAxis());
                currentBest = Math.abs(i);
            }
        }
        System.out.println("Returning surface value: " + surface);
        return surface;
    }

    @FunctionalInterface
    public interface SurfaceCriteria {

        /**
         * Tests whether the inputs define a valid surface according to this set of criteria.
         *
         * @param world The world in which the surface is to be tested.
         * @param pos   The block coordinates of the inside ('solid' part) of the surface.
         * @param side  The direction in which the surface must face.
         * @return True if the side {@code side} of the block at {@code pos} in {@code world} is a valid surface
         * according to this set of criteria, false otherwise.
         */
        boolean test(World world, BlockPos pos, Direction side);

        /** Returns a {@code SurfaceCriteria} with the opposite arrangement to this one. */
        default SurfaceCriteria flip(){
            return (world, pos, side) -> this.test(world, pos.offset(side), side.getOpposite());
        }

        /** Returns a {@code SurfaceCriteria} based on the given condition, where the inside of the surface satisfies
         * the condition and the outside does not. */
        static SurfaceCriteria basedOn(BiPredicate<World, BlockPos> condition){
            return (world, pos, side) -> condition.test(world, pos) && !condition.test(world, pos.offset(side));
        }

        /** Surface criterion which defines a surface as the boundary between any non-air block and an air block.
         * Used for particles, and is also good for placing fire. */
        // Was getNearestFloorLevelC
        SurfaceCriteria NOT_AIR_TO_AIR = basedOn(World::isAirBlock).flip();
    }
}
