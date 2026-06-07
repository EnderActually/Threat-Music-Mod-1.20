package dev.fouriiiis.threatmusicmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.fouriiiis.threatmusicmod.ThreatTracker;

@Mixin(ClientPlayNetworkHandler.class)
public class CheckDamageMixin {

    @Shadow private ClientWorld world;

    @Inject(method = "onEntityDamage", at = @At("TAIL"))
    public void onEntityDamage(EntityDamageS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity damagedEntity = world.getEntityById(packet.entityId());
        Entity sourceEntity = world.getEntityById(packet.sourceCauseId());

        if (damagedEntity != null && damagedEntity == client.player) {
            if (sourceEntity != null && sourceEntity != client.player) {
                ThreatTracker.trackEntity(sourceEntity);
            }
        } else if (damagedEntity instanceof LivingEntity && sourceEntity != null) {
            Entity attacker = ThreatTracker.resolveDamageSource(sourceEntity);
            if (attacker == client.player) {
                ThreatTracker.trackPlayerAttackedEntity(damagedEntity);
            }
        }
    }
}
