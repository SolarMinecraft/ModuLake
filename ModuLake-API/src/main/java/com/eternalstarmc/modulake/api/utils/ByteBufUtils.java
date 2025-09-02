package com.eternalstarmc.modulake.api.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Netty ByteBuf 工具类，提供便捷的读写操作
 */
public final class ByteBufUtils {

    private ByteBufUtils() {
        // 防止实例化
    }

    // ==================== String 操作 ====================

    /**
     * 写入字符串（UTF-8编码）
     *
     * @param buffer ByteBuf
     * @param str    要写入的字符串
     */
    public static void writeString(ByteBuf buffer, String str) {
        writeString(buffer, str, StandardCharsets.UTF_8);
    }

    /**
     * 写入字符串（指定编码）
     *
     * @param buffer  ByteBuf
     * @param str     要写入的字符串
     * @param charset 字符编码
     */
    public static void writeString(ByteBuf buffer, String str, Charset charset) {
        if (str == null) {
            buffer.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(charset);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    /**
     * 读取字符串（UTF-8编码）
     *
     * @param buffer ByteBuf
     * @return 读取的字符串
     */
    public static String readString(ByteBuf buffer) {
        return readString(buffer, StandardCharsets.UTF_8);
    }

    /**
     * 读取字符串（指定编码）
     *
     * @param buffer  ByteBuf
     * @param charset 字符编码
     * @return 读取的字符串
     */
    public static String readString(ByteBuf buffer, Charset charset) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, charset);
    }

    // ==================== 字节数组操作 ====================

    /**
     * 写入字节数组（带长度前缀）
     *
     * @param buffer ByteBuf
     * @param bytes  字节数组
     */
    public static void writeBytes(ByteBuf buffer, byte[] bytes) {
        if (bytes == null) {
            buffer.writeInt(-1);
            return;
        }
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    /**
     * 读取字节数组（带长度前缀）
     *
     * @param buffer ByteBuf
     * @return 读取的字节数组
     */
    public static byte[] readBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    // ==================== Base64 操作 ====================

    /**
     * 写入Base64编码的字符串
     *
     * @param buffer ByteBuf
     * @param str    要写入的字符串
     */
    public static void writeBase64String(ByteBuf buffer, String str) {
        if (str == null) {
            buffer.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] encoded = java.util.Base64.getEncoder().encode(bytes);
        buffer.writeInt(encoded.length);
        buffer.writeBytes(encoded);
    }

    /**
     * 读取Base64编码的字符串
     *
     * @param buffer ByteBuf
     * @return 解码后的字符串
     */
    public static String readBase64String(ByteBuf buffer) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        byte[] encoded = new byte[length];
        buffer.readBytes(encoded);
        byte[] decoded = java.util.Base64.getDecoder().decode(encoded);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    // ==================== UUID 操作 ====================

    /**
     * 写入UUID
     *
     * @param buffer ByteBuf
     * @param uuid   要写入的UUID
     */
    public static void writeUUID(ByteBuf buffer, UUID uuid) {
        if (uuid == null) {
            buffer.writeLong(-1L);
            buffer.writeLong(-1L);
            return;
        }
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * 读取UUID
     *
     * @param buffer ByteBuf
     * @return 读取的UUID
     */
    public static UUID readUUID(ByteBuf buffer) {
        long mostSigBits = buffer.readLong();
        long leastSigBits = buffer.readLong();
        if (mostSigBits == -1L && leastSigBits == -1L) {
            return null;
        }
        return new UUID(mostSigBits, leastSigBits);
    }

    // ==================== 可变长度整数操作 ====================

    /**
     * 写入可变长度整数（VarInt）
     *
     * @param buffer ByteBuf
     * @param value  要写入的整数值
     */
    public static void writeVarInt(ByteBuf buffer, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                buffer.writeByte(value);
                return;
            } else {
                buffer.writeByte((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * 读取可变长度整数（VarInt）
     *
     * @param buffer ByteBuf
     * @return 读取的整数值
     */
    public static int readVarInt(ByteBuf buffer) {
        int value = 0;
        int length = 0;
        byte current;

        do {
            current = buffer.readByte();
            value |= (current & 0x7F) << (length * 7);

            if (++length > 5) {
                throw new IllegalArgumentException("VarInt too big");
            }
        } while ((current & 0x80) == 0x80);

        return value;
    }

    /**
     * 写入可变长度长整数（VarLong）
     *
     * @param buffer ByteBuf
     * @param value  要写入的长整数值
     */
    public static void writeVarLong(ByteBuf buffer, long value) {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                buffer.writeByte((int) value);
                return;
            } else {
                buffer.writeByte((int) ((value & 0x7F) | 0x80));
                value >>>= 7;
            }
        }
    }

    /**
     * 读取可变长度长整数（VarLong）
     *
     * @param buffer ByteBuf
     * @return 读取的长整数值
     */
    public static long readVarLong(ByteBuf buffer) {
        long value = 0;
        int length = 0;
        byte current;

        do {
            current = buffer.readByte();
            value |= (current & 0x7FL) << (length * 7);

            if (++length > 10) {
                throw new IllegalArgumentException("VarLong too big");
            }
        } while ((current & 0x80) == 0x80);

        return value;
    }

    // ==================== 十六进制操作 ====================

    /**
     * 写入十六进制字符串
     *
     * @param buffer ByteBuf
     * @param hexStr 十六进制字符串
     */
    public static void writeHex(ByteBuf buffer, String hexStr) {
        if (hexStr == null) {
            buffer.writeInt(-1);
            return;
        }
        byte[] bytes = ByteBufUtil.decodeHexDump(hexStr);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    /**
     * 读取十六进制字符串
     *
     * @param buffer ByteBuf
     * @return 十六进制字符串
     */
    public static String readHex(ByteBuf buffer) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return ByteBufUtil.hexDump(bytes);
    }

    // ==================== 布尔值操作 ====================

    /**
     * 写入布尔值
     *
     * @param buffer ByteBuf
     * @param value  布尔值
     */
    public static void writeBoolean(ByteBuf buffer, boolean value) {
        buffer.writeByte(value ? 1 : 0);
    }

    /**
     * 读取布尔值
     *
     * @param buffer ByteBuf
     * @return 布尔值
     */
    public static boolean readBoolean(ByteBuf buffer) {
        return buffer.readByte() == 1;
    }

    // ==================== 数字类型操作 ====================

    /**
     * 写入整数（带null检查）
     *
     * @param buffer ByteBuf
     * @param value  整数值
     */
    public static void writeInteger(ByteBuf buffer, Integer value) {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeInt(value);
        }
    }

    /**
     * 读取整数（带null检查）
     *
     * @param buffer ByteBuf
     * @return 整数值
     */
    public static Integer readInteger(ByteBuf buffer) {
        byte flag = buffer.readByte();
        if (flag == 0) {
            return null;
        }
        return buffer.readInt();
    }

    /**
     * 写入长整数（带null检查）
     *
     * @param buffer ByteBuf
     * @param value  长整数值
     */
    public static void writeLong(ByteBuf buffer, Long value) {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeLong(value);
        }
    }

    /**
     * 读取长整数（带null检查）
     *
     * @param buffer ByteBuf
     * @return 长整数值
     */
    public static Long readLong(ByteBuf buffer) {
        byte flag = buffer.readByte();
        if (flag == 0) {
            return null;
        }
        return buffer.readLong();
    }

    /**
     * 写入短整数（带null检查）
     *
     * @param buffer ByteBuf
     * @param value  短整数值
     */
    public static void writeShort(ByteBuf buffer, Short value) {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeShort(value);
        }
    }

