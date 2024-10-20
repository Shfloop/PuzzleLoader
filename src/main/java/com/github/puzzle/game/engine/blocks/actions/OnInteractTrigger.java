package com.github.puzzle.game.engine.blocks.actions;

import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.block.IModBlock;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blockevents.actions.ActionId;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;

import java.util.Map;

@ActionId(id = "puzzle-loader:mod_block_interact")
public class OnInteractTrigger implements IBlockAction {

    public Identifier blockId;

    @Override
    public void act(BlockEventArgs args) {
        IModBlock block = PuzzleRegistries.BLOCKS.get(blockId);
        block.onInteract(args);
    }
}