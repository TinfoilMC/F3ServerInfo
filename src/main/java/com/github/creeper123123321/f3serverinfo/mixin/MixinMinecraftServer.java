package com.github.creeper123123321.f3serverinfo.mixin;

import com.github.creeper123123321.f3serverinfo.TickRateMeasurer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements TickRateMeasurer {
    private float f3serverinfo_tickTime;

    @Inject(method = "setupWorld()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci){
        f3serverinfo_tickTime *= 0.8;
        f3serverinfo_tickTime += 0.2 * MinecraftServer.getServer().lastTickLengths[MinecraftServer.getServer().getTicks() % 100] * 1e-6;
    }

    @Override
    public float getAvgTickTime() {
        return f3serverinfo_tickTime;
    }
}