    /**
     * 读取短整数（带null检查）
     *
     * @param buffer ByteBuf
     * @return 短整数值
     */
    public static Short readShort(ByteBuf buffer) {
        byte flag = buffer.readByte();
        if (flag == 0) {
            return null;
        }
        return buffer.readShort();
    }

    /**
     * 写入浮点数（带null检查）
     *
     * @param buffer ByteBuf
     * @param value  浮点数值
     */
    public static void writeFloat(ByteBuf buffer, Float value) {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeFloat(value);
        }
    }

    /**
     * 读取浮点数（带null检查）
     *
     * @param buffer ByteBuf
     * @return 浮点数值
     */
    public static Float readFloat(ByteBuf buffer) {
        byte flag = buffer.readByte();
        if (flag == 0) {
            return null;
        }
        return buffer.readFloat();
    }

    /**
     * 写入双精度浮点数（带null检查）
     *
     * @param buffer ByteBuf
     * @param value  双精度浮点数值
     */
    public static void writeDouble(ByteBuf buffer, Double value) {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeDouble(value);
        }
    }

    /**
     * 读取双精度浮点数（带null检查）
     *
     * @param buffer ByteBuf
     * @return 双精度浮点数值
     */
    public static Double readDouble(ByteBuf buffer) {
        byte flag = buffer.readByte();
        if (flag == 0) {
            return null;
        }
        return buffer.readDouble();
    }

    // ==================== 枚举操作 ====================

    /**
     * 写入枚举（通过序号）
     *
     * @param buffer ByteBuf
     * @param value  枚举值
     */
    public static void writeEnum(ByteBuf buffer, Enum<?> value) {
        if (value == null) {
            buffer.writeInt(-1);
        } else {
            buffer.writeInt(value.ordinal());
        }
    }

    /**
     * 读取枚举（通过序号）
     *
     * @param buffer    ByteBuf
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 枚举值
     */
    public static <T extends Enum<T>> T readEnum(ByteBuf buffer, Class<T> enumClass) {
        int ordinal = buffer.readInt();
        if (ordinal == -1) {
            return null;
        }
        try {
            return enumClass.getEnumConstants()[ordinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid ordinal for enum " + enumClass.getName() + ": " + ordinal);
        }
    }

    // ==================== 空值检查 ====================

    /**
     * 检查ByteBuf是否可读指定长度
     *
     * @param buffer ByteBuf
     * @param length 需要读取的长度
     * @return 是否可读
     */
    public static boolean isReadable(ByteBuf buffer, int length) {
        return buffer.isReadable() && buffer.readableBytes() >= length;
    }

    /**
     * 安全读取字符串，如果无法读取则返回默认值
     *
     * @param buffer     ByteBuf
     * @param defaultVal 默认值
     * @return 读取的字符串或默认值
     */
    public static String readStringSafely(ByteBuf buffer, String defaultVal) {
        if (!isReadable(buffer, 4)) {
            return defaultVal;
        }
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        if (!isReadable(buffer, length)) {
            return defaultVal;
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
