package com.valeriotor.beyondtheveil.dreaming.dreams;

import com.valeriotor.beyondtheveil.dreaming.Memory;
import com.valeriotor.beyondtheveil.networking.GenericToClientPacket;
import com.valeriotor.beyondtheveil.networking.Messages;
import com.valeriotor.beyondtheveil.util.DataUtil;
import com.valeriotor.beyondtheveil.util.WaypointType;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import java.util.concurrent.Executor;

public class DreamWater extends Dream{

    public DreamWater() {
        super(Memory.WATER, 5, ReminiscenceWaypoint::new);
    }

    @Override
    public boolean activate(Player p, Level l) {
        return activatePos(p, l, p.getOnPos());
    }

    @Override
    public boolean activatePlayer(Player caster, Player target, Level l) {
        return false;
    }

    @Override
    public boolean activatePos(Player p, Level l, BlockPos pos) {
        ServerLevel sl = (ServerLevel) l;
        BlockPos blockpos = sl.findNearestMapFeature(ConfiguredStructureTags.ON_OCEAN_EXPLORER_MAPS, pos, 100, false);
        if (blockpos != null) {
            //DataUtil.createWaypoint(p, WaypointType.OCEAN_MONUMENT, 20*600, blockpos);
            Reminiscence r = new ReminiscenceWaypoint(blockpos, 0x7F16FF);
            DataUtil.addReminiscence(p, Memory.WATER, r);
            return true;
        }
        return false;
    }
}
