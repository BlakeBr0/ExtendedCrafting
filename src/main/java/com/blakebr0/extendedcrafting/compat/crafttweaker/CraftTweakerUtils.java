package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.data.IData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CraftTweakerUtils {
//	public static String writeTag(NBTBase nbt) {
//		return from(nbt).toString();
//	}
//
//	private static IData from(NBTBase nbt) {
//        if (nbt == null) return null;
//
//        switch (nbt.getId()) {
//            case 1: // byte
//                return new DataByte(((NBTPrimitive) nbt).getByte());
//            case 2: // short
//                return new DataShort(((NBTPrimitive) nbt).getShort());
//            case 3: // int
//                return new DataInt(((NBTPrimitive) nbt).getInt());
//            case 4: // long
//                return new DataLong(((NBTPrimitive) nbt).getLong());
//            case 5: // float
//                return new DataFloat(((NBTPrimitive) nbt).getFloat());
//            case 6: // double
//                return new DataDouble(((NBTPrimitive) nbt).getDouble());
//            case 7: // byte[]
//                return new DataByteArray(((NBTTagByteArray) nbt).getByteArray(), false);
//            case 8: // string
//                return new DataString(((NBTTagString) nbt).getString());
//            case 9: { // list
//                List<NBTBase> original = new ArrayList<>();
//                ((NBTTagList) nbt).iterator().forEachRemaining(original::add);
//                List<IData> values = new ArrayList<>(original.stream().map(value -> from(value)).collect(Collectors.toList()));
//                return new DataList(values, false);
//            }
//            case 10: { // compound
//                Map<String, IData> values = new HashMap<>();
//                NBTTagCompound original = (NBTTagCompound) nbt;
//                for (String key : original.getKeySet()) {
//                    values.put(key, from(original.getTag(key)));
//                }
//
//                return new DataMap(values, false);
//            }
//            case 11: // int[]
//                return new DataIntArray(((NBTTagIntArray) nbt).getIntArray(), false);
//            default:
//                throw new RuntimeException("Unknown tag type: " + nbt.getId());
//        }
//    }
}
