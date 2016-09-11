package com.helospark.FakeSsh.compression.implementation;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;

import com.helospark.FakeSsh.compression.SshCompression;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

/**
 * Implementation of {@link SshCompression} that uses Zlib to compress and decompress data.
 * This implementation uses JZlib as it's backend
 * @author helospark
 */
public class ZlibSshCompression implements SshCompression {
	private Deflater compresser;
	private Inflater decompresser;

	public ZlibSshCompression(Deflater compresser, Inflater decompresser) {
		this.compresser = compresser;
		this.decompresser = decompresser;
	}

	@Override
	@SuppressWarnings("deprecation")
	public byte[] compress(byte[] data) {
		byte[] compressedDataBuffer = new byte[100];
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		synchronized (compresser) {
			compresser.setInput(data);
			while (compresser.next_in_index < data.length) {
				compresser.setOutput(compressedDataBuffer);
				compresser.deflate(JZlib.Z_PARTIAL_FLUSH);
				byteStream.write(compressedDataBuffer, 0, compressedDataBuffer.length - compresser.avail_out);
			}
		}
		return byteStream.toByteArray();
	}

	@Override
	public byte[] decompress(byte[] data) {
		try {
			return doDecompress(data);
		} catch (Exception e) {
			throw new RuntimeException("Unable to decompress", e);
		}
	}

	@SuppressWarnings("deprecation")
	private byte[] doDecompress(byte[] data) throws DataFormatException {
		byte[] decompressedDataBuffer = new byte[100];
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		synchronized (decompresser) {
			decompresser.setInput(data);
			while (true) {
				decompresser.setOutput(decompressedDataBuffer);
				int errorCode = decompresser.inflate(JZlib.Z_PARTIAL_FLUSH);
				if (errorCode == -5) {
					break;
				} else if (errorCode < 0) {
					throw new RuntimeException("asd");
				}
				byteStream.write(decompressedDataBuffer, 0, decompressedDataBuffer.length - decompresser.avail_out);
			}
		}
		return byteStream.toByteArray();
	}

}
