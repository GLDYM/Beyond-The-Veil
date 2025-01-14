package com.valeriotor.beyondtheveil.client.event;

import com.valeriotor.beyondtheveil.client.KeyBindings;
import com.valeriotor.beyondtheveil.client.reminiscence.ReminiscenceClient;
import com.valeriotor.beyondtheveil.client.util.DataUtilClient;
import com.valeriotor.beyondtheveil.dreaming.Memory;
import com.valeriotor.beyondtheveil.dreaming.dreams.Reminiscence;
import com.valeriotor.beyondtheveil.entity.NautilusEntity;
import com.valeriotor.beyondtheveil.lib.References;
import com.valeriotor.beyondtheveil.networking.GenericToServerPacket;
import com.valeriotor.beyondtheveil.networking.Messages;
import com.valeriotor.beyondtheveil.util.DataUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        ReminiscenceClient.tickEvent(event);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getVehicle() instanceof NautilusEntity nautilus && event.phase == TickEvent.Phase.END) {
                nautilus.setInput(player.input.left, player.input.right, player.input.up, player.input.down, player.input.jumping);
            }
        }
    }

    @SubscribeEvent
    public static void keyInputEvent(InputEvent.KeyInputEvent event) {
        ReminiscenceClient.keyInputEvent(event);
    }


    @SubscribeEvent
    public static void mouseScrollEvent(InputEvent.MouseScrollEvent event) {
        ReminiscenceClient.mouseScrollEvent(event);
    }

    @SubscribeEvent
    public static void screenOpenEvent(ScreenOpenEvent event) {
        ReminiscenceClient.screenOpenEvent(event);
    }

}
