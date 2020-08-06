package com.github.creeper123123321.f3serverinfo.mixin;

import com.github.creeper123123321.f3serverinfo.PacketRateMeasurer;
import com.github.creeper123123321.f3serverinfo.TickRateMeasurer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.OverlayDebug;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OverlayDebug.class)
public class DebugHudMixin {
    @Inject(at = @At("RETURN"), method = "method_2505")
    private void returnLeftText(CallbackInfoReturnable<List<String>> cir) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return;
        String msg;
        float tx = ((PacketRateMeasurer) MinecraftClient.getInstance().getNetworkHandler().getClientConnection()).getAvgTx();
        float rx = ((PacketRateMeasurer) MinecraftClient.getInstance().getNetworkHandler().getClientConnection()).getAvgRx();
        if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
            double tickTime = ((TickRateMeasurer) MinecraftServer.getServer()).getAvgTickTime();
            msg = String.format("Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", tickTime, tx, rx);
        } else {
            msg = String.format("\"%s\" server, %.0f tx, %.0f rx", MinecraftClient.getInstance().player.getServerBrand(), tx, rx);
        }
        cir.getReturnValue().add(2, "[F3ServerInfo] " + msg);
    }
}
