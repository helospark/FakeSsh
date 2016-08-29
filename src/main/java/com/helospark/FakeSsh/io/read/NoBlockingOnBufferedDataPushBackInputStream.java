package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Proxy around an inputstream that either returns a previously added buffer, or delegates to stream.
 * @author helospark
 */
public class NoBlockingOnBufferedDataPushBackInputStream extends PushbackInputStream {
	int bufferIndex = 0;

	public NoBlockingOnBufferedDataPushBackInputStream(InputStream in, int size) {
		super(in, size);
	}

	public NoBlockingOnBufferedDataPushBackInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int avail = buf.length - pos;
		int readBytes = 0;
		if (avail > 0) {
			if (len < avail) {
				avail = len;
			}
			System.arraycopy(buf, pos, b, off, avail);
			pos += avail;
			off += avail;
			len -= avail;
		} else {
			len = super.read(b, off, len);
			if (len == -1) {
				return avail == 0 ? -1 : avail;
			}
			return avail + len;
		}
		return avail;
	}

}
