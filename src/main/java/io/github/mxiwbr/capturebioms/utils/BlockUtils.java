package io.github.mxiwbr.capturebioms.utils;

import io.github.mxiwbr.capturebioms.CaptureBioms;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlockUtils {

    /**
     * Gets the next solid block above a given location.
     * Returns the max height limit if there is no block above
     * @param location
     */
    public static int getNextSolidBlockY(Location location) {

        int y = location.getBlockY() + 1;

        while (y < location.getWorld().getMaxHeight() &&
                location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).isPassable()) {
            y++;
        }

        return y;
    }

    /**
     * Creates a BoundingBox from a center location as well as size and height, just like
     * ParticleFactory.createSquareRisingEdges() works.
     * Goes down 5 blocks below the center location and up to the height coordinate.
     * @param center
     * @param size
     * @param height absolute y coordinate of highest point
     * @return BoundingBox
     */
    public static BoundingBox getBoundingBox (Location center, int size, double height) {

        double half = size / 2.0;

        double minX = center.getX() - half;
        double maxX = center.getX() + half;

        double minZ = center.getZ() - half;
        double maxZ = center.getZ() + half;

        double minY = center.getY() - 5;

        return new BoundingBox(minX, minY, minZ, maxX, height, maxZ);

    }

    public static ArrayList<Chunk> getChunksFromBoundingBox (BoundingBox boundingBox, World world) {

        var chunkList = new ArrayList<Chunk>();

        // Get min and max chunk coordinates for iterating
        int minChunkX = (int) Math.floor(boundingBox.getMinX() / 16.0);
        int maxChunkX = (int) Math.floor(boundingBox.getMaxX() / 16.0);
        int minChunkZ = (int) Math.floor(boundingBox.getMinZ() / 16.0);
        int maxChunkZ = (int) Math.floor(boundingBox.getMaxZ() / 16.0);

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {

            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {

                chunkList.add(world.getChunkAt(cx, cz));

            }

        }

        return chunkList;

    }


}
