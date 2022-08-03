package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import static net.minecraft.world.entity.EntityType.SMALL_FIREBALL;

public class WildfireFireballEntity extends Fireball {
    public double explosionPower = Config.wildfireExplodePower.get();
    public boolean doExplode = Config.wildfireDoFireballExplode.get();

    public WildfireFireballEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(SMALL_FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            Entity entity = result.getEntity();
            if (!entity.fireImmune()) {
                Entity entity1 = this.getOwner();
                int i = entity.getRemainingFireTicks();
                entity.setSecondsOnFire(5);
                boolean flag = entity.hurt(DamageSource.fireball(this, entity1), 5.0F);
                if (!flag) {
                    entity.setRemainingFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.doEnchantDamageEffects((LivingEntity) entity1, entity);
                }
            }

        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
                }
            }

        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            if (doExplode) {
                boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && doExplode;
                this.level.explode(null, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, flag, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
            }
            this.discard();
        }

    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}
