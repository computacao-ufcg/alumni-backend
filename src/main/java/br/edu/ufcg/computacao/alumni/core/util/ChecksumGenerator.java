package br.edu.ufcg.computacao.alumni.core.util;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumGenerator {
    public static long getCRC32Checksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }
}
