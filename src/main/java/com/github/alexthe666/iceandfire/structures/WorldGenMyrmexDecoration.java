package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenMyrmexDecoration {

    public static final ResourceLocation MYRMEX_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "myrmex_hive"));

    public static void generateSkeleton(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        EnumFacing direction = EnumFacing.HORIZONTALS[new Random().nextInt(3)];
        EnumFacing.Axis oppositeAxis = direction.getAxis() == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X;
        int maxRibHeight = rand.nextInt(2);
        for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
            BlockPos segment = blockpos.offset(direction, spine);
            if (origin.distanceSq(segment) <= (double) (radius * radius)) {
                worldIn.setBlockState(segment, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, direction.getAxis()));
            }
            if (spine % 2 != 0) {
                BlockPos rightRib = segment.offset(direction.rotateYCCW());
                BlockPos leftRib = segment.offset(direction.rotateY());
                if (origin.distanceSq(rightRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.distanceSq(rightRib.up(ribHeight).offset(direction.rotateYCCW())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(rightRib.up(ribHeight).offset(direction.rotateYCCW()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                    if (origin.distanceSq(leftRib.up(ribHeight).offset(direction.rotateY())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(leftRib.up(ribHeight).offset(direction.rotateY()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                }
                if (origin.distanceSq(rightRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
            }

        }
    }

    public static void generateLeaves(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isFullCube()) {
            IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
            for (BiomeDictionary.Type type : BiomeDictionary.getTypes(worldIn.getBiome(blockpos))) {
                if (type == BiomeDictionary.Type.SANDY || type == BiomeDictionary.Type.SAVANNA || type == BiomeDictionary.Type.WASTELAND) {
                    leaf = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                    break;
                }
                if (type == BiomeDictionary.Type.JUNGLE) {
                    leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                    break;
                }
                if (type == BiomeDictionary.Type.CONIFEROUS) {
                    leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                    break;
                }
            }
            for (int i = blockpos.getY(); i <= blockpos.getY() + 2; ++i) {
                int j = i - blockpos.getY();
                int k = 2 - j;

                for (int l = blockpos.getX() - k; l <= blockpos.getX() + k; ++l) {
                    int i1 = l - blockpos.getX();

                    for (int j1 = blockpos.getZ() - k; j1 <= blockpos.getZ() + k; ++j1) {
                        int k1 = j1 - blockpos.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0) {
                            BlockPos blockpos2 = new BlockPos(l, i, j1);
                            IBlockState state = worldIn.getBlockState(blockpos2);

                            if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos2)) {
                                worldIn.setBlockState(blockpos2, leaf, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void generatePumpkins(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isFullCube()) {
            worldIn.setBlockState(blockpos, BiomeDictionary.hasType(worldIn.getBiome(blockpos), BiomeDictionary.Type.JUNGLE) ? Blocks.MELON_BLOCK.getDefaultState() : Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(rand.nextInt(3))));
        }
    }

    public static void generateMushrooms(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)) {
            worldIn.setBlockState(blockpos, rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState());
        }
    }

    public static void generateGold(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        IBlockState gold = rand.nextBoolean() ? ModBlocks.goldPile.getDefaultState() : ModBlocks.silverPile.getDefaultState();
        if (worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)) {
            worldIn.setBlockState(blockpos, gold.withProperty(BlockGoldPile.LAYERS, 8), 3);
            worldIn.setBlockState(blockpos.north(), gold.withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
            worldIn.setBlockState(blockpos.south(), gold.withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
            worldIn.setBlockState(blockpos.west(), gold.withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
            worldIn.setBlockState(blockpos.east(), gold.withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
            if (rand.nextInt(3) == 0) {
                worldIn.setBlockState(blockpos.up(), Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 3);
                if (worldIn.getBlockState(blockpos.up()).getBlock() instanceof BlockChest) {
                    TileEntity tileentity1 = worldIn.getTileEntity(blockpos.up());
                    if (tileentity1 instanceof TileEntityChest && !((TileEntityChest) tileentity1).isInvalid()) {
                        ((TileEntityChest) tileentity1).setLootTable(MYRMEX_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    public static void generateTrashHeap(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if (worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)) {
            Block blob = Blocks.DIRT;
            switch (rand.nextInt(3)) {
                case 0:
                    blob = Blocks.DIRT;
                    break;
                case 1:
                    blob = Blocks.SAND;
                    break;
                case 2:
                    blob = Blocks.COBBLESTONE;
                    break;
                case 3:
                    blob = Blocks.GRAVEL;
                    break;
            }
            int i1 = 0;
            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos pos : BlockPos.getAllInBox(blockpos.add(-j, -k, -l), blockpos.add(j, k, l))) {
                    if (pos.distanceSq(blockpos) <= (double) (f * f)) {
                        worldIn.setBlockState(pos, blob.getDefaultState(), 4);
                    }
                }
                blockpos = blockpos.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }

        }
    }

    public static void generateTrashOre(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        Block current = worldIn.getBlockState(blockpos).getBlock();
        if (origin.distanceSq(blockpos) <= (double) (radius * radius)) {
            if (current == Blocks.DIRT || current == Blocks.SAND || current == Blocks.COBBLESTONE || current == Blocks.GRAVEL) {
                Block ore = Blocks.REDSTONE_ORE;
                if (rand.nextInt(3) == 0) {
                    ore = rand.nextBoolean() ? Blocks.GOLD_ORE : ModBlocks.silverOre;
                } else if (rand.nextInt(3) == 0) {
                    ore = Blocks.DIAMOND_ORE;
                } else if (rand.nextInt(2) == 0) {
                    ore = rand.nextBoolean() ? Blocks.EMERALD_ORE : ModBlocks.sapphireOre;
                }
                worldIn.setBlockState(blockpos, ore.getDefaultState());
            }
        }
    }
}