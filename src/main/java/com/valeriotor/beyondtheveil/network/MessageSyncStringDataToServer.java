package com.valeriotor.beyondtheveil.network;

import java.util.List;

import com.google.common.collect.Lists;
import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.lib.PlayerDataLib;

import com.valeriotor.beyondtheveil.research.ResearchUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncStringDataToServer implements IMessage {

    private String[] keys;

    public MessageSyncStringDataToServer() {
    }


    public MessageSyncStringDataToServer(String... keys) {
        this.keys = keys;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        List<String> list = Lists.newArrayList();
        while (buf.isReadable())
            list.add(ByteBufUtils.readUTF8String(buf));
        this.keys = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            this.keys[i] = list.get(i);

    }


    @Override
    public void toBytes(ByteBuf buf) {
        for (String key : keys)
            ByteBufUtils.writeUTF8String(buf, key);
    }

    public static class SyncDataToServerMessageHandler implements IMessageHandler<MessageSyncStringDataToServer, IMessage> {

        @Override
        public IMessage onMessage(MessageSyncStringDataToServer message, MessageContext ctx) {
            EntityPlayerMP p = ctx.getServerHandler().player;
            p.getServerWorld().addScheduledTask(() -> {
                for (String s : message.keys) {
                    if (PlayerDataLib.allowedStrings.contains(s) || s.contains("dialogue")) {
                        p.getCapability(PlayerDataProvider.PLAYERDATA, null).addString(s, false);
                        ResearchUtil.markResearchAsUpdated(p, s);
                    }
                }
            });
            return null;
        }

    }

}
