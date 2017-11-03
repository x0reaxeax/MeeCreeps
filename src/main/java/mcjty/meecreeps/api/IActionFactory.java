package mcjty.meecreeps.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A factory for actions
 */
public interface IActionFactory {

    /**
     * Return true if this action is possible given the targetted block and
     * surroundings
     */
    boolean isPossible(World world, BlockPos pos);

    /**
     * Return true if this action is possible given the targetted block
     * but maybe not with surroundings. i.e. it is possible to do this but
     * some items may be missing or some circumstances may be less ideal for this
     * task
     */
    boolean isPossibleSecondary(World world, BlockPos pos);

    /**
     * Actually create the action
     */
    IActionWorker createWorker(IWorkerHelper helper);
}
