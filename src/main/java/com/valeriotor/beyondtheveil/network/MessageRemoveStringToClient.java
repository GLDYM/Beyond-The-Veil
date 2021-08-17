package com.valeriotor.beyondtheveil.network;

import com.valeriotor.beyondtheveil.BeyondTheVeil;
import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.lib.PlayerDataLib;
import com.valeriotor.beyondtheveil.worship.ActivePowers.TransformDeepOne;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRemoveStringToClient implements IMessage{
	
	public String key;
	
	public MessageRemoveStringToClient() {}
	public MessageRemoveStringToClient(String key) {this.key = key;}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.key);
	}
	
	public static class RemoveStringToClientMessageHandler implements IMessageHandler<MessageRemoveStringToClient, IMessage>{

		@Override
		public IMessage onMessage(MessageRemoveStringToClient message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
		    final WorldClient worldClient = minecraft.world;
		    minecraft.addScheduledTask(new Runnable()
		    {
		      public void run() {
				processMessage(message, ctx);
		      }
		    });
			return null;
		}
		
		public void processMessage(MessageRemoveStringToClient message, MessageContext ctx) {
			if(Minecraft.getMinecraft().player == null) {
				System.err.println("BTV sync error: null player. No data was lost, but it wasn't synchronized to client.");
				return;
			}
			if(message.key.equals(PlayerDataLib.ALL)) {
				Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYERDATA, null).removeAllStrings();
				Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYERDATA, null).removeAllInts();
			}
			else {
				Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYERDATA, null).removeString(message.key);
				keySpecificAction(message.key);
			}
		}

		private void keySpecificAction(String key) {
			switch(key) {
				case PlayerDataLib.TRANSFORMED:
					Minecraft.getMinecraft().player.capabilities.isFlying = false;
					break;
			}
		}
		
	}

}
