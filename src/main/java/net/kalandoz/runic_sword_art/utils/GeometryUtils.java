package net.kalandoz.runic_sword_art.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public final class GeometryUtils {

    public static Vector3d getCentre(BlockPos pos){
        return new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
    }

    public static Vector3d getFaceCentre(BlockPos pos, Direction face){
        return getCentre(pos).add(new Vector3d(face.toVector3f()).scale(0.5));
    }

    public static double component(Vector3d vec, Direction.Axis axis){
        return new double[]{vec.x, vec.y, vec.z}[axis.ordinal()]; // Wow, that's compact.
    }

}