package com.github.creeper123123321.f3serverinfo.mixin;

import com.github.creeper123123321.f3serverinfo.PacketRateMeasurer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin implements PacketRateMeasurer {
    @Unique
    private int f3serverinfo_amountTicks, f3serverinfo_amountRx, f3serverinfo_amountTx;
    @Unique
    private float f3serverinfo_avgTx, f3serverinfo_avgRx;

    @Inject(method = "method_7401", at = @At("HEAD"))
    private void onSend(Packet packet, GenericFutureListener<? extends Future<? super Void>>[] genericFutureListeners, CallbackInfo ci) {
        f3serverinfo_amountTx++;
    }

    @Inject(method = "channelRead0", at = @At("HEAD"))
    private void onReceive(ChannelHandlerContext channelHandlerContext, Packet packet, CallbackInfo ci) {
        f3serverinfo_amountRx++;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onSend(CallbackInfo ci) {
        if (++f3serverinfo_amountTicks == 20) {
            f3serverinfo_avgRx *= 0.75;
            f3serverinfo_avgRx += 0.25 * f3serverinfo_amountRx;
            f3serverinfo_avgTx *= 0.75;
            f3serverinfo_avgTx += 0.25 * f3serverinfo_amountTx;

            f3serverinfo_amountTicks = 0;
            f3serverinfo_amountTx = 0;
            f3serverinfo_amountRx = 0;
        }
    }

    @Override
    public float getAvgTx() {
        return f3serverinfo_avgTx;
    }

    @Override
    public float getAvgRx() {
        return f3serverinfo_avgRx;
    }
}
