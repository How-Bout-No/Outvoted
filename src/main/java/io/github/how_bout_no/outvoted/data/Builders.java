package io.github.how_bout_no.outvoted.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Extends and overrides {@link ShapedRecipeBuilder} and {@link SmithingRecipeBuilder} to be compatible with creativetab config
 * Yea, it's a lot just for that
 */
class ShapedBuilder extends ShapedRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;

    public ShapedBuilder(IItemProvider resultIn, int countIn) {
        super(resultIn, countIn);
        this.result = resultIn.asItem();
        this.count = countIn;
    }

    public static ShapedRecipeBuilder shapedRecipe(IItemProvider resultIn) {
        return shapedRecipe(resultIn, 1);
    }

    public static ShapedRecipeBuilder shapedRecipe(IItemProvider resultIn, int countIn) {
        return new ShapedBuilder(resultIn, countIn);
    }

    @Override
    public ShapedRecipeBuilder key(Character symbol, ITag<Item> tagIn) {
        return this.key(symbol, Ingredient.fromTag(tagIn));
    }

    @Override
    public ShapedRecipeBuilder key(Character symbol, IItemProvider itemIn) {
        return this.key(symbol, Ingredient.fromItems(itemIn));
    }

    @Override
    public ShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredientIn);
            return this;
        }
    }

    @Override
    public ShapedRecipeBuilder patternLine(String patternIn) {
        if (!this.pattern.isEmpty() && patternIn.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternIn);
            return this;
        }
    }

    @Override
    public ShapedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
        this.advancementBuilder.withCriterion(name, criterionIn);
        return this;
    }

    @Override
    public ShapedRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }

    @Override
    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    @Override
    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Shaped Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, new ResourceLocation(save));
        }
    }

    @Override
    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(new ShapedRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.pattern, this.key, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/misc/" + id.getPath())));
    }
}

class SmithingBuilder extends SmithingRecipeBuilder {
    private final Ingredient base;
    private final Ingredient addition;
    private final Item output;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private final IRecipeSerializer<?> serializer;

    public SmithingBuilder(IRecipeSerializer<?> serializer, Ingredient base, Ingredient addition, Item output) {
        super(serializer, base, addition, output);
        this.serializer = serializer;
        this.base = base;
        this.addition = addition;
        this.output = output;
    }

    public static SmithingRecipeBuilder smithingRecipe(Ingredient base, Ingredient addition, Item output) {
        return new SmithingBuilder(IRecipeSerializer.SMITHING, base, addition, output);
    }

    @Override
    public SmithingRecipeBuilder addCriterion(String name, ICriterionInstance criterion) {
        this.advancementBuilder.withCriterion(name, criterion);
        return this;
    }

    @Override
    public void build(Consumer<IFinishedRecipe> consumer, String id) {
        this.build(consumer, new ResourceLocation(id));
    }

    @Override
    public void build(Consumer<IFinishedRecipe> recipe, ResourceLocation id) {
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        recipe.accept(new SmithingRecipeBuilder.Result(id, this.serializer, this.base, this.addition, this.output, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/combat/" + id.getPath())));
    }
}
