package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import static net.minecraft.entity.EntityType.SMALL_FIREBALL;

public class WildfireFireballEntity extends AbstractFireballEntity {
    public double explosionPower = Outvoted.config.entities.wildfire.attacking.fireballexplosionpower;
    public boolean doExplode = Outvoted.config.entities.wildfire.attacking.dofireballexplosion;

    public WildfireFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(SMALL_FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when the fireball hits an entity
     */
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (!this.world.isClient) {
            Entity entity = result.getEntity();
            if (!entity.isFireImmune()) {
                Entity entity1 = this.getOwner();
                int i = entity.getFireTicks();
                entity.setOnFireFor(5);
                boolean flag = entity.damage(DamageSource.fireball(this, entity1), 5.0F);
                if (!flag) {
                    entity.setFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.dealDamage((LivingEntity) entity1, entity);
                }
            }

        }
    }

    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (!this.world.isClient) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                BlockPos blockpos = result.getBlockPos().offset(result.getSide());
                if (this.world.isAir(blockpos)) {
                    this.world.setBlockState(blockpos, AbstractFireBlock.getState(this.world, blockpos));
                }
            }

        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onCollision(HitResult result) {
        super.onCollision(result);
        if (!this.world.isClient) {
            if (doExplode) {
                boolean flag = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && doExplode;
                this.world.createExplosion((Entity) null, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, flag, flag ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE);
            }
            this.remove();
        }

    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean collides() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}
