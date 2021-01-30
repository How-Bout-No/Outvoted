package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import static net.minecraft.entity.EntityType.SMALL_FIREBALL;

public class InfernoFireballEntity extends AbstractFireballEntity {
    public double explosionPower = OutvotedConfig.COMMON.infernofireballexplosionpower.get();
    public boolean doExplode = OutvotedConfig.COMMON.infernodofireballexplosion.get();

    public InfernoFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(SMALL_FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when the fireball hits an entity
     */
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        super.onEntityHit(p_213868_1_);
        if (!this.world.isRemote) {
            Entity entity = p_213868_1_.getEntity();
            if (!entity.isImmuneToFire()) {
                Entity entity1 = this.func_234616_v_();
                int i = entity.getFireTimer();
                entity.setFire(5);
                boolean flag = entity.attackEntityFrom(DamageSource.func_233547_a_(this, entity1), 5.0F);
                if (!flag) {
                    entity.forceFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.applyEnchantments((LivingEntity) entity1, entity);
                }
            }

        }
    }

    protected void func_230299_a_(BlockRayTraceResult p_230299_1_) {
        super.func_230299_a_(p_230299_1_);
        if (!this.world.isRemote) {
            Entity entity = this.func_234616_v_();
            if (entity == null || !(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getEntity())) {
                BlockPos blockpos = p_230299_1_.getPos().offset(p_230299_1_.getFace());
                if (this.world.isAirBlock(blockpos)) {
                    this.world.setBlockState(blockpos, AbstractFireBlock.getFireForPlacement(this.world, blockpos));
                }
            }

        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            if (doExplode) {
                boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_()) && doExplode;
                this.world.createExplosion((Entity) null, this.getPosX(), this.getPosY(), this.getPosZ(), (float) this.explosionPower, flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            }
            this.remove();
        }

    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
