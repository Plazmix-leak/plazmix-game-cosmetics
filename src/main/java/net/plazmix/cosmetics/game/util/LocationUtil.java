package net.plazmix.cosmetics.game.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;

/**
 * Created by 0xC0d3r on 27/06/2020
 */
public class LocationUtil {
    
    public static HashMap<Block, Double> getBlocksInRadius(Location loc, double dR) {
        return getBlocksInRadius(loc, dR, 9999.0D);
    }
    
    public static HashMap<Block, Double> getBlocksInRadius(Location loc, double dR, double maxHeight) {
        HashMap<Block, Double> blockList = new HashMap<>();
        int iR = (int) dR + 1;
        for (int x = -iR; x <= iR; x++) {
            for (int z = -iR; z <= iR; z++) {
                for (int y = -iR; y <= iR; y++) {
                    if (Math.abs(y) <= maxHeight) {
                        Block curBlock = loc.getWorld().getBlockAt((int) (loc.getX() + x), (int) (loc.getY() + y), (int) (loc.getZ() + z));
                        double offset = MathUtil.offset(loc, curBlock.getLocation().add(0.5D, 0.5D, 0.5D));
                        if (offset <= dR)
                            blockList.put(curBlock, 1.0D - offset / dR);
                    }
                }
            }
        }
        return blockList;
    }
    
    public static HashMap<Block, Double> getInRadius(Block block, double dR, boolean hollow) {
        HashMap<Block, Double> blockList = new HashMap<>();
        int iR = (int) dR + 1;
        for (int x = -iR; x <= iR; x++) {
            for (int z = -iR; z <= iR; z++) {
                for (int y = -iR; y <= iR; y++) {
                    Block curBlock = block.getRelative(x, y, z);
                    double offset = MathUtil.offset(block.getLocation(), curBlock.getLocation());
                    if (offset <= dR && (!hollow || offset >= dR - 1.0D))
                        blockList.put(curBlock, 1.0D - offset / dR);
                }
            }
        }
        return blockList;
    }
}
