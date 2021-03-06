package com.brandon3055.draconicevolution.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by brandon3055 on 28/09/2016.
 */
public class TileDissEnchanter extends TileInventoryBase {

    public TileDissEnchanter() {
        setInventorySize(3);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return stack.isItemEnchanted();
        }
        else if (index == 1) {
            return stack.getItem() == Items.BOOK;
        }
        else return false;
    }

    @Override
    public void receivePacketFromClient(PacketTileMessage packet, EntityPlayerMP client) {
        ItemStack input = getStackInSlot(0);
        ItemStack books = getStackInSlot(1);
        ItemStack output = getStackInSlot(2);

        if (input == null || !input.isItemEnchanted() || books == null || books.stackSize <= 0 || output != null) {
            return;
        }

        NBTTagList list = input.getEnchantmentTagList();
        if (list == null) {
            return;
        }

        int targetId = packet.intValue;

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            int id = compound.getShort("id");
            int lvl = compound.getShort("lvl");
            Enchantment e = Enchantment.getEnchantmentByID(id);

            if (e == null || id != targetId) {
                continue;
            }

            int cost = (int)(((double) lvl / (double) e.getMaxLevel()) * 20);

            if (!client.capabilities.isCreativeMode && cost > client.experienceLevel) {
                client.addChatComponentMessage(new TextComponentTranslation("chat.dissEnchanter.notEnoughLevels.msg", cost).setStyle(new Style().setColor(TextFormatting.RED)));
                return;
            }

            if (!client.capabilities.isCreativeMode) {
                client.removeExperienceLevel(cost);
            }

            NBTTagCompound stackCompound = input.getTagCompound();
            if (stackCompound == null) {
                return;
            }

            books.stackSize--;
            if (books.stackSize <= 0) {
                setInventorySlotContents(1, null);
            }

            int repairCost = stackCompound.getInteger("RepairCost");
            repairCost -= ((double)repairCost * (1D / list.tagCount()));
            stackCompound.setInteger("RepairCost", repairCost);

            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            Items.ENCHANTED_BOOK.addEnchantment(book, new EnchantmentData(e, lvl));
            setInventorySlotContents(2, book);
            list.removeTag(i);

            if (list.tagCount() <= 0) {
                stackCompound.removeTag("ench");
            }

            return;
        }
    }
}
