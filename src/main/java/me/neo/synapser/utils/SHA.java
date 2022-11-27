package me.neo.synapser.utils;

import me.neo.synapser.types.Longs;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    public static class SHABase {
        protected MessageDigest md;
        public void update(byte[] value) {
            md.update(value);
        }

        public void update(String value) {
            update(value.getBytes(StandardCharsets.UTF_8));
        }

        public void update(long value) {
            update(Longs.longToBytes(value));
        }

        public void update(int value) {
            update(ByteBuffer.allocate(4).putInt(value).array());
        }

        public byte[] hexdigest() {
            return md.digest();
        }
    }

    public static class SHA1 extends SHABase {
        public SHA1() {
            try {
                md = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class SHA256 extends SHABase {
        public SHA256() {
            try {
                md = MessageDigest.getInstance("SHA256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
